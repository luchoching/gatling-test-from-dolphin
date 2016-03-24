package fromdolphin

import java.io.{File, PrintWriter}

import play.api.libs.json.Json

import scalaj.http._

object Tokens {

  def main(args: Array[String]): Unit = {

    val nums = 100 to 999 toList

    val users = nums map {num => "testfriend" + num.toString}
    val userLines = "user" :: users

    val tokens = users map getAuth
    val tokenLines = "token" :: tokens

    // for private chat GET_MESSAGES
    save(userLines, "users.csv")

    // for CREATE_SESSION
    save(tokenLines, "tokens.csv")
  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  def save(source: List[Any], fileName: String) = {
    printToFile(new File(fileName)) { p =>
      source foreach {line =>
        if(!"".equals(line)) {
          println(line)
          p.println(line)
        }
      }
    }
  }

  def getAuth(userName: String) = {
    val request: HttpRequest = Http("http://localhost:8888/oauth/token")
      .postForm(Seq(
        "username" -> userName,
        "password" -> "abc12345",
        "grant_type" -> "password",
        "client_id" -> "2975114b9ffb4c25bb9da75963f1b8c1"))

    request.asString.code match {
      case 200 =>
        ( Json.parse(request.asString.body) \ "access_token").asOpt[String] getOrElse None
      case _ => ""
    }
  }
}

