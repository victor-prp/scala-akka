package org.victorp.akka.samples.distpubsub

import akka.actor.Actor
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}

/**
 * @author victorp
 */
class Publisher extends Actor {

  import DistributedPubSubMediator.Publish

  // activate the extension
  val mediator = DistributedPubSub(context.system).mediator

  def receive = {
    case in: String =>
      val out = in.toUpperCase
      mediator ! Publish("content", out)
  }
}