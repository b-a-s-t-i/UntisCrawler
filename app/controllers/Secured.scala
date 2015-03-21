package controllers

import com.google.api.client.googleapis.auth.oauth2.{GoogleIdTokenVerifier, GoogleIdToken}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import play.api.Logger
import provider.UserProvider

trait Secured {

  val userProvider: UserProvider

  lazy val jsonFactory = new JacksonFactory
  lazy val tokenVerifier = new GoogleIdTokenVerifier(new NetHttpTransport(), jsonFactory)



  def verifyGoogleAuth(stringToken: String): Unit ={

    val token = GoogleIdToken.parse(jsonFactory, stringToken)
    if(tokenVerifier.verify(token)){
      val tempPayload :GoogleIdToken.Payload = token.getPayload

      Logger.info(
        tempPayload.getEmail + " " +
        tempPayload.getEmailVerified + " " +
        tempPayload.getHostedDomain + " " +
        tempPayload.getAudience + " " +
        tempPayload.getAuthorizedParty
      )

      Logger.info(tempPayload.toPrettyString)
      Logger.info(tempPayload.toString)

    }
//
//    GoogleIdToken.Payload payload = null;
//    try {
//      GoogleIdToken token = GoogleIdToken.parse(mJFactory, tokenString);
//      if (mVerifier.verify(token)) {
//        GoogleIdToken.Payload tempPayload = token.getPayload();
//        if (!tempPayload.getAudience().equals(mAudience))
//          mProblem = "Audience mismatch";
//        else if (!mClientIDs.contains(tempPayload.getIssuee()))
//          mProblem = "Client ID mismatch";
//        else
//          payload = tempPayload;
//      }
//    } catch (GeneralSecurityException e) {
//      mProblem = "Security issue: " + e.getLocalizedMessage();
//    } catch (IOException e) {
//      mProblem = "Network problem: " + e.getLocalizedMessage();
//    }
//    return payload;
  }
}
