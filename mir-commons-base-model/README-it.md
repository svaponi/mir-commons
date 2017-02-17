# mir-commons-base-model <sup><sub>[[Javadoc](https://svaponi.github.io/mir-commons/mir-commons-base-model)]</sub></sup>

Offre una implementazione ready-to-use del DAO Layer basata su Java Generics.

## How to

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
