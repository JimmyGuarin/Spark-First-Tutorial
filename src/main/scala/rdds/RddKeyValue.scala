package jimmyguarin
package rdds

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object RddKeyValue {

  def main(args: Array[String]): Unit = {


    val conf = new SparkConf().setAppName("Demo").setMaster("local[1]")
    val sc = SparkContext.getOrCreate(conf)

    //Muy usados en Big Data (MapReduce)

    val listTuples = List(('a', 1), ('b', 2), ('c', 3))

    val numbers = 1 to 100
    val bySquare = numbers.toSeq.map(n => (n*n, n))

    val prdd = sc.parallelize(listTuples)

    val prdd2 = sc.parallelize(bySquare)

    /**
     * ZIP TO RDDs
     * Same size, same partition
     */
    val rdd1: RDD[Int] = sc.parallelize(1 to 10, 2)
    val rdd2: RDD[Int] = sc.parallelize(1001 to 1010, 2)
    val prdd3: RDD[(Int, Int)] = rdd1 zip rdd2

    /**
     * Transformaciones 1 RDD KEY VALUE
     *
     * reduceByKey, foldByKey: similar to reduce and fold in simple rdds
     *
     * groupByKey: group vales associated to the same key
     * this operation is very expensive in communications
     * it is better to user reduce operations
     *
     * combineByKey: general method to aggregate by key
     *
     * mapValues, flatMapValues
     *
     */

    // reduceByKey
    val example: Seq[(Char, Int)] = List(('a', 2), ('b', 2), ('a', 8), ('c', 3), ('b', 3))
    val pr = sc.parallelize(example)
    val reduceByKey = pr.reduceByKey((x, y) => x + y)
    val collectReduceByKey = reduceByKey.collect()

    println("collectReduceByKey", collectReduceByKey.mkString(" "))


    // groupByKey
    val pr2 = sc.parallelize(example)
    val groupByKey: RDD[(Char, Iterable[Int])] = pr2.groupByKey()
    val collectGroupByKey: Array[(Char, Iterable[Int])] = groupByKey.collect()
    println("collectGroupByKey", collectGroupByKey.mkString(" "))

    // combineByKey
    //example: for each key, get a tuple that has the sum and num of values
    val pr3: RDD[(Char, Int)] = sc.parallelize(example)
    val combineByKey: RDD[(Char, (Int, Int))] = pr3.combineByKey[(Int, Int)](
      value => (value, 1), //create combiner
      (accum, value) => ((accum._1 + value, accum._2 + 1)), //merge value
      (accum1, accum2) => ((accum1._1 + accum2._1, accum1._2 + accum2._2)), // merge combiner
    )
    val collectCombineByKey: Array[(Char, (Int, Int))] = combineByKey.collect()
    println("collectCombineByKey", collectCombineByKey.mkString(" "))


    /**
     * Transformaciones 2RDD KEY VALUE
     *
     *  join - leftOuterJoin - rightOuterJoin - fullOuterJoin
     *  subtractByKey
     *  cogroup
     */

    val listTuples2 = List(('a', 1), ('b', 2), ('c', 3))
    val listTuples3 = List(('a', 5), ('b', 5))


    /**
     * Actions
     *
     *  same as simple plus:
     *  collectAsMap
     *  countByKey
     *  lookup(key)
     */

  }

}
