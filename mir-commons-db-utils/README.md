# mir-commons-db-utils <sup><sub>[[Javadoc](https://svaponi.github.io/mir-commons/mir-commons-db-utils)]</sub></sup>

Provides features to access DB without Hibernate, ideal for simple projects or feasibility tests.


## How to

Setup this module is super easy, you just need 2 simple moves.

- Injecting a [`Dao`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/main/java/it/miriade/commons/model/dao/Dao.java) object in our service.

```java
@Autowired
DbQuery dbq;
```

- Setup a DataSource in your `context.xml`.

```xml
<bean id="dataSource"
class="org.springframework.jdbc.datasource.DriverManagerDataSource">
<property name="driverClassName" value="${jdbc.driver.class}" />
<property name="url" value="${jdbc.url}" />
<property name="username" value="${jdbc.user}" />
<property name="password" value="${jdbc.password}" />
</bean>
```
Then you can start accessing you DB with standard SQL.

```java
Date C0 = new Date();
String C1 = "foo";
int C2 = 1;
long C3 = Long.MAX_VALUE;
double C4 = 0.23;

// Create a new table
dbq.update("CREATE TABLE test01 (" +
"C0 timestamp, " +
"C1 varchar, " +
"C2 integer, " +
"C3 bigint, " +
"C4 double precision)");

// Insert data into table
dbq.update("INSERT INTO test01 SELECT " +
"to_timestamp('" + DateHandler.formatDate(C0, "yyyyMMdd HH:mm:ss.SSS") + "','YYYYMMDD HH24:MI:SS.MS') AS C0, " +
"'" + C1 + "' AS C1," +
C2 + " AS C2, " +
C3 + " AS C3, " +
C4 + " AS C4");

// Select data from table
DbResult result = dbq.select("SELECT C0, C1, C2, C3, C4 FROM test01");

// Delete data from table
int n = dbq.update("DELETE FROM test01");
```
And you could go on with all SQL that comes into your mind. You can also inject results into a entity object, like [`Test01`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-db-utils/src/test/java/it/miriade/commons/dbutils/entities/Test01.java).

```java
List<test01> entities = dbq.getEntityList("SELECT * FROM test01 ORDER BY C0", Test01.class);
```

See all this in action in [`DbQueryTest`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-db-utils/src/test/java/it/miriade/commons/dbutils/DbQueryTest.java)
