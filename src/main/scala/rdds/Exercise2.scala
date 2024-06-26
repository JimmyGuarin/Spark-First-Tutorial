package jimmyguarin
package rdds

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object Exercise2 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local[1]")
    val sc = SparkContext.getOrCreate(conf)

    val fichrdd: RDD[String] = sc.textFile("./src/main/resources/apat63_99.txt") // rdd of lines

    //My Solution
    val titles = fichrdd.first()
    val citedCiting = fichrdd.filter(_ != titles).map{ line =>
      val array = line.split(",")
      val country = array(4).replace("\"", "")
      val claims = if (array(8) == "") 0 else array(8).toInt
      (country, claims) //country - claims
    }


    // number and total
    val claimsByCountry = citedCiting.combineByKey[(Int, Int)](
      value => (1, value),
      (accum, value) => (accum._1 + 1, accum._2 + value),
      (accumPart1, accumPart2) => (accumPart1._1 + accumPart2._1, accumPart1._2 + accumPart2._2)
    )


    // ¿Cuál es el número medio de claims para España (código de país "ES")?
    val question1 = claimsByCountry.lookup("ES").head
    println("question1", question1, question1._2.toDouble / question1._1)


    // ¿Cuál es el número medio de claims para Argentina (código de país "AR")?
    val question2 = claimsByCountry.lookup("AR").head
    println("question2", question2, question2._2.toDouble / question2._1)

    // ¿Cuál es el número medio de claims para México (código de país "MX")?
    val question3 = claimsByCountry.lookup("MX").head
    println("question3", question3, question3._2.toDouble / question3._1)


    // NOTES: I can use mapValues to put the division as value

    claimsByCountry.takeOrdered(10).foreach { value =>
      println("claimsByCountry", value)
    }


  }

}
