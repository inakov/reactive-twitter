package models.services

import models.dtos.TweetDto
import models.{User, Tweet}
import models.commands.CreateTweetCommand

import scala.concurrent.Future

/**
 * Created by inakov on 17.01.15.
 */
trait TweetService {

  def createTweet(tweetCommand: CreateTweetCommand, user: User): Tweet

  def save(tweet: Tweet): Future[Tweet]

  def countTweets(userId: String) : Future[Int]

  def tweets(): Future[List[TweetDto]]

  def tweets(username: String): Future[List[TweetDto]]

  def tweetsForFollower(user: User): Future[List[TweetDto]]

}
