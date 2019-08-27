name := "ScalaApplication"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "com.typesafe.scala-logging" % "scala-logging_2.12" % "3.9.2"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.3"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.3"
