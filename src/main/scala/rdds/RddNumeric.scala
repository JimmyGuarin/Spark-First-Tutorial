package jimmyguarin
package rdds

import org.apache.spark.{SparkConf, SparkContext}

object RddNumeric {

  // the elements are number
  // you have extra methods like : sum, max, min, stats, mean, variance

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Demo").setMaster("local[1]")
    val sc = SparkContext.getOrCreate(conf)

    val nrdd = sc.parallelize(Range(1, 1000)).cache()

    // stats summary
    val sts = nrdd.stats()

    println("Stats Summary", sts)

    // get  outliers
    val stddev = sts.stdev
    val avg = sts.mean
    val frdd = nrdd.filter(n => Math.abs(n - avg) < (3 * stddev)).cache() // use cache because we are gonna use it more than once
    println("NUmber of outliers", sts.count - frdd.count)
  }
}
