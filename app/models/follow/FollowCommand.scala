package models.follow

import play.api.libs.json.Json

/**
 * Created by inakov on 17.01.15.
 */
case class FollowCommand(follow: String)

object FollowCommand{
  implicit val followCommandFormat = Json.format[FollowCommand]
}
