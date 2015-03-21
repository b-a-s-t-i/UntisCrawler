package controllers

import play.api.Logger
import provider.UserProvider
import scaldi.{Injector, Injectable}

import play.api.mvc._

class HomeController(implicit inj: Injector) extends Controller with Injectable with Secured{

  override val userProvider: UserProvider = inject[UserProvider]

  def index = Action { implicit request =>
    val start = System.currentTimeMillis()
    verifyGoogleAuth("eyJhbGciOiJSUzI1NiIsImtpZCI6IjRhMzE4ZmJiNzFhYTlhYWY5OTU2OWZmYTdkZmI5MmVhOTcyN2M1YjIifQ.eyJpc3MiOiJhY2NvdW50cy5nb29nbGUuY29tIiwic3ViIjoiMTA5NTMzMjYyNzg2MTA1NjY5MTc2IiwiYXpwIjoiODcwNDcwMzE2Mjg2LTJvcTUwNWw2MDRuc3YyNm9zcDRmaTA1bXZoc3RsZ2NmLmFwcHMuZ29vZ2xldXNlcmNvbnRlbnQuY29tIiwiZW1haWwiOiJzZWJhc3RpYW4uY2hsYW5AZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF1ZCI6Ijg3MDQ3MDMxNjI4Ni1tYWE4M2lsYTNyMmhucWtwcmp0aWxtYWpzamI1M28wZy5hcHBzLmdvb2dsZXVzZXJjb250ZW50LmNvbSIsImlhdCI6MTQyNjk3NTIxNCwiZXhwIjoxNDI2OTc4OTk0fQ.wE-ct-N6PbVlWUtqP9rx-DkvHddac-aF5wfzzlpXV3PSnEKc8D3oRTDU4sidoqCRcpdhEit8j_vcmSegZrBD2MFiYuWFIhaudRJTAK1D5gDzushCfotE3BTun6XcZuuZAWz6vqy63AEmGPxNu0IZQESB55P72mBk3nlJo8a6VZg")
    Logger.info(s"${System.currentTimeMillis() - start}")

    Ok("hi")
  }

}
