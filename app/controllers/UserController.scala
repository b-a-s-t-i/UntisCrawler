package controllers

import play.api.mvc.{Results, Security, Action, Controller}

import scaldi.{Injector, Injectable}

import play.api.libs.concurrent.Execution.Implicits.defaultContext


class UserController(implicit inj: Injector) extends Controller with Injectable with ControllerUtils{


}
