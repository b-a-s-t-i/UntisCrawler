package controllers

import provider.UserProvider
import scaldi.{Injector, Injectable}

import play.api.mvc._

class HomeController(implicit inj: Injector) extends Controller with Injectable with Secured{

  override val userProvider: UserProvider = inject[UserProvider]

  def index = Action { implicit request =>
    Ok(userProvider.getActivatedUser().toString())
  }

}
