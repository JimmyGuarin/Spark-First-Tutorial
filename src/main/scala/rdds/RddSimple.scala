package jimmyguarin
package rdds

import jimmyguarin.Utils.printRdds
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RddSimple {

  // Resilient distributed dataset

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("Demo").setMaster("local[1]")
    val sc = SparkContext.getOrCreate(conf)

    val data = Seq(1, 2, 3)

    // el numero de particiones por defecto es function del tamano
    // del cluster o del numero de bloques de fichero

    val rdd = sc.parallelize(data)
    printRdds(rdd)

    // se puede especificar el numero de particiones crear el rdds
    val rdds3 = sc.parallelize(Seq(1,2,3,4,5), 2)
    println("rdds3 partition size", rdds3.partitions.size)

    // to see how it's paralleled
    val rdd2: Array[Array[Int]] = rdd.glom().collect()
    print(s"rdd2 ${rdd2.foreach(print)}")

    /**
     * TRANSFORMACIONES:
     *
     * operacione sobre RDDS, devuelve otro RDD
     * se ejecuta de modo lazy
     * examples:
     * from one RDD: filter, map, flatMap, sample, distinct groupBy
     * from two RDDs: union, intersect,  subtract, cartersian
     */
    val hundred = Range(0, 100)
    val rdds4 = sc.parallelize(hundred, 10)
    val odds = rdds4.filter(_ % 2 != 0)
    val even = rdds4.filter(_ % 2 == 0)
    val intersec = odds.intersection(even)
    val union = odds.union(even)
    val double = hundred.map(_ * 2)
    val sample =  odds.sample(withReplacement = false, 0.5) // probably of choose an element

    // producto cartesiano, operacion muy costosa
    val cartesian = odds.cartesian(even)



    // TODO: write some Tests

    /**
     * ACCIONES:
     *
     * Obtienen datos de salida a partir de los RDDs
     * Devuelven valores al driver o al sistema de almacenamiento
     * Fuerzan a que se realicen las transformaciones pendientes
     *
     * acciones de agregacion, de conteo, y de obtener valores
     *
     * agregacion: reduce, fold,
     * los resultados son diferentes en comparacion a scala debido a que se ejecutan en paralelo en Spark cada particion
     *
     * count:
     * countApprox = version aproximada,
     * countApproxDistinct = version aproximada elementos distintos
     * countByValue
     * take
     * takeSAmple
     * top
     * takeOrdered
     * collect
     */

    // depende del numero de particiones
    val reduce = hundred.reduce((a, b) => a - b)
    print("reduce", reduce)

    //agregate: devuelve una coleccion agregando los elementos del RDD usando dos funciones, version general de reduce y fold
    val list1 = Range(1, 11).toSeq
    type Accum = (List[Int], Double, Int)
    def seqOp(accum: Accum, value: Int): Accum =
      ((value * value) :: accum._1, accum._2 * value, accum._3 + 1)
    def combOp(accum1: Accum, accum2: Accum) = {
      (accum1._1 ::: accum2._1, accum1._2 * accum2._2, accum1._3 + accum2._3)
    }
    val result1 = list1.aggregate((List[Int](), 1.0, 0))(seqOp, combOp)
    println("result aggregate", result1)

    println("intersec.count", intersec.count())
    println("union.count", union.count())

    println("count cartesian", cartesian.count)
    println("count approx cartesian", cartesian.countApprox(1, 0.5))

    odds.takeSample(withReplacement = false, 10)
      .foreach(println("line sample", _))

    val countByValue: collection.Map[Int, Long] = odds.countByValue()

    println("takeOrdered", odds.takeOrdered(4).mkString(" "))


    // HOMEWORK

    val fichrdd: RDD[String] = sc.textFile("../datos/quijote.txt") // rdd of lines
    val words = fichrdd.flatMap(line => line.split("")).map(_.strip) // rdd of words
    val sizeByWord = words.countByValue


  }

}
