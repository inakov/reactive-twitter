package models.follow

import play.api.libs.json.Json

/**
 * Created by inakov on 17.01.15.
 */
case class FollowResponse(followed: Option[Boolean] = None, unfollowed: Option[Boolean] = None)

object FollowResponse{
  implicit val followResponseFormat = Json.format[FollowResponse]
}
