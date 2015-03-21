package controllers

import play.api.Logger
import play.api.mvc.{Controller}
import provider.{WebUntisProvider, UserProvider}
import scaldi.{Injector, Injectable}
import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.Future


class TimetableController(implicit inj: Injector) extends Controller with Injectable with Secured {

  override val userProvider: UserProvider = inject[UserProvider]
  val webuntisProvider: WebUntisProvider = inject[WebUntisProvider]

//  def schoolSearch(query: String) = withAuthOAsync(parse.anyContent) { (request, user) =>
//    user match {
//      case Some(x) => webuntisProvider.doSchoolQuerty(query).map(Ok(_))
//      case None => Future{ Unauthorized }
//    }
//  }
//
//  def loadLists(se: String, sc: String, u: String, p: String) = withAuthOAsync(parse.anyContent) { (request, user) =>
//    user match{
//      case Some(x) => webuntisProvider.loadList(se, sc, u, p).map(Ok(_))
//      case None => Future{ Unauthorized }
//    }
//  }
//
//  def userData(se: String, sc: String, u: String, p: String) = withAuthOAsync(parse.anyContent) { (request, user) =>
//    user match{
//      case Some(x) => webuntisProvider.loadUserData(se, sc, u, p).map(Ok(_))
//      case None => Future{ Unauthorized }
//    }
//  }
//
//  def addConfig(se: String, sc: String, u: String, p: String, ei: Int, et: Int) = withAuthOAsync(parse.anyContent) { (request, user) =>
//    Logger.info(s"$se $sc $u $p $ei $et");
//    user match {
//      case Some(x) => Future{
//        userProvider.addTimetableConfig(x.userId, se, sc, u, p, ei, et)
//        Ok
//      }
//      case None => Future{ Unauthorized }
//    }
//  }
}
