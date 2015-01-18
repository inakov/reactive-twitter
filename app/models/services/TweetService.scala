package models.services

import models.{User, Tweet}
import models.commands.CreateTweetCommand

import scala.concurrent.Future

/**
 * Created by inakov on 17.01.15.
 */
trait TweetService {

  def createTweet(tweetCommand: CreateTweetCommand, user: User): Tweet
  def save(tweet: Tweet): Future[Tweet]

}
