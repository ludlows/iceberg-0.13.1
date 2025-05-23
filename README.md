<!--
  - Licensed to the Apache Software Foundation (ASF) under one
  - or more contributor license agreements.  See the NOTICE file
  - distributed with this work for additional information
  - regarding copyright ownership.  The ASF licenses this file
  - to you under the Apache License, Version 2.0 (the
  - "License"); you may not use this file except in compliance
  - with the License.  You may obtain a copy of the License at
  -
  -   http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing,
  - software distributed under the License is distributed on an
  - "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  - KIND, either express or implied.  See the License for the
  - specific language governing permissions and limitations
  - under the License.
  -->

![](site/docs/img/Iceberg-logo.png)

A customized version of apache iceberg 0.13.1 .
It is enough for batch-processing scenarios.

[![](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/java-ci.yml/badge.svg)](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/java-ci.yml)
[![](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/spark-ci.yml/badge.svg)](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/spark-ci.yml)
[![](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/hive-ci.yml/badge.svg)](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/hive-ci.yml)
[![](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/flink-ci.yml/badge.svg)](https://github.com/ludlows/iceberg-0.13.1/actions/workflows/flink-ci.yml)

Apache Iceberg is a new table format for storing large, slow-moving tabular data. It is designed to improve on the de-facto standard table layout built into Hive, Trino, and Spark.

Background and documentation is available at <https://iceberg.apache.org>

### Building

Iceberg is built using Gradle with Java 1.8 or Java 11.

* To invoke a build and run tests: `./gradlew build`
* To skip tests: `./gradlew build -x test -x integrationTest`

Iceberg table support is organized in library modules:

* `iceberg-common` contains utility classes used in other modules
* `iceberg-api` contains the public Iceberg API
* `iceberg-core` contains implementations of the Iceberg API and support for Avro data files, **this is what processing engines should depend on**
* `iceberg-parquet` is an optional module for working with tables backed by Parquet files
* `iceberg-arrow` is an optional module for reading Parquet into Arrow memory
* `iceberg-orc` is an optional module for working with tables backed by ORC files
* `iceberg-hive-metastore` is an implementation of Iceberg tables backed by the Hive metastore Thrift client
* `iceberg-data` is an optional module for working with tables directly from JVM applications

This project Iceberg also has modules for adding Iceberg support to processing engines:

* `iceberg-spark2` is an implementation of Spark's Datasource V2 API in 2.4 for Iceberg (use iceberg-spark-runtime for a shaded version)
* `iceberg-spark3` is an implementation of Spark's Datasource V2 API in 3.0 for Iceberg (use iceberg-spark3-runtime for a shaded version)
* `iceberg-flink` contains classes for integrating with Apache Flink (use iceberg-flink-runtime for a shaded version)
* `iceberg-mr` contains an InputFormat and other classes for integrating with Apache Hive
* `iceberg-pig` is an implementation of Pig's LoadFunc API for Iceberg

### Compatibility

Iceberg's Spark integration is compatible with Spark using the modules in the following table:

| Iceberg version | Spark 2.4.x           | Spark 3.0.x            | Spark 3.1.x                    | Spark 3.2.x                    |
| --------------- | --------------------- | ---------------------- | ------------------------------ | ------------------------------ |
| master branch   | iceberg-spark-runtime | iceberg-spark3-runtime | iceberg-spark-runtime-3.1_2.12 | iceberg-spark-runtime-3.2_2.12 |
| 0.12.x          | iceberg-spark-runtime | iceberg-spark3-runtime | iceberg-spark-runtime-3.1_2.12 |                                |
| 0.11.x          | iceberg-spark-runtime | iceberg-spark3-runtime |                                |                                |
| 0.10.x          | iceberg-spark-runtime | iceberg-spark3-runtime |                                |                                |
| 0.9.x           | iceberg-spark-runtime | iceberg-spark3-runtime |                                |                                |

Iceberg's Flink integration is compatible with Flink using the modules in the following table:

| Iceberg version | Flink 1.11.x          | Flink 1.12.x               | Flink 1.13.x               | Flink 1.14.x               |
| --------------- | --------------------- | -------------------------- | -------------------------- | -------------------------- |
| master branch   |                       | iceberg-flink-runtime-1.12 | iceberg-flink-runtime-1.13 | iceberg-flink-runtime-1.14 |
| 0.12.x          |                       | iceberg-flink-runtime      |                            |                            |
| 0.11.x          | iceberg-flink-runtime |                            |                            |                            |
| 0.10.x          | iceberg-flink-runtime |                            |                            |                            |

