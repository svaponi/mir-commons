# mir-commons
Utilities for Java (web) programming with Spring.

### mir-commons-base-model
Implementa lo strato DAO con dei Java Generic.
Se abbiamo una Java entity, esempio [`Foo`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/test/java/it/miriade/commons/model/test/entities/Foo.java), che usa le annotation JPA, possiamo avere le funzionalità DAO semplicemente innetando un oggetto [`GenericDao`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/main/java/it/miriade/commons/model/dao/GenericDao.java) nel nostro service.

```Java
@Autowired
@Qualifier("FooDao")
GenericDao<Foo, String> fooDao;
```

E aggiungendo la configurazione in `context.xml`.

```Xml
<bean id="FooDao" class="it.miriade.commons.model.dao.GenericDaoImpl">
  <constructor-arg value="it.miriade.commons.model.test.entities.Foo" />
</bean>
```

Oppure, se non abbiamo nessuna entity ma vogliamo usare le funzionalità di accesso al db, per esempio eseguire un named-query, posso usare l'oggetto [`Dao`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/main/java/it/miriade/commons/model/dao/Dao.java).

```Java
@Autowired
Dao dao;
```

E la relativa configurazione in `context.xml`.

```Xml
<bean id="Dao" class="it.miriade.commons.model.dao.DaoImpl" />
```

Per vedere i due oggetti al lavoro vedere  [`GenericDaoImplTests`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/test/java/it/miriade/commons/model/test/GenericDaoImplTests.java) e [`DaoImplTests`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/test/java/it/miriade/commons/model/test/DaoImplTests.java).


### mir-commons-base-utils
Offre utilities base per maneggiare oggetti Java standard come String, Date, File, Collection, ecc..

### mir-commons-db-utils
Offre funzionalità di accesso al DB per progetti senza JPA.

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
