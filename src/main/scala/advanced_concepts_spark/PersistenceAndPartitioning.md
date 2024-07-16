
**Persistence**

*Problem when using RDD several times*

- Spark recomputes RDD and its dependencies each time an action is executed.
- very expensive (more in iterative problems)

Solution:

- Keep the RDD in memory / disc
- methods: cache() or persist()

Persistence levels: *spark.StorageLevel*
   - MEMORY_ONLY (DEFAULT)


**Partitioning**

*Number of partitions based on cluster size or number of HDFS file blocks*

- It's possible to adjust it when creating or operating a RDD
- Two useful functions: 
  - `rdd.getNumPartitions()` RDD num of partitions 
  - `rdd.glom()` return a new RDD along with each partition elements in a list 

repartitioning functions: 
- `repartition(n)` a new RDD  with n partitions
- `coalesce(n)` more efficient
- `partitionBy(n, [partitionFunc])` by key. Use a partition function. RDD key/value only
   