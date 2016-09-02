package it.miriade.commons.model.test;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.miriade.commons.model.dao.Dao;
import it.miriade.commons.model.dao.GenericDao;
import it.miriade.commons.model.test.entities.Foo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(Constants.CONTEXT_XML_FILE)
public class DaoImplTests implements InitializingBean {

	static final Logger slog = LoggerFactory.getLogger(DaoImplTests.class);
	final Logger log = LoggerFactory.getLogger(this.getClass());

	static Dao sdao;

	@Autowired
	Dao dao;

	@Autowired
	GenericDao<Foo, String> fooDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		sdao = dao;
	}

	@BeforeClass
	public static void oneTimeSetUp() {
		slog.debug("@BeforeClass");
		// log.debug("Creating FOO table..");
		// sdao.executeUpdateQuery("create table FOO (name varchar PRIMARY KEY, insert_date date);");
		// log.debug("Table FOO created");
	}

	@AfterClass
	public static void oneTimeTearDown() {
		slog.debug("@AfterClass");
		// sdao.executeUpdateQuery("drop table if exists FOO cascade;");
		// log.debug("Table FOO deleted");
	}

	@Before
	public void setUp() {
		log.debug("@Before");
	}

	@After
	public void tearDown() {
		log.debug("@After");
	}

	@Test
	public void nullObjects() {
		Assert.assertNotNull("static Dao must be not null", sdao);
		Assert.assertNotNull("Dao must be not null", dao);
		Assert.assertNotNull("GenericDao<Foo, String> must be not null", fooDao);
	}

	@Test
	public void deleteAll() {
		log.debug("TEST: deleteAll()");

		fooDao.deleteAll();
		int count = fooDao.getCount();
		Assert.assertEquals(0, count);
	}

	@Test
	public void selectQuery() {
		List<?> result = dao.executeSelectQuery("SELECT to_char(now(),'YYYY/MM/DD HH24:MI:SS.SSS'), now()");
		for (Object item : result)
			if (item != null && item.getClass().isArray())
				slog.debug(Arrays.deepToString((Object[]) item));
			else
				slog.debug("{}", item);

		result.clear();

		result = dao.executeSelectQuery("SELECT to_char(now(),'YYYY/MM/DD HH24:MI:SS.SSS')");
		for (Object item : result)
			if (item != null && item.getClass().isArray())
				slog.debug(Arrays.deepToString((Object[]) item));
			else
				slog.debug("{}", item);
	}

	@Test
	public void create() {
		log.debug("TEST: create()");

		Foo f0 = new Foo("foo");
		fooDao.save(f0);
		int count = fooDao.getCount();
		Assert.assertEquals(1, count);

		Foo f1 = fooDao.get("foo");
		Assert.assertEquals(f1.getUid(), "foo");
		Assert.assertEquals(f1, f0);
	}

}
