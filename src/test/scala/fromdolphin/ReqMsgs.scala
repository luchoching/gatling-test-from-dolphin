package fromdolphin

import play.api.libs.json.{JsValue, Json}

object ReqMsgs {
  
  //Todo: need get tid?
  var transactionId = 1

  def createSessionMsg(accessToken: String) = Json.obj(
    "2" -> 5,                 // client_type or client_version
    "5" -> 5,                 // client_type or client_version
    "3" -> "",                // session_id
    "21" ->  accessToken,     // access_token
    "10" -> 16,               // font_height
    "19" -> 200,              // sticker_pixel_size
    "18" -> 64                // virtual_gift_pixel_size
  )

  def getReqMsg(packetType: Int, fields: JsValue = Json.obj()) = {
    transactionId += 1
    Json.obj(
      "F" -> fields,
      "I" -> transactionId,
      "T" -> packetType
    )
  }

  def getCreateSessionMsg(accessToken: String) = getReqMsg(211, createSessionMsg(accessToken))

  def getPingMsg = getReqMsg(2)
}