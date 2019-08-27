//Author : Sumit Rauniyar
//Description : Capture Changed Data
//Assumption : The Primary Key is Unknown

package com.imdbmovies

object ImdbDelta extends App with Context {

  // Create a DataFrame from reading a master CSV file
  val df_full_imdb = sparkSession
    .read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("D:\\imdb_movies.csv")
    .toDF("id", "homepage", "original_language", "original_title", "popularity", "release_date", "revenue", "runtime", "status", "title", "vote_average", "vote_count", "modified_date")

  // Create a DataFrame from reading an incremental CSV file
  val df_incr_imdb = sparkSession
    .read
    .option("header", "true")
    .option("inferSchema", "true")
    .csv("D:\\imdb_movies_update.csv")
    .toDF("id", "homepage", "original_language", "original_title", "popularity", "release_date", "revenue", "runtime", "status", "title", "vote_average", "vote_count", "modified_date")

  // Create a temporary table for these data frames
  df_full_imdb.createOrReplaceTempView("master_imdb")
  df_incr_imdb.createOrReplaceTempView("incr_imdb")

  val df_full_imdb_with_hash = sparkSession.sql("Select a.*,hash(concat(a.id,a.homepage,a.original_language,a.original_title,a.popularity,a.release_date,a.revenue,a.runtime,a.status,a.title,a.vote_average,a.vote_count,a.modified_date)) as hash_key from master_imdb a")
  val df_incr_imdb_with_hash = sparkSession.sql("Select a.*,hash(concat(a.id,a.homepage,a.original_language,a.original_title,a.popularity,a.release_date,a.revenue,a.runtime,a.status,a.title,a.vote_average,a.vote_count,a.modified_date)) as hash_key from incr_imdb a")

  // Create new temporary tables with the hash value of the entire row included
  df_full_imdb_with_hash.createOrReplaceTempView("interm_master_imdb")
  df_incr_imdb_with_hash.createOrReplaceTempView("interm_incr_imdb")

 // Capture the delta
  val df_comparison = sparkSession.sql(
    """
      |select incr.*
      |from
      |interm_incr_imdb incr
      |left join
      |interm_master_imdb master
      |on
      |incr.hash_key=master.hash_key
      |where
      |master.hash_key is null
      |""".stripMargin)

  //The first step of filtering out exact duplicates between two records
  df_comparison.show()

  val df_full_imdb_with_hash_wo_time = sparkSession.sql("Select a.*,hash(concat(a.id,a.homepage,a.original_language,a.original_title,a.popularity,a.release_date,a.revenue,a.runtime,a.status,a.title,a.vote_average,a.vote_count)) as hash_key from master_imdb a")
  val df_incr_imdb_with_hash_wo_time = sparkSession.sql("Select a.*,hash(concat(a.id,a.homepage,a.original_language,a.original_title,a.popularity,a.release_date,a.revenue,a.runtime,a.status,a.title,a.vote_average,a.vote_count)) as hash_key from incr_imdb a")

  // Create new temporary tables with the hash value of the row included excluding the timestamp
  df_full_imdb_with_hash_wo_time.createOrReplaceTempView("interm_wo_time_master_imdb")
  df_incr_imdb_with_hash_wo_time.createOrReplaceTempView("interm_wo_time_incr_imdb")

  // Capture the delta again, this time without the timestamp
  val df_comparison_wo_time = sparkSession.sql(
    """
      |select incr.*
      |from
      |interm_wo_time_incr_imdb incr
      |left join
      |interm_wo_time_master_imdb master
      |on
      |incr.hash_key=master.hash_key
      |where
      |master.hash_key is null
      |""".stripMargin)

  //The second step of filtering out same copy of the full row excluding timestamp between two records
  df_comparison_wo_time.show()

}
