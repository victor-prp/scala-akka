package org.victorp.akka.samples.mergesort

import akka.actor.Actor

/**
 * @author victorp
 */
class UserFacadeActor extends Actor{
  override def receive: Receive = {
    case result:SortResult =>
      println(result)
      System.exit(0)
  }
}
