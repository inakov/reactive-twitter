package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import models.User
import models.dtos.UserSuggestion
import models.services.{TweetService, UserService}
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by inakov on 15.01.15.
 */
class DiscoverController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator],
                                    val userService: UserService, val tweetService: TweetService)
  extends Silhouette[User, CachedCookieAuthenticator] {

  def discover = SecuredAction.async{ implicit request =>{
    userService.discoverUser(request.identity.identify).map{
        users => Ok(views.html.discover(users, request.identity))
      }
    }
  }

  def discoverTweets = SecuredAction.async{ implicit request => {
    tweetService.tweets().map(tweets => Ok(Json.toJson(tweets)))
    }
  }

  def discoverUsers = SecuredAction.async{ implicit request =>{
    userService.discoverUser(request.identity.identify).map{ users =>
      val currentUser = request.identity
      val userSuggestions:List[UserSuggestion] = users.map{ suggestedUser =>
        UserSuggestion(suggestedUser.identify,
          suggestedUser.avatarURL,
          suggestedUser.name,
          suggestedUser.username,
          suggestedUser.verified,
          suggestedUser.biography,
          currentUser.following.contains(suggestedUser.identify))
      }
      Ok(Json.toJson(userSuggestions))
    }
  }
  }

}
