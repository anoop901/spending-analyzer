name := """spending-analyzer"""
organization := "com.naravaram.anoop"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test
libraryDependencies += "com.univocity" % "univocity-parsers" % "2.5.5"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.naravaram.anoop.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.naravaram.anoop.binders._"
