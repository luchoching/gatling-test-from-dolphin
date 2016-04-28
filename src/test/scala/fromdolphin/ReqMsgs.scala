package fromdolphin

import play.api.libs.json.{JsValue, Json}

object ReqMsgs {

  //Todo: need get tid?
  var transactionId = 1

  // json zip mapping: ferry/src/packets/packet-field.js

  // CREATE_SESSION
  def session(accessToken: String) = Json.obj(
    "2" -> 5,                 // client_type or client_version
    "5" -> 5,                 // client_type or client_version
    "3" -> "",                // session_id
    "21" ->  accessToken,     // access_token
    "10" -> 16,               // font_height
    "19" -> 200,              // sticker_pixel_size
    "18" -> 64                // virtual_gift_pixel_size
  )

  def request(packetType: Int, fields: JsValue = Json.obj()) = {
    transactionId += 1
    Json.obj(
      "F" -> fields,
      "I" -> transactionId,
      "T" -> packetType
    )
  }

  // GET_CHATS
  def ChatsMessage() = Json.obj(
//    "3" -> "None", // chat_type
//    "2" -> "None", // limit
    "1" -> 0 // version
  )


  //GET_MESSAGES -- create private chat room
  def privateChatMessages(destination: String) = Json.obj(
    "1" -> destination,       //chat_id
    "2" -> 1,                 // chat_type
    "5" -> 30,                 //limit
    "3" -> java.lang.System.currentTimeMillis,   // latest_message_timestamp
    "4" -> 0                 // oldest_message_timestamp
  )

  //MESSAGE (private)
  def message(source: String, destination: String) = Json.obj(
    "1" -> 1,                 // messageType
    "2" -> source,            // source
    "3" -> 1,                 // chatType
    "4" -> destination,       // destination
    "6" -> 1,                 // contentType
    "8" -> "from gatling",    // content
    "16" -> java.lang.System.currentTimeMillis // timestamp
    // "19" -> // previousMessageGuid <-- none
  )


  def createSession(accessToken: String) = request(211, session(accessToken))

  def pingMsg = request(2)

  def createPrivateChat(destination: String) = request(550, privateChatMessages(destination))

  def privateMsg(source: String, destination: String) = request(500, message(source, destination))

  def getChatsMsg = request(551, ChatsMessage())
}