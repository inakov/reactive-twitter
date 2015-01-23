package controllers

import javax.inject.Inject

import com.mohiva.play.silhouette.contrib.services.CachedCookieAuthenticator
import com.mohiva.play.silhouette.core.{Silhouette, Environment}
import models.commands.{CreateTweetCommand}
import models.{Tweet, User}
import models.services.{TweetService}
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
 * Created by inakov on 17.01.15.
 */
class TweetController @Inject() (implicit val env: Environment[User, CachedCookieAuthenticator],
                                 val tweetService: TweetService)
  extends Silhouette[User, CachedCookieAuthenticator] {

  def createTweet = SecuredAction.async(parse.json){implicit request => {
      request.body.validate[CreateTweetCommand].asOpt match{
        case Some(tweetCommand) => {
          val tweet: Tweet = tweetService.createTweet(tweetCommand, request.identity)
          tweetService.save(tweet).map(tweet => Ok(Json.toJson(tweet)))
        }
        case None => Future.successful(BadRequest)
      }
    }
  }

  def loadUserTweets(username: String) = SecuredAction.async{
    tweetService.tweets(username).map{ tweets =>
      Ok(Json.toJson(tweets))
    }
  }

  def loadNewsfeed = SecuredAction.async{ implicit request =>{
      tweetService.tweetsForFollower(request.identity).map(tweets => Ok(Json.toJson(tweets)))
    }
  }

}
