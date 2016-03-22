package fromdolphin

import play.api.libs.json.{JsValue, Json}

object ReqMsgs {

  val accessToken = "d701e45abaea453c8075ff06c6b9593d0fda1eb9a0ee41e4aedad96daf7944e5"

  var transactionId = 1

  val createSessionMsg = Json.obj(
    "2" -> 5,
    "5" -> 5,
    "3" -> "",
    "21" ->  accessToken,
    "10" -> 16,
    "19" -> 200,
    "18" -> 64
  )

  def getReqMsg(packetType: Int, fields: JsValue = Json.obj()) = {
    transactionId += 1
    Json.obj(
      "F" -> fields,
      "I" -> transactionId,
      "T" -> packetType
    )
  }

  def getCreateSessionMsg = getReqMsg(211, createSessionMsg)

  def getPingMsg = getReqMsg(2)
}