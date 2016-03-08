package org.victorp.akka.samples.mergesort

import akka.actor.Actor

/**
 * @author victorp
 */
class MergeSortActor extends Actor{
  override def receive: Receive = {
    case sortIt:SortItPlease => {
      sortIt.source ! SortResult(sortIt.notSorted.sorted)
    }
  }
}
