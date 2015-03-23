package controllers

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import provider.UserProvider
import scaldi.{Injector, Injectable}

import play.api.mvc._

class HomeController(implicit inj: Injector) extends Controller with Injectable with Secured{

  override val userProvider: UserProvider = inject[UserProvider]
  override val tokenVerifier: GoogleIdTokenVerifier = inject[GoogleIdTokenVerifier]

  def index = Action { implicit request =>
    Ok(userProvider.getActivatedUser().toString())
  }

}
