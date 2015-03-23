package controllers

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import play.api.mvc.{Controller}
import provider.UserProvider

import scaldi.{Injector, Injectable}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.JsonUtil

import scala.concurrent.Future

class UserController(implicit inj: Injector) extends Controller with Injectable with ControllerUtils with Secured{

  override val userProvider: UserProvider = inject[UserProvider]
  override val tokenVerifier: GoogleIdTokenVerifier = inject[GoogleIdTokenVerifier]

  def register() = auth(parse.anyContent){ (request, user) =>
    Future{
      Ok(JsonUtil.getMobileResponse(user.userId, user.email))
    }
  }

  def getId() = auth(parse.anyContent) { (request, user) =>
    Future{
      Ok(JsonUtil.getMobileResponse(user.userId, user.userId))
    }
  }

  def registerPushNotification(pushId: String) = auth(parse.anyContent) { (request, user) =>
    Future{
      userProvider.addUserPushNotification(user.userId, pushId)
      Ok
    }
  }

}
