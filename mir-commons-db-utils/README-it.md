# mir-commons-db-utils <sup><sub>[[Javadoc](https://svaponi.github.io/mir-commons/mir-commons-db-utils)]</sub></sup>

Questo modulo fornisce funzionalità di accesso al DB per progetti senza JPA (Hibernate, Ibatis, ecc..). Ideale per piccoli progetti, prototipi o studi di fattibilità.

## How to

```Java
@Autowired
DbQuery dbq;

private Date C0 = new Date();
private String C1 = "foo";
private int C2 = 1;
private long C3 = Long.MAX_VALUE;
private double C4 = 0.23;

```
```Java
dbq.update("CREATE TABLE test01 (" +
"C0 timestamp, " +
"C1 varchar, " +
"C2 integer, " +
"C3 bigint, " +
"C4 double precision)");
```
```Java
dbq.update("INSERT INTO test01 SELECT " +
"to_timestamp('" + DateHandler.formatDate(C0, "yyyyMMdd HH:mm:ss.SSS") + "','YYYYMMDD HH24:MI:SS.MS') AS C0, " +
"'" + C1 + "' AS C1," +
C2 + " AS C2, " +
C3 + " AS C3, " +
C4 + " AS C4");
```
```Java
DbResult result = dbq.select("SELECT C0, C1, C2, C3, C4 FROM test01");
```
```Java
int n = dbq.update("DELETE FROM test01");
```
E anche con entity  [Test01](https://github.com/svaponi/mir-commons/blob/master/mir-commons-dbutils/src/test/java/it/miriade/commons/dbutils/entities/Test01.java).

```Java
List<test01> entities = dbq.getEntityList("SELECT * FROM test01 ORDER BY C0", test01.class);
```
Vedi esempio completo in [`DbQueryTest`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-dbutils/src/test/java/it/miriade/commons/dbutils/DbQueryTest.java)

### mir-commons-web-utils
Offre funzionalità base per l'utilizzo di Spring MVC, sia con UI che per API REST. Uno tra tutti la gestione dei log, vedi [`LogInitializerServlet`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-web-utils/src/main/java/it/miriade/commons/web/configuration/LogInitializerServlet.java).
