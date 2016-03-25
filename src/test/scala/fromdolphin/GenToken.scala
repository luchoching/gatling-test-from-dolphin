package fromdolphin

import java.io.{File, PrintWriter}

import play.api.libs.json.Json

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scalaj.http._

case class Talk(source: String, destination: String, token: String)

object Tokens {

  def main(args: Array[String]): Unit = {

    val nums = 100 to 105 toList

    val users = nums map {num => "testfriend" + num.toString}
    val userLines = "user" :: users

    // for private chat destination
    var destinations = new ListBuffer[String]()
    for(i <- users.indices) { destinations += users(Random.nextInt(users.size)) }

    val tokens = users map getAuth

    val lines = tokens zip destinations filter(x  => ! "".equals(x._1))
    //println(lines)

    val talkLines = ("source,token", "destination") :: lines

    save(userLines, "users.csv")

    saveTalk(talkLines, "talks.csv")

  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }

  def save(source: List[Any], fileName: String) = {
    printToFile(new File(fileName)) { p =>
      source foreach {line =>
        if(!"".equals(line)) {
          p.println(line)
        }
      }
    }
  }

  def saveTalk(lines: List[(String, String)], fileName: String) = {
    printToFile(new File(fileName)) { p =>
      lines foreach { line =>
        p.println(line._1+","+line._2)
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
        val token = ( Json.parse(request.asString.body) \ "access_token").asOpt[String] getOrElse None
        //println(token)
        userName+","+token
      case _ => ""
    }
  }
}

