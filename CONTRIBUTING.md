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

# Contributing

In this document, you will find some guidelines on contributing to Apache Iceberg. Please keep in mind that none of
these are hard rules and they're meant as a collection of helpful suggestions to make contributing as seamless of an
experience as possible.

If you are thinking of contributing but first would like to discuss the change you wish to make, we welcome you to
head over to the [Community](https://iceberg.apache.org/community/) page on the official Iceberg documentation site
to find a number of ways to connect with the community, including slack and our mailing lists. Of course, always feel
free to just open a [new issue](https://github.com/apache/iceberg/issues/new) in the GitHub repo.

## Pull Request Process

Pull requests are the preferred mechanism for contributing to Iceberg
* PRs are automatically labeled based on the content by our github-actions labeling action
* It's helpful to include a prefix in the summary that provides context to PR reviewers, such as `Build:`, `Docs:`, `Spark:`, `Flink:`, `Core:`, `API:`
* If a PR is related to an issue, adding `Closes #1234` in the PR description will automatically close the issue and helps keep the project clean
* If a PR is posted for visibility and isn't necessarily ready for review or merging, be sure to convert the PR to a draft

## Building the Project Locally

Please refer to the [Building](https://github.com/apache/iceberg#building) section of the main readme for instructions
on how to build iceberg locally.

## Style

For Java styling, check out the section
[Setting up IDE and Code Style](https://iceberg.apache.org/community/#setting-up-ide-and-code-style) from the
documentation site.

For Python, please use the tox command `tox -e format` to apply autoformatting to the project.

### Java style guidelines

#### Line breaks

Continuation indents are 2 indents (4 spaces) from the start of the previous line.

Try to break long lines at the same semantic level to make code more readable.
* Don't use the same level of indentation for arguments to different methods
* Don't use the same level of indentation for arguments and chained methods

```java
  // BAD: hard to see arguments passed to the same method
  doSomething(new ArgumentClass(1,
      2),
      3);

  // GOOD: break lines at the same semantic level
  doSomething(
      new ArgumentClass(1, 2),
      3);

  // BAD: arguments and chained methods mixed
  SomeObject myNewObject = SomeObject.builder(schema, partitionSpec
      sortOrder)
      .withProperty("x", "1")
      .build()

  // GOOD: method calls at the same level, arguments indented
  SomeObject myNewObject = SomeObject
      .builder(schema, partitionSpec,
          sortOrder)
      .withProperty("x", "1")
      .build()
```

#### Method naming

1. Make method names as short as possible, while being clear. Omit needless words.
2. Avoid `get` in method names, unless an object must be a Java bean.
    * In most cases, replace `get` with a more specific verb that describes what is happening in the method, like `find` or `fetch`.
    * If there isn't a more specific verb or the method is a getter, omit `get` because it isn't helpful to readers and makes method names longer.
3. Where possible, use words and conjugations that form correct sentences in English when read
    * For example, `Transform.preservesOrder()` reads correctly in an if statement: `if (transform.preservesOrder()) { ... }`

#### Boolean arguments

Avoid boolean arguments to methods that are not `private` to avoid confusing invocations like `sendMessage(false)`. It is better to create two methods with names and behavior, even if both are implemented by one internal method.

```java
  // prefer exposing suppressFailure in method names
  public void sendMessageIgnoreFailure() {
    doSomethingInternal(true);
  }

  public void sendMessage() {
    sendMessageInternal(false);
  }

  private void sendMessageInternal(boolean suppressFailure) {
    ...
  }
```

When passing boolean arguments to existing or external methods, use inline comments to help the reader understand actions without an IDE.

```java
  // BAD: it is not clear what false controls
  dropTable(identifier, false);

  // GOOD: these uses of dropTable are clear to the reader
  dropTable(identifier, true /* purge data */);
  dropTable(identifier, purge);
```

#### Config naming

1. Use `-` to link words in one concept
    * For example, preferred convection `access-key-id` rather than `access.key.id`
2. Use `.` to create a hierarchy of config groups
    * For example, `s3` in `s3.access-key-id`, `s3.secret-access-key`
