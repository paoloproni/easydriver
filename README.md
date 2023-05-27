# easydriver
This is a library which let a programmer build queries for PostgreSQL easier than using plain JDBC

This library is a higher level than JDBC. The programmer does not need to write plain strings, which are error prone and it does not use the reflection, which is slow.

This library does not use annotations, making simple to port it to a different language from Java.

The goal is to use a easy and small layer, which is not a complete ORM such as Hibernate or TopLink, but it is a simple tool to make database access.

The idea is to generate some code, possibly automatically, that reflects how the tables are, then the programmer can build programmatically the queries, which are based on EasyDriver structure.
