package jimmyguarin
package rdds

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Exercise1 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local[1]")
    val sc = SparkContext.getOrCreate(conf)

    // (citing, cited)
    val fichrdd: RDD[String] = sc.textFile("./src/main/resources/cite75_99.txt") // rdd of lines


    // this solution is shorted and it was proposed by teacher
    val shortedMapping = fichrdd.map{ line =>
        val array = line.split(",")
        (array(1), 1)
    }.reduceByKey(_ + _)


    //My Solution
    val citedCiting = fichrdd.map{ line =>
      val array = line.split(",")
      (array(1), array(0))
    }

    val totalCitingByCited = citedCiting.combineByKey[Int](
      value => 1,
      (accum, value) => accum + 1,
      (accumPart1, accumPart2) => accumPart1 + accumPart2
    )

    //¿Cuántas citas ha recibido la patente número 3986997?
    val question1 = totalCitingByCited.lookup("3986997")
    println("question1", question1)


    //¿Cuántas citas ha recibido la patente número 4418284?
    val question2 = totalCitingByCited.lookup("4418284")
    println("question2", question2)

    //¿Cuántas citas ha recibido la patente número 4314227?
    val question3 = totalCitingByCited.lookup("4314227")
    println("question3", question3)

    //¿Cuántas citas ha recibido la patente número 3911418?
    val question4 = totalCitingByCited.lookup("3911418")
    println("question4", question4)

    //¿Cuál es la patente que ha recibido más citas? Indica el número de patente.

    val question5 = totalCitingByCited.sortBy(
      (cited) => cited._2,
      ascending = false
    ).take(1)
    println("question5", question5.mkString(""))

    println("count", citedCiting)
  }

}
