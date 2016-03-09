package org.victorp.akka.samples.mergesort


import akka.actor.{ActorRef, Props, Actor}
import org.victorp.akka.samples.mergesort.SortResult

import scala.annotation.tailrec

/**
 * @author victorp
 */
class MergeSortActor(val depth: Int) extends Actor {

  val leftActor = context.actorOf(Props(new MergeSortActor(depth - 1)), name = s"mergeSortLeft-${depth - 1}")
  val rightActor = context.actorOf(Props(new MergeSortActor(depth - 1)), name = s"mergeSortRight-${depth - 1}")


  var source: Option[ActorRef] = None
  var state = SortState.NOT_STARTED
  var currentSide = Side.NONE
  var sortedLeft = List[Int]()
  var sortedRight = List[Int]()

  override def receive: Receive = {
    case sortIt: SortItPlease if depth > 0 => {
      state = SortState.STARTED
      currentSide = sortIt.side
      source = Some(sortIt.source)
      val input = sortIt.notSorted
      val (left, right) = input.splitAt(input.size)
      leftActor ! SortItPlease(left, self, Side.LEFT)
      rightActor ! SortItPlease(right, self, Side.RIGHT)
    }

    case sortIt: SortItPlease if depth == 0 => {
      sortIt.source ! SortResult(sortIt.notSorted.sorted,sortIt.side)
    }


    case leftResult: SortResult if leftResult.side eq Side.LEFT => {
      sortedLeft = leftResult.sorted
      partialResultReceived()
    }

    case rightResult: SortResult if rightResult.side eq Side.RIGHT => {
      sortedRight = rightResult.sorted
      partialResultReceived()
    }
  }

  private def merge(left: List[Int], right: List[Int]): List[Int] = (left, right) match {
    case (List(), r ) => r
    case (l, List()) => l
    case (leftHead :: leftTail, rightHead :: rightTail) if leftHead < rightHead => leftHead :: merge(leftTail, right)
    case (leftHead :: leftTail, rightHead :: rightTail) if leftHead >= rightHead => rightHead :: merge(left, rightTail)
  }


  def partialResultReceived() = {
    if (state eq SortState.PARTIALLY_COMPLETED) {
      source.get ! SortResult(merge(sortedLeft, sortedRight),currentSide)
      state = SortState.FULLY_COMPLETED
    } else {
      state = SortState.PARTIALLY_COMPLETED
    }
  }

}


object SortState extends Enumeration {
  val NOT_STARTED, STARTED, PARTIALLY_COMPLETED, FULLY_COMPLETED = Value
}

object Side extends Enumeration {
  val LEFT, RIGHT, NONE = Value
}
