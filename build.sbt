name := "scala-akka"

version := "1.0"

scalaVersion := "2.11.7"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

resolvers += "MVN Repo" at "http://mvnrepository.com/artifact"

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-cluster_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-remote_2.11" % "2.4.2"
libraryDependencies += "com.typesafe.akka" % "akka-cluster-tools_2.11" % "2.4.2"


// set the location of the JDK to use for compiling Java code.
// if 'fork' is true, this is used for 'run' as well
javaHome := Some(file("C:\\Program Files\\Java\\jdk1.8.0_73"))