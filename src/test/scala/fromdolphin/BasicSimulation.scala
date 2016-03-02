package fromdolphin

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  object Init {
    val init = exec(http("request_1")
      .get("/"))
      .pause(7)
  }

  val httpConf = http
    .baseURL("http://localhost:3000")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .wsBaseURL("ws://localhost:8000")

  val scn = scenario("Scenario Name").exec(Init.init)

  setUp(scn.inject(atOnceUsers(1)).protocols(httpConf))

}