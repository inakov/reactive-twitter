package models.tweet

import core.models.IdentifiableModel
import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID

/**
 * Created by inakov on 14.01.15.
 */
case class Tweet(
                  override var _id: Option[BSONObjectID],
                  authorId: String,
                  content: String,
                  location: Option[String],
                  hashtags: Set[String],
                  created: DateTime
                  ) extends IdentifiableModel
object Tweet{
  import play.api.libs.json._
  import play.modules.reactivemongo.json.BSONFormats._
  implicit val tweetFormat = Json.format[Tweet]
}
