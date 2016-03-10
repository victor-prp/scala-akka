package org.victorp.akka.samples.mergesort

import java.text.SimpleDateFormat
import java.util.GregorianCalendar

import akka.actor.Actor

/**
 * @author victorp
 */
class UserFacadeActor extends Actor{
  val fmt = new SimpleDateFormat("ss-mm-hh")

  def isSorted(sorted: List[Int]):Boolean = {
    var result = true
    sorted.fold(Integer.MIN_VALUE){
      (prev,current) => {
        if (prev > current) result = false
        current
      }
    }
    result
  }

  override def receive: Receive = {
    case result:SortResult =>
      println(s"""sort completed ${fmt.format(new GregorianCalendar().getTime)} """)

      println(s"result is ${isSorted(result.sorted)}, size: ${result.sorted.size}")
      System.exit(0)
  }
}
