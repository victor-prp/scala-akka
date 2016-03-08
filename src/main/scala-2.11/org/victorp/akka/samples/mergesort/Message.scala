package org.victorp.akka.samples.mergesort

import akka.actor.ActorRef

/**
 * @author victorp
 */
case class SortItPlease(notSorted:List[Int], source:ActorRef )
case class SortResult(sorted:List[Int])
