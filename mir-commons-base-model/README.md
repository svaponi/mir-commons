# mir-commons-base-model <sup><sub>[[Javadoc](https://svaponi.github.io/mir-commons/mir-commons-base-model)]</sub></sup>

Provides a ready-to-use DAO layer implementation based on Java Generics.

## How to

Once you have your Java entity written using JPA annotations, like [`Foo`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/test/java/it/miriade/commons/model/test/entities/Foo.java), make it implement `ModelEntity<PK>` interface (with PK the class of your @Id, also a composite one). Then you can access all DAO features with 3 simple moves.

- Injecting a [`GenericDao`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/main/java/it/miriade/commons/model/dao/GenericDao.java) object in our service.

```java
@Autowired
@Qualifier("FooDao")
GenericDao<Foo, String> fooDao;
```

- Add `FooDao` declaration in your `context.xml`.

```xml
<bean id="FooDao" class="it.miriade.commons.model.dao.GenericDaoImpl">
  <constructor-arg value="it.miriade.commons.model.test.entities.Foo" />
</bean>
```

- Be sure to have a proper Hibernate config, and add an `HibernateTemplate`.

```xml
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
<property name="driverClass" value="YOUR_DRIVER" />
<property name="jdbcUrl" value="YOUR_URL" />
<property name="user" value="YOUR_USER" />
<property name="password" value="YOUR_PASSWORD" />
</bean>

<bean id="sessionFactory"
  class="org.springframework.orm.hibernate4.LocalSessionFactoryBean">
  <property name="mappingResources">
    <list>
      <value>YOUR_NAMED_QUERIES_FILE</value>
    </list>
  </property>
  <property name="dataSource" ref="dataSource"></property>
  <property name="packagesToScan">
    <list>
      <value>YOUR_ENTITIES_PACKAGE</value>
    </list>
  </property>
  <property name="hibernateProperties">
    <props>
      <prop key="hibernate.show_sql">false</prop>
      <prop key="hibernate.format_sql">false</prop>
      <prop key="use_sql_comments">false</prop>
      <prop key="hibernate.dialect">YOUR_PROVIDER_DIALECT</prop>
      <prop key="hibernate.hbm2ddl.auto">${hibernate.hbm2ddl.auto}</prop>
      <prop key="hibernate.current_session_context_class">validate</prop>
      <prop key="hibernate.bytecode.use_reflection_optimizer">true</prop>
      <prop key="hibernate.temp.use_jdbc_metadata_defaults">false</prop>
      <prop key="javax.persistence.validation.mode">NONE</prop>
    </props>
  </property>
</bean>

<bean id="hibernateTemplate" class="org.springframework.orm.hibernate4.HibernateTemplate">
  <property name="sessionFactory" ref="sessionFactory" />
</bean>
```

Or, alternativelly, if you do not have any entities but you just need to access DB somehow, for example exec a named-query, just use a [`Dao`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/main/java/it/miriade/commons/model/dao/Dao.java) object.

```java
@Autowired
Dao dao;
```

With relative config in `context.xml`.

```xml
<bean id="Dao" class="it.miriade.commons.model.dao.DaoImpl" />
```

## See in action

Se both alternatives in action in these JUnit classes: [`GenericDaoImplTests`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/test/java/it/miriade/commons/model/test/GenericDaoImplTests.java) and [`DaoImplTests`](https://github.com/svaponi/mir-commons/blob/master/mir-commons-base-model/src/test/java/it/miriade/commons/model/test/DaoImplTests.java).
