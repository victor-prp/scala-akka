package org.victorp.akka.samples.cluster

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.ClusterEvent._

/**
 * @author victorp
 */
class SimpleClusterListener(val name:String) extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  // subscribe to cluster changes, re-subscribe when restart
  override def preStart(): Unit = {
    //#subscribe
    cluster.subscribe(self,
      classOf[MemberEvent], classOf[UnreachableMember])

    val allActors = context.actorSelection("*")
    allActors ! MemberStarted(name)
    //#subscribe
  }


  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberStarted(memberName) =>
      log.info("member started: {}", memberName)
    case clusterSate:CurrentClusterState =>
      log.info("clusterSate: {}", clusterSate)
    case MemberUp(member) =>
      log.info("Member is Up: {}", member.address)
    case UnreachableMember(member) =>
      log.info("Member detected as unreachable: {}", member)
    case MemberRemoved(member, previousStatus) =>
      log.info("Member is Removed: {} after {}",
        member.address, previousStatus)
    case _: MemberEvent => // ignore
  }

  case class MemberStarted(name:String)
}