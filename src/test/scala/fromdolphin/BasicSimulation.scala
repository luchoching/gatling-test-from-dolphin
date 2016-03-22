package fromdolphin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

//  object Init {
//    val init = exec(http("request_1")
//      .get("/"))
//      .pause(7)
//  }

  object WsInit {
    val wsInit =
      exec(ws("Conn WS").open("/"))
      .pause(5)

      .exec(ws("Create User Session")
      .sendText(ReqMsgs.getCreateSessionMsg.toString()))
      .pause(10)

      .exec(ws("Ping")
      .sendText(ReqMsgs.getPingMsg.toString()))
      .pause(10)

      .exec(ws("Close WS").close)
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


  val scn = scenario("Test Chat from Dolphin").exec(WsInit.wsInit)

  // Single user
  //setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))

  setUp(scn.inject(rampUsers(10) over (20 seconds)).protocols(httpConf))
}
