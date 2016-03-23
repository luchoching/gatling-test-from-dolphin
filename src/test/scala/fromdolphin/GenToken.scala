package fromdolphin

import play.api.libs.json.Json

import scalaj.http._

object Tokens {

  def main(args: Array[String]): Unit = {
     println(getToken("testfriend888"))
  
  }

  def getToken(userName: String) = {
    val request: HttpRequest = Http("http://localhost:8888/oauth/token")
      .postForm(Seq(
        "username" -> userName,
        "password" -> "abc12345",
        "grant_type" -> "password",
        "client_id" -> "2975114b9ffb4c25bb9da75963f1b8c1"))

    ( Json.parse(request.asString.body) \ "access_token").asOpt[String] getOrElse None
  }
}

