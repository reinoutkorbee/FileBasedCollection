# FileBasedCollection

The FileBasedCollection is a Java Colelction backed by a file. The file is a binary, compressed file, generated with the Snappy library. The version used here is snappy-java-1.1.2.1.jar. 

The FileBasedCollection is a modifiable collection that extends the AbstractCollection and implements Closeable. The FileBasedCollection has an internal store and a chunk size. If the internal store contains more elements then the chunk size, the internal store is swapped to disk. The iterator implements Closeable and runs through the file backing the colelction, loading the next n elements. The iterator is auto-closeable. Great care has been taken to clean up data after the collection, including shut down hooks, but if the process crashes, the backing file will be left over. This collection is meant for very large colelctions that can't possibly fit in memory. It has been tested with billions of items and several gigabytes of data.
