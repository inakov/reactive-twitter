package models.services

import com.google.inject.Inject
import models.{User, Tweet}
import models.commands.CreateTweetCommand
import models.daos.TweetDAO
import org.joda.time.DateTime
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by inakov on 18.01.15.
 */
class TweetServiceImpl @Inject() (tweetDao: TweetDAO) extends TweetService{

  val hashtagsRegex = """(?<=^|(?<=[^a-zA-Z0-9-\.]))#([A-Za-z]+[A-Za-z0-9]+)""".r

  private def extractHashtags(content: String): Set[String] ={
    val result = for(matched <- hashtagsRegex.findAllMatchIn(content)) yield matched.group(1)
    result.toSet
  }

  override def save(tweet: Tweet): Future[Tweet] = {
    tweetDao.insert(tweet).map{
      result => result match{
        case Right(document) => document
        case Left(ex) => throw ex
      }
    }
  }

  def createTweet(tweetCommand: CreateTweetCommand, user: User): Tweet = {
    Tweet(Some(BSONObjectID.generate), user.username, user.identify,
      tweetCommand.content, tweetCommand.location, extractHashtags(tweetCommand.content), DateTime.now)
  }
}
