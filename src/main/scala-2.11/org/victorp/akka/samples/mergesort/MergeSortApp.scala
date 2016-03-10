package org.victorp.akka.samples.mergesort

import java.text.SimpleDateFormat
import java.util.{GregorianCalendar}

import akka.actor.{Props, ActorSystem}

import scala.util.Random

/**
 * @author victorp
 */
object MergeSortApp extends App{

  // Create an Akka system
  val system = ActorSystem("MergeSortSystem")

  // create actors
  val mergeSortActor = system.actorOf(Props(new MergeSortActor(10)), name = "mergeSortActor")
  val userFacadeActor = system.actorOf(Props(new UserFacadeActor()), name = "userFacadeActor")

  val bigList =
    for {
      i <- 1 to 100000
    } yield Random.nextInt()


  val fmt = new SimpleDateFormat("ss-mm-hh")
  println(s"""start sorting now ${fmt.format(new GregorianCalendar().getTime)} """)
  mergeSortActor ! SortItPlease(bigList.toList,userFacadeActor)

}
