package jimmyguarin

import org.apache.spark.rdd.RDD

object Utils {

  def printRdds[A](list: RDD[A]): Unit = {
    list.foreach(item => {
      println(item)
    })
  }
}
