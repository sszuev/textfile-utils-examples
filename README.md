## TextFile Utils Examples

Java-examples for https://github.com/DataFabricRus/textfile-utils
#### MergeSort:
```java
cc.datafabric.textfileutils.files.MergeSortKt.sort(
/* source file */ source,
/* result file (nonexistent physically) */ target,
/* comparator to sort lines */ comparator,
/* delimiter to be used to split content on lines */ "\n",
/* allowed memory consumption (approximately) */ allocatedMemory,
/* to control diskspace, if true no additional diskspace will be used */ false,
/* e.g. UTF-8 */ charset,
/* kotlin-dispatcher, to be used for sort parallelization */ kotlinx.coroutines.Dispatchers.getIO()
);
```
#### BinarySearch:
```java
kotlin.Pair<Long, List<String>> found = cc.datafabric.textfileutils.files.BinarySearchKt.binarySearch(
/* source (sorted) file */ source,
/* pattern to search */ searchString,
/* to use while reading data from file */ ByteBuffer.allocateDirect(8912 * 2),
/* e.g. UTF-8 */ charset,
/* delimiter to be used to split content on lines */ "\n",
/* comparator to sort lines */ comparator,
/* line restriction, to avoid memory lack e.g. when there is no delimiter */ 1024,
/* maximum number of lines in a paragraph */ 8912
);
```

#### Available via [jitpack](https://jitpack.io/#DataFabricRus/textfile-utils):
```kotlin
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.DataFabricRus:textfile-utils:1.0-SNAPSHOT'
}
```

### Apache License Version 2.0