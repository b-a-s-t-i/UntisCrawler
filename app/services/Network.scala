package services

import play.api.{Logger, Play}
import play.api.Play.current
import play.api.libs.json.Json

import play.api.libs.ws._

import scala.concurrent.Future

class Network {

  def getTimetable(serverUrl: String, authCookie: String, elementType: Int, elementId: Int, date: Int) = {
    val form = Map(
      "ajaxCommand" -> Seq("getWeeklyTimetable"),
      "elementType" -> Seq(s"${elementType}"),
      "elementId" -> Seq(s"${elementId}"),
      "date" -> Seq(s"${date}")
    )
    doTimetableRequest(serverUrl, authCookie, form)
  }

  def getList(serverUrl: String, authCookie: String, elementType: Int) = {
    val form = Map(
      "ajaxCommand" -> Seq("getPageConfig"),
      "type" -> Seq(s"${elementType}")
    )
    doTimetableRequest(serverUrl, authCookie, form)
  }

  def schoolSearch(searchParams: String) = {
    val url = "https://query.webuntis.com/schoolquery/"
    val body = Json.obj(
      "jsonrpc" -> "2.0",
      "method" -> "searchSchool",
      "id" -> 0,
      "params" -> Json.arr(
        Json.obj(
          "search" -> searchParams
        )
      )
    )
    WS.url(url).post(body)
  }

  def authenticate(serverUrl: String, school: String, username: String, password: String): Future[WSResponse] = {
    val url = s"${serverUrl}/WebUntis/j_spring_security_check?request.preventCache=${System.currentTimeMillis()}"

    val form = Map(
      "buttonName" -> Seq("login"),
      "school" -> Seq(school),
      "j_username" -> Seq(username),
      "j_password" -> Seq(password)
    )
    WS.url(url).withFollowRedirects(false).post(form)
    //val cookieString = cookie.distinct.foldRight("")((a,b) => a  + (if(!b.isEmpty || !a.isEmpty) ";" else "") + b)
  }

  def authenticate2(serverUrl: String, school: String, username: String, password: String): Future[WSResponse] = {
    val url = s"${serverUrl}/WebUntis/jsonrpc.do?school=${school}"
    val body = Json.obj(
      "jsonrpc" -> "2.0",
      "method" -> "authenticate",
      "id" -> 0,
      "params" -> Json.obj(
        "user" -> username,
        "password" -> password
      )
    )
    Logger.info(body.toString())
    WS.url(url).post(body)
  }


  def doTimetableRequest(serverUrl: String, authCookie: String, requestParams: Map[String, Seq[String]]): Future[WSResponse] = {
    val urlAppendix = "/WebUntis/Timetable.do"
    val url = s"${serverUrl}${urlAppendix}?request.preventCache=${System.currentTimeMillis()}"
    WS.url(url).withHeaders("Cookie" -> authCookie).post(requestParams)
  }

  def push(): Unit ={
    val apiKey = Play.current.configuration.getString("gcm.api.key").get
    val endpoint = "https://android.googleapis.com/gcm/send"
    WS.url(endpoint)
      .withHeaders(
      "Content-Type" -> "application/json",
      "Authorization" -> apiKey)
      .post("")//TODO
  }

}
