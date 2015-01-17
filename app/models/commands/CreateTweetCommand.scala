package models.commands

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
/**
 * Created by inakov on 17.01.15.
 */
case class CreateTweetCommand(content: String,
                              location: Option[String])

object CreateTweetCommand{
  implicit val createTweetCommandReads: Reads[CreateTweetCommand] = (
    (JsPath \ "content").read[String](minLength[String](1) keepAnd maxLength[String](140)) and
    (JsPath \ "location").readNullable[String]
    )(CreateTweetCommand.apply _)
}
