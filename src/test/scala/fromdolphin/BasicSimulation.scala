package fromdolphin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  object OnlyOnePrivateChat {
    def conWs() = exec(ws("Conn WS").open("/"))

    def closeWs() = exec(ws("Close WS").close)

    def createSession() = exec(ws("Create testfriend887 Session")
      .sendText(ReqMsgs.createSession("1076e9f0df07423c973f1c7be3f4046536b7af68b6c2469e96a45c2ce45e2624").toString()))
      .pause(10)

    def getMsgs() = {
      exec(ws("Start Private Chat(get messages)")
        .sendText(ReqMsgs.createPrivateChat("testfriend889").toString()))
    }

    def sendMsg = exec(ws("Send Private Message")
      .sendText(ReqMsgs.privateMsg("testfriend887","testfriend889").toString()))

    def repeatSendMsg() = repeat(5) { sendMsg.pause(5) }

    val init = exec(conWs(), createSession(), getMsgs(), repeatSendMsg(), closeWs())
  }

  object PrivateChat {
    val talks = csv("talks.csv").random

    def conWs() = exec(ws("Conn WS").open("/"))

    def closeWs() = exec(ws("Close WS").close)

    def createSession() = feed(talks)
      .exec(ws("Create Session")
      .sendText(ReqMsgs.createSession("${token}").toString()))
      .pause(10)

    def getMsgs() =
      exec(ws("Start Private Chat(get messages)")
      .sendText(ReqMsgs.createPrivateChat("${destination}").toString()))

    def sendMsg =
      exec(ws("Send Private Message")
      .sendText(ReqMsgs.privateMsg("${source}","${destination}").toString()))

    def repeatSendMsg() = repeat(5) { sendMsg.pause(5) }

    val init = exec(conWs(), createSession(), getMsgs(),  repeatSendMsg(), closeWs())
  }

  val httpConf = http
//    .baseURL("http://localhost:8000")
//    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
//    .doNotTrackHeader("1")
//    .acceptLanguageHeader("en-US,en;q=0.5")
//    .acceptEncodingHeader("gzip, deflate")
//    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .wsBaseURL("ws://localhost:8000")
    .inferHtmlResources(BlackList(""".*\.css""", """.*\.ico"""), WhiteList())


  val scn = scenario("Test Chat (Stargate + Mercury)").exec(PrivateChat.init)
  val scn2 = scenario("Test Chat (Stargate + Mercury)").exec(OnlyOnePrivateChat.init)

  setUp(scn.inject(
    atOnceUsers(5) //Injects a given number of users at once.
    //rampUsers(1000) over (60 seconds) //  Injects a given number of users with a linear ramp over a given duration.
  ).protocols(httpConf))
}
