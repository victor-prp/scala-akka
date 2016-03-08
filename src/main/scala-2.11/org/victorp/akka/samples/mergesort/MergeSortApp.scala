package org.victorp.akka.samples.mergesort

import akka.actor.{Props, ActorSystem}

/**
 * @author victorp
 */
object MergeSortApp extends App{

  // Create an Akka system
  val system = ActorSystem("MergeSortSystem")

  // create actors
  val mergeSortActor = system.actorOf(Props[MergeSortActor], name = "mergeSortActor")
  val userFacadeActor = system.actorOf(Props[UserFacadeActor], name = "userFacadeActor")

  mergeSortActor ! SortItPlease(List(2,4,1,89,432,2,1),userFacadeActor)

}
