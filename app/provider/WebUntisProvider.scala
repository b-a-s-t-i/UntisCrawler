package provider

import model._
import play.api.Logger
import play.api.libs.json.{JsObject, JsValue, Json}
import scaldi.{Injector, Injectable}
import services.WebUntisService
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.{JsonUtil, TimetableUtil}

import scala.concurrent.Future

trait WebUntisProvider {
  def doSchoolQuerty(query: String): Future[List[SchoolSearchResult]]
  def authenticate(server: String, school: String, username: String, password: String): Future[Option[String]]
  def loadList(server: String, school: String, username: String, password: String): Future[List[TimetableElementList]]
  def loadUserData(server: String, school: String, username: String, password: String): Future[TimetableUserData]
  def loadTimetable(server: String, school: String, username: String, password: String, elementType: Int, elementId: Int): Future[Option[List[Option[TimetableResponse]]]]
}

class WebUntisProviderImpl(implicit inj: Injector) extends WebUntisProvider with Injectable {

  val webuntisService: WebUntisService = inject[WebUntisService]

  override def doSchoolQuerty(query: String): Future[List[SchoolSearchResult]] = {
    webuntisService.doSchoolSearch(query).map{ response =>
      val result = JsonUtil.objectMapper.readValue[SchoolSearchResponse](response.body)
      if(result.result != null){
        result.result.schools
      }else{
        List()
      }
    }
  }

  override def authenticate(server: String, school: String, username: String, password: String): Future[Option[String]] = {
    webuntisService.doAuthentication(checkUrl(server), school, username, password).map{ response =>
      response.allHeaders.get("SET-COOKIE").map{ cookie =>
        cookie.distinct.foldRight("")((a,b) => a  + (if(!b.isEmpty || !a.isEmpty) ";" else "") + b)
      }
    }
  }

  override def loadList(server: String, school: String, username: String, password: String): Future[List[TimetableElementList]] = {
    authenticate(checkUrl(server), school, username, password).flatMap { cookie =>
      cookie match {
        case Some(c) => {
          val listFutures = Future.sequence((1 to 4).toList.map(webuntisService.getElementList(checkUrl(server), c, _)))
          listFutures.map { response =>
            response.map{ e =>
              val json = Json.parse(e._2.body).as[JsObject] ++ Json.obj("elementType" -> e._1)
              JsonUtil.objectMapper.readValue[TimetableElementList](json.toString())
            }
          }
        }
        case None => {
          Future {
            List()
          }
        }
      }
    }
  }

  override def loadUserData(server: String, school: String, username: String, password: String): Future[TimetableUserData] = {
    webuntisService.getUserData(checkUrl(server), school, username, password).map{ response =>
      JsonUtil.objectMapper.readValue[TimetableUserDataResponse](response.body).result
    }
  }

  private def checkUrl(url: String): String = {
    if(!url.startsWith("http")){
      s"https://${url}"
    }else{
      url
    }
  }

  override def loadTimetable(server: String, school: String, username: String, password: String, elementType: Int, elementId: Int): Future[Option[List[Option[TimetableResponse]]]] = {
    authenticate(server, school, username, password).flatMap{ auth =>
      auth match {
        case Some(cookie) => {
          val blub = Future.sequence(TimetableUtil.getRequestDate().map(webuntisService.getTimetable(server, cookie, elementType, elementId, _)).map{ data =>
            data.map(r => JsonUtil.parseTimetableResponse(r.body))
          })
          blub.map(Some(_))
        }
        case None => Future { None }
      }
    }
  }
}
