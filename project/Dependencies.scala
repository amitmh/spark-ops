import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.0"
  val sparkCore = "org.apache.spark" %% "spark-core" % "3.0.1"
  val sparkSql = "org.apache.spark" %% "spark-sql" % "3.0.1"
}
