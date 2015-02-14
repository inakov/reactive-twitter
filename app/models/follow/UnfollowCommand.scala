package models.follow

import play.api.libs.json.Json

/**
 * Created by inakov on 17.01.15.
 */
case class UnfollowCommand(unfollow: String)

object UnfollowCommand{
  implicit val unfollowCommandFormat = Json.format[UnfollowCommand]
}