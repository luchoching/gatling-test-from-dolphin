package fromdolphin

import java.io.{File, PrintWriter}

import play.api.libs.json.Json

import scala.collection.mutable.ListBuffer
import scala.util.Random
import scalaj.http._

object Tokens {

  def main(args: Array[String]): Unit = {

    val nums = 100 to 300 toList

    val users = nums map {num => "testfriend" + num.toString}

    // for private chat destination
    var destinations = new ListBuffer[String]()
    for(i <- users.indices) { destinations += users(Random.nextInt(users.size)) }

    val tokens = users map getAuth

    // filter source == destination
    val lines = tokens zip destinations filter{x  =>
      val hasToken = ! "".equals(x._1)
      if (hasToken) {
        val source = x._1.split(",")(0)
        val destination = x._2
        ! source.equals(destination)
      } else false
    }

    val talkLines = ("source,token", "destination") :: lines

    save(talkLines, "talks.csv")

  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }


  def save(lines: List[(String, String)], fileName: String) = {
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
        "client_id" -> "2975114b9ffb4c25bb9da75963f1b8c1",
        "scope" -> "chat"))

    request.asString.code match {
      case 200 =>
        val token = ( Json.parse(request.asString.body) \ "access_token").asOpt[String] getOrElse None
        //println(token)
        userName+","+token
      case _ => ""
    }
  }
}

