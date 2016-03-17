package org.victorp.akka.samples.distpubsub

import akka.actor.{Actor, ActorLogging}
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}

/**
 * @author victorp
 */
class Subscriber extends Actor with ActorLogging {

  import DistributedPubSubMediator.{Subscribe, SubscribeAck}

  val mediator = DistributedPubSub(context.system).mediator
  // subscribe to the topic named "content"
  mediator ! Subscribe("content", self)

  def receive = {
    case s: String =>
      log.info("Got {}", s)
    case SubscribeAck(Subscribe("content", None, `self`)) =>
      log.info("subscribing");
  }
}