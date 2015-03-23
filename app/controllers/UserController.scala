package controllers

import play.api.mvc.{Controller}
import provider.UserProvider

import scaldi.{Injector, Injectable}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.JsonUtil

import scala.concurrent.Future

class UserController(implicit inj: Injector) extends Controller with Injectable with ControllerUtils with Secured{

  override val userProvider: UserProvider = inject[UserProvider]

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

}
