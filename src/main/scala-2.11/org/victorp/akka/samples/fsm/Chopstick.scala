package org.victorp.akka.samples.fsm

import akka.actor.{ActorRef, Actor, FSM}


/**
 * @author victorp
 */


sealed trait ChopstickMessage
object Take extends ChopstickMessage
object Put extends ChopstickMessage
final case class Taken(chopstick: ActorRef) extends ChopstickMessage
final case class Busy(chopstick: ActorRef) extends ChopstickMessage

/**
 * Some states the chopstick can be in
 */
sealed trait ChopstickState
case object Available extends ChopstickState
case object Taken extends ChopstickState

/**
 * Some state container for the chopstick
 */
final case class TakenBy(hakker: ActorRef)

class Chopstick extends Actor with FSM[ChopstickState, TakenBy] {
  import context._

  // A chopstick begins its existence as available and taken by no one
  startWith(Available, TakenBy(system.deadLetters))

  // When a chopstick is available, it can be taken by a some hakker
  when(Available) {
    case Event(Take, _) =>
      goto(Taken) using TakenBy(sender()) replying Taken(self)
  }

  // When a chopstick is taken by a hakker
  // It will refuse to be taken by other hakkers
  // But the owning hakker can put it back
  when(Taken) {
    case Event(Take, currentState) =>
      stay replying Busy(self)
    case Event(Put, TakenBy(hakker)) if sender() == hakker =>
      goto(Available) using TakenBy(system.deadLetters)
  }

  // Initialize the chopstick
  initialize()
}