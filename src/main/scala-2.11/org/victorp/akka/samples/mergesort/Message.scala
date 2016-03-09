package org.victorp.akka.samples.mergesort

import akka.actor.ActorRef

/**
 * @author victorp
 */
case class SortItPlease(notSorted:List[Int], source:ActorRef, side:Side.Value = Side.NONE )
case class SortResult(sorted:List[Int], side:Side.Value)
