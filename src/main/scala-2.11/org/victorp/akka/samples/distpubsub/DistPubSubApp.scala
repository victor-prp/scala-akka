package org.victorp.akka.samples.distpubsub

import akka.actor.{ActorRef, Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.victorp.akka.samples.cluster.SimpleClusterListener

import scala.collection.parallel.mutable

/**
 * @author victorp
 */
object DistPubSubApp extends App{
  val clusterAppConfig = ConfigFactory.parseURL(DistPubSubApp.getClass.getClassLoader.getResource("dist-pub-sub.conf"))
  println(clusterAppConfig)



  startup(Seq("2551", "2552", "0"))


  def startup(ports: Seq[String]): Unit = {
    val publishers = mutable.ParHashMap[String,ActorRef]()

    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
        withFallback(clusterAppConfig)

      // Create an Akka system
      val system = ActorSystem("ClusterSystem", config)
      // Create an actor that handles cluster domain events
      system.actorOf(Props(new PubSubClusterListener(s"listener-$port")), name = "clusterListener")
      system.actorOf(Props[Subscriber], name = s"subscriber-$port")
      val publisher = system.actorOf(Props[Publisher], name = s"publisher-$port")
      publishers(port) = publisher
    }

    println("Going to wait 10 seconds in order to make sure all cluster nodes are in sync")

    Thread.sleep(15000)
    println("Going to publish using all publishers")

    for ((port,publisherRef) <- publishers){
      publisherRef ! s"Hello from $port"
    }

  }

}
