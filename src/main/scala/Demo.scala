package jimmyguarin

import org.apache.spark.{SparkConf, SparkContext}

object Demo {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local[1]")
    val sc = SparkContext.getOrCreate(conf)

    val data = Seq(
      (10, "Oscar"),
      (11, "Oscarfmdc"),
      (12, "Ofernandez")
      )

    val rdd = sc.parallelize(data)
    rdd.foreach(item => {
      println(item)
    })

  }
}
