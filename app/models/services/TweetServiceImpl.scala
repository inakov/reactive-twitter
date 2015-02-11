package models.services

import com.google.inject.Inject
import core.db.DBQueryBuilder
import models.dtos.{Author, TweetDto}
import models.{User, Tweet}
import models.commands.CreateTweetCommand
import models.daos.{UserDAO, TweetDAO}
import org.joda.time.DateTime
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by inakov on 18.01.15.
 */
class TweetServiceImpl @Inject() (tweetDao: TweetDAO, userDao: UserDAO) extends TweetService{

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
    Tweet(Some(BSONObjectID.generate), user.identify,
      tweetCommand.content, tweetCommand.location, extractHashtags(tweetCommand.content), DateTime.now)
  }

  override def countTweets(userId: String): Future[Int] = {
    tweetDao.count(Json.obj("authorId" -> userId))
  }

  override def tweets(): Future[List[TweetDto]] = {
    createTweetDtos(tweetDao.find())
  }

  override def tweets(authorId: String): Future[List[TweetDto]] = {
    createTweetDtos(tweetDao.find(Json.obj("authorId" -> authorId)))
  }

  override def tweetsForFollower(user: User): Future[List[TweetDto]] = {
    createTweetDtos(tweetDao.find(DBQueryBuilder.in("authorId", user.following.toSeq)))
  }

  private def createTweetDtos(tweets: Future[List[Tweet]]): Future[List[TweetDto]] = {
    for{
      tweetList <- tweets
      users <- userDao.findUsersByIds(tweetList.map(_.authorId))
    } yield {
      tweetList.map{ tweet =>
        val author = users.find(_.identify == tweet.authorId).map(author =>
          Author(author.identify,
            author.avatarURL.get,
            author.name,
            author.username,
            author.verified.getOrElse(false)))
        TweetDto(tweet.identify,
          tweet.content,
          tweet.location,
          tweet.hashtags,
          tweet.created,
          author.get)
      }
    }

  }
}
