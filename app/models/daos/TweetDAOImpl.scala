package models.daos

import core.dao.DocumentDao
import models.Tweet

import scala.concurrent.Future

/**
 * Created by inakov on 14.01.15.
 */
class TweetDAOImpl extends TweetDAO with DocumentDao[Tweet]{

  override val collectionName: String = "tweets"
  override def ensureIndexes: Future[List[Boolean]] = Future.successful(Nil)
}
