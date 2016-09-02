package it.miriade.commons.model.test;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.miriade.commons.model.dao.Dao;
import it.miriade.commons.model.dao.GenericDao;
import it.miriade.commons.model.test.entities.Foo;
import it.miriade.commons.utils.DateHandler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(Constants.CONTEXT_XML_FILE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DaoImplTests {

	final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	Dao dao;

	@Autowired
	GenericDao<Foo, String> fooDao;

	// @BeforeClass
	// public static void oneTimeSetUp() {
	// slog.debug("@BeforeClass");
	// }

	// @AfterClass
	// public static void oneTimeTearDown() {
	// slog.debug("@AfterClass");
	// }

	// @Before
	// public void setUp() {
	// log.debug("@Before");
	// }

	// @After
	// public void tearDown() {
	// log.debug("@After");
	// }

	@Test
	public void t0_nullObjects() {
		log.debug("t0_nullObjects");

		Assert.assertNotNull("Dao is null", dao);
		Assert.assertNotNull("GenericDao<Foo, String> is null", fooDao);
	}

	// @Test
	public void t1_deleteAll() {
		log.debug("t1_deleteAll");

		fooDao.deleteAll();
		int count = fooDao.getCount();
		Assert.assertEquals(0, count);
	}

	@Test
	public void t2_selectQuery() {
		log.debug("t2_selectQuery");

		List<?> result = dao
				.executeSelectQuery("SELECT 1 as a, 2.0 as b, to_char(now(),'YYYY/MM/DD HH24:MI:SS.SSS') as now_char, now(), cast(now() as date) as now_date");
		show(result);

		Assert.assertNotNull("result is null", result);
		Assert.assertSame("result has 1 row", result.size(), 1);
		Assert.assertSame("result has 5 columns", ((Object[]) result.get(0)).length, 5);
		Assert.assertEquals("type of value at row 0, column 0", ((Object[]) result.get(0))[0].getClass(), Integer.class);
		Assert.assertEquals("type of value at row 0, column 1", ((Object[]) result.get(0))[1].getClass(), BigDecimal.class);
		Assert.assertEquals("type of value at row 0, column 2", ((Object[]) result.get(0))[2].getClass(), String.class);
		Assert.assertEquals("type of value at row 0, column 3", ((Object[]) result.get(0))[3].getClass(), Timestamp.class);
		Assert.assertEquals("type of value at row 0, column 4", ((Object[]) result.get(0))[4].getClass(), Date.class);

		result.clear();

		result = dao.executeSelectQuery("SELECT to_char(now(),'YYYY/MM/DD HH24:MI:SS.SSS')");
		show(result);

		Assert.assertNotNull("result is null", result);
		Assert.assertSame("result has 1 row", result.size(), 1);
		// One column does not need an Object array
		Assert.assertNotEquals("result has 1 column", result.get(0).getClass(), Object[].class);
		Assert.assertEquals("value at row 0, column 0", result.get(0).toString().substring(0, 10), DateHandler.current("yyyy/MM/dd"));

		result.clear();

		result = dao.executeSelectQuery("SELECT 1 as a union SELECT 2 union SELECT 3");
		show(result);

		Assert.assertNotNull("result is null", result);
		Assert.assertSame("result has 3 rows", result.size(), 3);
		// One column does not need an Object array
		Assert.assertNotEquals("result has 1 column", result.get(0).getClass(), Object[].class);
		Assert.assertEquals("value at row 0, column 0", result.get(0), 1);
		Assert.assertEquals("value at row 1, column 0", result.get(1), 2);
		Assert.assertEquals("value at row 2, column 0", result.get(2), 3);

	}

	@Test
	public void t3_nameQueryQuery() {
		log.debug("t3_nameQueryQuery");

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("p1", 1000);
		params.put("p2", "__%");
		List<?> result = dao.getTupleCollectionByNamedQueryAndParams("MyNamedQuery", params);
		show(result);

		Assert.assertNotNull("result is null", result);
		Assert.assertEquals("result size", result.size(), 2);
		Assert.assertEquals("result columns", ((Object[]) result.get(0)).length, 3);
		Assert.assertEquals("type of value at row 0, column 0", ((Object[]) result.get(0))[0].getClass(), Integer.class);
		Assert.assertEquals("type of value at row 0, column 1", ((Object[]) result.get(0))[1].getClass(), String.class);
		Assert.assertEquals("type of value at row 0, column 2", ((Object[]) result.get(0))[2].getClass(), BigDecimal.class);
		Assert.assertEquals("type of value at row 1, column 0", ((Object[]) result.get(1))[0].getClass(), Integer.class);
		Assert.assertEquals("type of value at row 1, column 1", ((Object[]) result.get(1))[1].getClass(), String.class);
		Assert.assertEquals("type of value at row 1, column 2", ((Object[]) result.get(1))[2].getClass(), BigDecimal.class);

		Assert.assertEquals("value at row 0, column 0", ((Object[]) result.get(0))[0], 11);
		Assert.assertEquals("value at row 0, column 1", ((Object[]) result.get(0))[1], "12");
		Assert.assertEquals("value at row 0, column 2", ((Object[]) result.get(0))[2], BigDecimal.valueOf(13.0));
		Assert.assertEquals("value at row 1, column 0", ((Object[]) result.get(1))[0], 101);
		Assert.assertEquals("value at row 1, column 1", ((Object[]) result.get(1))[1], "102");
		Assert.assertEquals("value at row 1, column 2", ((Object[]) result.get(1))[2], BigDecimal.valueOf(103.0));
	}

	@Test
	public void t4_create() {
		log.debug("t4_create");

		Foo f0 = new Foo("foo");
		fooDao.save(f0);
		int count = fooDao.getCount();
		Assert.assertEquals(1, count);

		Foo f1 = fooDao.get("foo");
		Assert.assertEquals(f1.getUid(), "foo");
		Assert.assertEquals(f1, f0);

		f1.setUid("new name");
		fooDao.merge(f1);

		Foo f2 = fooDao.get("new name");
		Assert.assertEquals(f2.getUid(), "new name");
		Assert.assertEquals(f2.getName(), "new name");
		Assert.assertEquals(f2, f1);
	}

	@Test
	public void t5_deleteAllAgain() {
		log.debug("t5_deleteAllAgain");

		fooDao.deleteAll();
		int count = fooDao.getCount();
		Assert.assertEquals(0, count);
	}

	private void show(List<?> result) {
		for (Object item : result)
			if (item != null && item.getClass().isArray())
				log.debug("{}", Arrays.deepToString((Object[]) item));
			else
				log.debug("{}", item);

	}
}
