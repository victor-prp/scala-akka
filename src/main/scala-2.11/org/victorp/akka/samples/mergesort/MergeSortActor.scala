package org.victorp.akka.samples.mergesort


import akka.actor.{ActorRef, Props, Actor}
import akka.event.{Logging, LoggingAdapter}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer


/**
 * @author victorp
 */
class MergeSortActor(val depth: Int) extends Actor {

  var leftActor:Option[ActorRef] = None
  var rightActor:Option[ActorRef] = None


  var source: Option[ActorRef] = None
  var state = SortState.NOT_STARTED
  var currentSide = Side.NONE
  var sortedLeft = List[Int]()
  var sortedRight = List[Int]()

  val log: LoggingAdapter = Logging.getLogger(context.system, self)

  log.info(s"${self.path.toString} is started")

  override def receive: Receive = {
    case sortIt: SortItPlease if depth > 0 => {
      leftActor = Some(context.actorOf(Props(new MergeSortActor(depth - 1)), name = s"mergeSortLeft-${depth - 1}"))
      rightActor = Some(context.actorOf(Props(new MergeSortActor(depth - 1)), name = s"mergeSortRight-${depth - 1}"))

      state = SortState.STARTED
      currentSide = sortIt.side
      source = Some(sortIt.source)
      val input = sortIt.notSorted
      val (left, right) = input.splitAt(input.size)
      leftActor.get ! SortItPlease(left, self, Side.LEFT)
      rightActor.get ! SortItPlease(right, self, Side.RIGHT)
    }

    case sortIt: SortItPlease if depth == 0 => {
      sortIt.source ! SortResult(sortIt.notSorted.sorted, sortIt.side)
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


  def partialResultReceived() = {
    if (state eq SortState.PARTIALLY_COMPLETED) {
      source.get ! SortResult(SortUtil.merge(sortedLeft, sortedRight), currentSide)
      state = SortState.FULLY_COMPLETED
    } else {
      state = SortState.PARTIALLY_COMPLETED
    }
  }

}

object SortUtil {


  def merge(left: List[Int], right: List[Int]):List[Int] = {
    val result = ListBuffer[Int]()
    merge(left,right,result)
    result.toList
  }

  @tailrec
    def merge(left: List[Int], right: List[Int], result:ListBuffer[Int]): Unit = (left, right) match {
      case (List(), r ) => result.prependAll(r)
      case (l, List()) => result.prependAll(l)
      case (leftHead :: leftTail, rightHead :: rightTail) if leftHead < rightHead =>  {
        result.prepend(leftHead)
        merge(leftTail, right, result)
      }
      case (leftHead :: leftTail, rightHead :: rightTail) if leftHead >= rightHead => {
        result.prepend(rightHead)
        merge(left, rightTail, result)
      }
      case _ => println("Unexpected case")
    }

}

object SortState extends Enumeration {
  val NOT_STARTED, STARTED, PARTIALLY_COMPLETED, FULLY_COMPLETED = Value
}

object Side extends Enumeration {
  val LEFT, RIGHT, NONE = Value
}
