package org.victorp.akka.samples.distpubsub

import akka.actor.Actor
import akka.cluster.pubsub.{DistributedPubSub, DistributedPubSubMediator}
import DistributedPubSubMediator.Publish

/**
 * @author victorp
 */
class Publisher extends Actor {


  // activate the extension
  val mediator = DistributedPubSub(context.system).mediator

  def receive = {
    case in: String =>
      val out = in.toUpperCase
      mediator ! Publish("content", out)
  }
}