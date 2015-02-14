package models.tweet

import models.tweet.{CreateTweetCommand, TweetDto}
import models.user.User

import scala.concurrent.Future

/**
 * Created by inakov on 17.01.15.
 */
trait TweetService {

  def createTweet(tweetCommand: CreateTweetCommand, user: User): Tweet

  def save(tweet: Tweet): Future[Tweet]

  def countTweets(userId: String) : Future[Int]

  def tweets(): Future[List[TweetDto]]

  def tweetsWithHashtags(hashtags: Set[String]): Future[List[TweetDto]]

  def tweets(username: String): Future[List[TweetDto]]

  def tweetsForFollower(user: User): Future[List[TweetDto]]

}
