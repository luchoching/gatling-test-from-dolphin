package fromdolphin

import java.io.{File, PrintWriter}

import play.api.libs.json.Json

import scalaj.http._

object Tokens {

  def main(args: Array[String]): Unit = {

    val nums = 700 to 800 toList
    // val users = nums map {num => "testfriend" + num.toString}

    val tokens = nums map {num => "testfriend" + num.toString} map getAuth

    def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
      val p = new java.io.PrintWriter(f)
      try { op(p) } finally { p.close() }
    }

    printToFile(new File("tokens.txt")) { p =>
      tokens foreach {token =>
        if(!"".equals(token)) p.println(token)
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

