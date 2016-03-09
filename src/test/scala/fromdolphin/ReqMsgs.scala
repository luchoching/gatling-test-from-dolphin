package fromdolphin

import play.api.libs.json.{JsValue, Json}

object ReqMsgs {

  val accessToken = "0999c08a7b7c467f999724ce6f16bd37a9d1d72d994d4549bafc0e96dddcfda8"

  var transactionId = 1

  val createSessionMsg = Json.obj(
    "client_type" -> 5,
    "client_version" -> 5,
    "session_id" -> "",
    "access_token" ->  accessToken,
    "font_height" -> 16,
    "sticker_pixel_size" -> 200,
    "virtual_gift_pixel_size" -> 64
  )

  def getReqMsg(packetType: Int, fields: JsValue = Json.obj()) = {
    transactionId += 1
    Json.obj(
      "createTime" -> java.lang.System.currentTimeMillis(),
      "fields" -> fields,
      "retryCount" -> 0,
      "sentTime" -> java.lang.System.currentTimeMillis(),
      "transactionId" -> transactionId,
      "type" -> packetType
    )
  }

  def getCreateSessionMsg = getReqMsg(211, createSessionMsg)

  def getPingMsg = getReqMsg(2)
}