package controllers

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import model.{MobileTimetableUserDataResponse}
import play.api.mvc.{Controller}
import provider.{WebUntisProvider, UserProvider}
import scaldi.{Injector, Injectable}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.JsonUtil

import scala.concurrent.Future

class TimetableController(implicit inj: Injector) extends Controller with Injectable with Secured {

  override val userProvider: UserProvider = inject[UserProvider]
  override val tokenVerifier: GoogleIdTokenVerifier = inject[GoogleIdTokenVerifier]

  val webuntisProvider: WebUntisProvider = inject[WebUntisProvider]


  def schoolSearch(query: String) = auth(parse.anyContent) { (request, user) =>
    webuntisProvider.doSchoolQuerty(query).map(r => Ok(JsonUtil.getMobileResponse(user.userId, r)))
  }

  def loadTimetableData(server: String, school: String, username: String, password: String) = auth(parse.anyContent) { (request, user) =>
    val result = for{
      lists <- webuntisProvider.loadList(server, school, username, password)
      userData <- webuntisProvider.loadUserData(server, school, username, password)
    }yield MobileTimetableUserDataResponse(lists, userData)

    result.map(r => Ok(JsonUtil.getMobileResponse(user.userId, r)))
  }

  def saveTimetableConfig(server: String, school: String, username: String, password: String, elementId: Int, elementTyp: Int) = auth(parse.anyContent){ (request, user) =>
    Future{
      userProvider.addTimetableConfig(user.userId, server, school, username, password, elementId, elementTyp)
      Ok
    }
  }

}
