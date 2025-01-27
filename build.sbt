ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "Spark-DEmo",
    idePackagePrefix := Some("jimmyguarin")
  )


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.5.0" // https://mvnrepository.com/artifact/org.apache.spark/spark-core
)

