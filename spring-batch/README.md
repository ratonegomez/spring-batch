## Spring Batch example

# Overview

```
Read (csv file) --> process --> Write (json file)
```
# Building

```
mvn spring-boot:run
```

# Endpoint

URI: /api/country/job1

# Example 

Read CSV file

```
"130","450","MG","MDG","Madagascar","Madagascar"
```
Write to a JSON file

```
{"name":"Madagascar","iso_code":"MDG"}
```
# Framework
- [Spring boot][]
- [Spring batch][]

[Spring boot]: https://docs.spring.io/spring-boot/docs/2.2.0.RELEASE/reference/html/
[Spring batch]: https://docs.spring.io/spring-batch/docs/4.2.x/reference/html/index.html





