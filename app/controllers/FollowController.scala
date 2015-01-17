package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import models.User
import models.commands.{UnfollowCommand, FollowCommand}
import models.dtos.FollowResponse
import models.services.UserService
import play.api.libs.json.Json

import scala.concurrent.Future

/**
 * Created by inakov on 17.01.15.
 */
class FollowController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator],
                                  val userService: UserService)
  extends Silhouette[User, CachedCookieAuthenticator] {


  def follow = SecuredAction.async(parse.json){ implicit request => {
      request.body.validate[FollowCommand].asOpt match{
        case Some(followCommand) =>
          userService.follow(followCommand.follow, request.identity.identify);
          Future.successful(Ok(Json.toJson(FollowResponse(followed = Some(true)))))
        case None => Future.successful(BadRequest)
      }
    }
  }

  def unfollow = SecuredAction.async(parse.json){ implicit request => {
      request.body.validate[UnfollowCommand].asOpt match {
        case Some(unfollowCommand) =>
          userService.unfollow(unfollowCommand.unfollow, request.identity.identify);
          Future.successful(Ok(Json.toJson(FollowResponse(unfollowed = Some(true)))))
        case None => Future.successful(BadRequest)
      }
    }
  }
}
