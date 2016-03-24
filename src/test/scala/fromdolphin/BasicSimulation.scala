package fromdolphin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  object OnlyOnePrivateChat {
    def conWs() = exec(ws("Conn WS").open("/"))

    def closeWs() = exec(ws("Close WS").close)

    def createSession() = exec(ws("Create testfriend888 Session")
      .sendText(ReqMsgs.createSession("2fa78f7f3a844c2dbf401814390f6dec83cde93182dc4afeb3709a5a643735aa").toString()))
      .pause(10)

    def getMsgs() = {
      exec(ws("Start Private Chat(get messages)")
        .sendText(ReqMsgs.createPrivateChat("testfriend300").toString()))
    }

    def sendMsg = exec(ws("Send Private Message")
      .sendText(ReqMsgs.privateMsg("testfriend888","testfriend300").toString()))

    def repeatSendMsg() = repeat(5) { sendMsg.pause(5) }

    val init = exec(conWs(), createSession(), getMsgs(), repeatSendMsg(), closeWs())
  }


  object PrivateChat {

    val tokens = csv("tokens.csv").random
    val users = csv("users.csv").random

    def conWs() = exec(ws("Conn WS").open("/"))

    def closeWs() = exec(ws("Close WS").close)

    def createSession() = feed(tokens)
      .exec(ws("Create User Segssion")
      .sendText(ReqMsgs.createSession("${token}").toString()))
      .pause(10)

    def getMsgs() = {
      feed(users)
      .exec(ws("Start Private Chat(get messages)")
      .sendText(ReqMsgs.createPrivateChat("${user}").toString()))
      .pause(10)
  }

    def repeatSendMsg = repeat(5) { sendMsg }

    def sendMsg() = exec(ws("Send Private Message")
      .sendText(ReqMsgs.privateMsg("${user}","${user}").toString()))
      .pause(10)

    val init = exec(conWs(), createSession(), getMsgs(), sendMsg(), closeWs())
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

  setUp(scn2.inject(
    atOnceUsers(1) //Injects a given number of users at once.
    //rampUsers(1000) over (60 seconds) //  Injects a given number of users with a linear ramp over a given duration.
  ).protocols(httpConf))
}
