package models.tweet

import core.dao.BaseDocumentDao

import scala.concurrent.Future

/**
 * Created by inakov on 14.01.15.
 */
class TweetDAOImplBase extends TweetDAO with BaseDocumentDao[Tweet]{

  override val collectionName: String = "tweets"
  override def ensureIndexes: Future[List[Boolean]] = Future.successful(Nil)
}
