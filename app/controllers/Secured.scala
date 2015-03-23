package controllers

import java.io.IOException
import java.security.GeneralSecurityException

import com.google.api.client.googleapis.auth.oauth2.{GoogleIdTokenVerifier, GoogleIdToken}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import model.UiUser
import play.api.Logger
import play.api.mvc._
import provider.UserProvider
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future

trait Secured {

  val AUDIENCE = "870470316286-maa83ila3r2hnqkprjtilmajsjb53o0g.apps.googleusercontent.com"
  val AUTHORIZED_PARTY = "870470316286-2oq505l604nsv26osp4fi05mvhstlgcf.apps.googleusercontent.com"
  val HEADER = "Authorization"
  val userProvider: UserProvider

  lazy val jsonFactory = new JacksonFactory
  lazy val tokenVerifier = new GoogleIdTokenVerifier(new NetHttpTransport(), jsonFactory)

  def auth[A](bp: BodyParser[A])(f: (Request[A], UiUser) => Future[Result]) = {
    //pyramid of doom
    Action.async(bp) { request =>
      request.headers.get(HEADER) match{
        case Some(token) =>{
          verifyGoogleAuth(token).flatMap{ oGoogleId =>
            oGoogleId match {
              case Some(googleId) => {
                userProvider.getUserByEmail(googleId.getEmail) match {
                  case Some(user) => f(request, user)
                  case None =>{
                    val success = userProvider.addUser(googleId.getEmail)
                    if(success){
                      userProvider.getUserByEmail(googleId.getEmail) match{
                        case Some(nUser) => {
                          Logger.info(s"New user created: ${nUser.userId}" )
                          f(request, nUser)
                        }
                        case None => {
                          Logger.warn("User created but not found .... super weird")
                          Future { Results.InternalServerError }
                        }
                      }
                    }else{
                      Logger.warn("User creation failed")
                      Future { Results.InternalServerError }
                    }
                  }
                }
              }
              case None => Future { Results.Unauthorized("Error during auth") }
            }
          }
        }
        case None => Future{
          Results.BadRequest("No Auth Header")
        }
      }
    }
  }


  def verifyGoogleAuth(stringToken: String): Future[Option[GoogleIdToken.Payload]] = {
    Future {
      try {
        val token = GoogleIdToken.parse(jsonFactory, stringToken)
        if (tokenVerifier.verify(token)) {
          val tempPayload: GoogleIdToken.Payload = token.getPayload
          (tempPayload.getAudience, tempPayload.getAuthorizedParty) match {
            case (AUDIENCE, AUTHORIZED_PARTY) => Some(tempPayload)
            case _ => None
          }
        }else{
          Logger.info("Token not verified")
          None
        }
      } catch {
        case e1: GeneralSecurityException => {
          Logger.warn("Security issue: " + e1.getLocalizedMessage())
          None
        }
        case e2: IOException => {
          Logger.warn("Network problem: " + e2.getLocalizedMessage())
          None
        }
        case e3: IllegalArgumentException =>{
          Logger.warn("Illegal tkoen: " + e3.getLocalizedMessage())
          None
        }
      }
    }
  }
}
