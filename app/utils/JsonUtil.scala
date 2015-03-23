package utils

import java.util.UUID

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import model._
import play.api.Logger
import play.api.libs.json.{Json, JsValue}

import scala.util.{Failure, Success, Try}


object JsonUtil {

  val objectMapper = new ObjectMapper() with ScalaObjectMapper
  objectMapper.registerModule(DefaultScalaModule)
  objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def parseTimetableResponse(data: String): Option[TimetableResponse] = {
    val deserializedData = Try(objectMapper.readValue(data, classOf[TimetableResponseWrapper]))
    deserializedData match {
      case Success(result) => {
        if(result.isSessionTimeout){
          None
        }else{
          Some(result.result)
        }
      }
      case Failure(e) => {
        Logger.error(s"Failed to parse Json: ${e.getMessage}")
        e.printStackTrace()
        None
      }
    }
  }

  def mergedTimetablePeriodToJson(data: MergedTimetablePeriod): String = {
    objectMapper.writeValueAsString(data)
  }

  def jsonToMergedTimetablePeriod(json: String): Option[MergedTimetablePeriod] = {
    val data = Try(objectMapper.readValue[MergedTimetablePeriod](json))
    data match {
      case Success(mergedData) => Some(mergedData)
      case Failure(e) => {
        Logger.error(s"Failed to parse Json: ${e.getMessage}")
        e.printStackTrace()
        None
      }
    }
  }


  def getMobileResponse[A](userId: UUID, data: A): JsValue = {
    Try(objectMapper.writeValueAsString(GenericResponse(userId, data))) match {
      case Success(d) => {
        Json.parse(d)
      }
      case Failure(e) => {
        Logger.warn("Not able to create response")
        Json.arr(Json.obj("error" -> "true"))
      }
    }
  }

}
