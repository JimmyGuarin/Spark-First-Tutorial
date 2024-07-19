package jimmyguarin
package advanced_concepts_spark

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object SequenceFiles {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local[1]")
    val sc = SparkContext.getOrCreate(conf)

    val fileRdd: RDD[String] = sc.textFile("./src/main/resources/apat63_99.txt") // rdd of lines

    val titles = fileRdd.first()
    val prdd = fileRdd
      .filter(_ != titles)
      .map(line => line.split(","))
      .map(list => ( list(4).replace("\"", ""), list(0)+ "," + list(1)))

    println(prdd.take(2).toSeq)

    //write
    prdd.saveAsSequenceFile("./src/main/resources/generated/apat63_99-seq")

    //read
    val prdd2 = sc.sequenceFile[String, String]("./src/main/resources/generated/apat63_99-seq")

    println(prdd2.countByKey().get("US"))


  }


}
