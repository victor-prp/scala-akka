package org.victorp.akka.samples.mergesort

import akka.actor.{Props, ActorSystem}

import scala.util.Random

/**
 * @author victorp
 */
object MergeSortApp extends App{

  // Create an Akka system
  val system = ActorSystem("MergeSortSystem")

  // create actors
  val mergeSortActor = system.actorOf(Props(new MergeSortActor(3)), name = "mergeSortActor")
  val userFacadeActor = system.actorOf(Props(new UserFacadeActor()), name = "userFacadeActor")


  val bigList =
    for {
      i <- 1 to 10000
    } yield Random.nextInt()

  println("start sorting now")
  mergeSortActor ! SortItPlease(bigList.toList,userFacadeActor)

}
