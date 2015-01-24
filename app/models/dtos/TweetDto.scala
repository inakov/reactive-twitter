package models.dtos

import org.joda.time.DateTime
import play.api.libs.json.Json

/**
 * Created by inakov on 1/24/15.
 */
case class TweetDto(id: String,
                    content: String,
                    location: Option[String],
                    hashtags: Set[String],
                    created: DateTime,
                    author: Author)

case class Author(id: String,
                  avatarURL: String,
                  name: Option[String],
                  username: String,
                  verified: Boolean)

object TweetDto{
  implicit val authorFormat = Json.format[Author]
  implicit val tweetDtoFormat = Json.format[TweetDto]
}
