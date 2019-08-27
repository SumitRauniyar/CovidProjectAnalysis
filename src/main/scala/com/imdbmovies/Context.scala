package com.imdbmovies

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

// Define the SparkSession to interact with underlying spark functionality.
trait Context {

  lazy val sparkConf = new SparkConf()
    .setAppName("Learn Spark")
    .setMaster("local[*]")
    .set("spark.cores.max", "2")

  lazy val sparkSession = SparkSession
    .builder()
    .config(sparkConf)
    .getOrCreate()

  // This gives an impression to Spark that hadoop is installed in our local system
  System.setProperty("hadoop.home.dir", "C:\\hadoop")

}
