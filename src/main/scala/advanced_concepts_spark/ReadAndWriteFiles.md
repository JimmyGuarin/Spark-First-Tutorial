**Text Files**
-

`sc.textFile(fileName, directory)` create a RDD from lines of one or several text files

- if you specify a directory, it will read all files in it

`sc.wholeTextFile(fileName/directory)` RDD key/value

`sc.saveAsTextFile(output_directory)` save a directory. as many files as partitions

**Sequence Files**
- 
a flat, binary file type that serves as a container for data to be used
in Apache Hadoop distributed computing projects.
- Key/Value
- Implement interface `Writable`
- `sc.saveAsSequenceFile(output_directory)`

