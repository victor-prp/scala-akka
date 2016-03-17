package org.victorp.akka.samples.fsm

import akka.actor.{ActorRef, FSM, Actor}

import scala.collection.parallel.mutable

/**
 * @author victorp
 */
sealed trait AccountState
case object NotLoaded extends AccountState

case object StartLoading extends AccountState
case object PartiallyLoaded extends AccountState
case object FullyLoaded extends AccountState

sealed trait AccountEvent
case object LoadAccount extends AccountEvent
case object UnloadAccount extends AccountEvent
case object FailedToLoad extends AccountEvent
case object FailedToUnload extends AccountEvent

case class Sessions(sessions:mutable.ParHashMap[String,ActorRef])

class Account extends Actor with FSM[AccountState, Sessions] {
  import context._

  when(NotLoaded){
    case Event(LoadAccount, _ ) =>
      goto(StartLoading)

  }

  when(NotLoaded){
    case Event(LoadAccount, _ ) =>
      goto(StartLoading)
  }

}
