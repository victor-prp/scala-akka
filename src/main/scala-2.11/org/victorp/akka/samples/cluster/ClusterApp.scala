package org.victorp.akka.samples.cluster

import akka.actor.{Props, ActorSystem}
import com.typesafe.config.{ConfigFactory, Config}

/**
 * @author victorp
 */
object ClusterApp extends App{
  val clusterAppConfig = ConfigFactory.parseURL(ClusterApp.getClass.getClassLoader.getResource("cluster-app.conf"))
  println(clusterAppConfig)



   startup(Seq("2551", "2552", "0"))


  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      // Override the configuration of the port
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
        withFallback(clusterAppConfig)

      // Create an Akka system
      val system = ActorSystem("ClusterSystem", config)
      // Create an actor that handles cluster domain events
      system.actorOf(Props(new SimpleClusterListener(s"listener-$port")), name = "clusterListener")
    }
  }

}
