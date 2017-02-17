package it.miriade.commons.dbutils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.miriade.commons.dbutils.entities.Test01;
import it.miriade.commons.skipintegration.SkipIntegrationTestsRule;
import it.miriade.commons.skipintegration.SkipIntegrationTestsRule.SkipIntegrationTests;

/**
 * TODO Unico modo per testare il get di un {@link Date} è formattarlo e
 * trasformarlo in String, altrimenti fallisce (anche in long, con il getTime())
 * 
 * @author svaponi
 * @created Jul 21, 2016 12:52:57 PM
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(Constants.CONTEXT_XML_FILE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DbQueryTest {

	static final Logger _log = LoggerFactory.getLogger(DbQueryTest.class);
	static final double _delta = 0.01;
	static final boolean _dropAllAtTheEnd = false;

	private static final SimpleDateFormat iso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
	private static final SimpleDateFormat ts = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS"); // "yyyyMMdd
																								// HH:mm:ss.SSS"
	private static final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");

	final int NUM_OF_COLUMNS = 5;
	final Date C0 = new Date();
	final String C1 = "foo";
	final int C2 = 1;
	final long C3 = Long.MAX_VALUE;
	final double C4 = 0.23;
	final int BATCH_LENGTH = 5;

	static DbQuery _dbq;

	private Logger log = LoggerFactory.getLogger(DbQueryTest.class);

	@Autowired
	private DbQuery dbq;

	@Rule
	public SkipIntegrationTestsRule rule = new SkipIntegrationTestsRule();

	@PostConstruct
	public void init() {
		_dbq = dbq;
	}

	@Test
	public void test00_prerequisiti() {
		log.debug("@test00_prerequisiti");

		// Test object not null
		Assert.assertNotNull("DbQuery should be not null", dbq);
		Assert.assertNotNull("static DbQuery should be not null", _dbq);

		dbq.update("DROP TABLE IF EXISTS test01");
		dbq.update("DROP TABLE IF EXISTS test05");
	}

	@Test
	@SkipIntegrationTests
	public void test01_createTable() {
		log.debug("test01_createTable()");
		dbq.update("CREATE TABLE test01 (C0 timestamp, C1 varchar, C2 integer, C3 bigint, C4 double precision)");

		try {
			dbq.select("SELECT * FROM test01");
		} catch (Exception e) {
			// Se genera eccezione la tabella non è stata creata
			Assert.assertNotNull("Table no created", null);
		}

		dbq.update("INSERT INTO test01 SELECT to_timestamp('" + ts.format(C0) + "','YYYYMMDD HH24:MI:SS.MS') AS C0, '"
				+ C1 + "' AS C1, " + C2 + " AS C2, " + C3 + " AS C3, " + C4 + " AS C4");
	}

	@Test
	@SkipIntegrationTests
	public void test02_querySelect() {
		log.debug("test02_queryTest()");
		DbResult result = dbq.select("SELECT C0, C1, C2, C3, C4 FROM test01");

		// Test result is not null
		Assert.assertNotNull("DbResult should be not null", result);

		// Test number rows
		Assert.assertEquals("DbResult should have 1 row", 1, result.size());

		// Test number of columns
		Assert.assertEquals("DbResult should have " + NUM_OF_COLUMNS + " columns", NUM_OF_COLUMNS,
				result.columns.size());

		// Test column names
		for (int i = 0; i < NUM_OF_COLUMNS; i++)
			Assert.assertEquals(i + "° column name should be C" + i, "C" + i, result.columns.get(i));

		// Test values
		String expected = iso.format(C0);
		String actual = iso.format((Date) result.get(0, "C0"));
		Assert.assertEquals("Column C0 should be...", expected, actual);
		Assert.assertEquals("Column C1 should be...", C1, result.get(0, "C1"));
		Assert.assertEquals("Column C2 should be...", C2, result.get(0, "C2"));
		Assert.assertEquals("Column C3 should be...", C3, result.get(0, "C3"));
		Assert.assertEquals("Column C4 should be...", (double) C4, (double) result.get(0, "C4"), _delta);
	}

	@Test
	@SkipIntegrationTests
	public void test03_queryDelete() {
		log.debug("test03_queryDelete()");
		int n = dbq.update("DELETE FROM test01");

		// Test number of deleted rows
		Assert.assertEquals("Delete query should have affected 1 row", 1, n);
	}

	@Test
	@SkipIntegrationTests
	public void test04_queryInsert() {
		log.debug("test04_queryInsert()");
		dbq.update("INSERT INTO test01 (C0, C1, C2, C3, C4) VALUES (?, ?, ?, ?, ?), (?, ?, ?, ?, ?)", C0, C1, C2, C3,
				C4, addDays(C0, 1), C1 + "1", C2 + 1, C3 - 1, C4 + 1);
	}

	@Test
	@SkipIntegrationTests
	public void test04_queryTest() {
		log.debug("test04_queryTest()");
		// ordino in modo che la prima riga sia la prima inserita
		DbResult result = dbq.select("SELECT * FROM test01 ORDER BY C0");

		// Test number of rows
		Assert.assertEquals("DbResult should have 2 rows", 2, result.size());

		String expected = date.format(C0);
		String actual = date.format((Date) result.get(0, "C0"));

		// Test 1st row values
		Assert.assertEquals("Column C0 should be...", expected, actual);
		Assert.assertEquals("Column C1 should be...", C1, result.get(0, "C1"));
		Assert.assertEquals("Column C2 should be...", C2, result.get(0, "C2"));
		Assert.assertEquals("Column C3 should be...", C3, result.get(0, "C3"));
		Assert.assertEquals("Column C4 should be...", C4, result.get(0, "C4"));

		expected = date.format(addDays(C0, 1));
		actual = date.format((Date) result.get(1, "C0"));

		// Test 2nd row values
		Assert.assertEquals("Column C0 should be...", expected, actual);
		Assert.assertEquals("Column C1 should be...", C1 + "1", result.get(1, "C1"));
		Assert.assertEquals("Column C2 should be...", C2 + 1, result.get(1, "C2"));
		Assert.assertEquals("Column C3 should be...", C3 - 1, result.get(1, "C3"));
		Assert.assertEquals("Column C4 should be...", C4 + 1, result.get(1, "C4"));
	}

	@Test
	@SkipIntegrationTests
	public void test05_createBatch() {
		log.debug("test05_createBatch()");
		List<String> queries = new ArrayList<String>();

		queries.add("CREATE TABLE test05 (C0 timestamp, C1 varchar, C2 integer, C3 bigint, C4 double precision)");
		for (int i = 0; i < BATCH_LENGTH; i++) {
			Date d = addDays(C0, i);
			queries.add("INSERT INTO test05 (C0, C1, C2, C3, C4) SELECT to_timestamp('" + ts.format(d)
					+ "','YYYYMMDD HH24:MI:SS.MS'), '" + (C1 + i) + "', " + (C2 + i) + ", " + (C3 - i) + ", " + (C4 + i)
					+ "");
		}

		dbq.batch(queries);

		DbResult result = dbq.select("SELECT * FROM test05");
		for (int i = 0; i < BATCH_LENGTH; i++)
			try {
				Date d = addDays(C0, i);
				String expected = date.format(d);
				String actual = date.format((Date) result.get(i, "C0"));

				// Test 1st row values
				Assert.assertEquals("Column C0 should be...", expected, actual);
				Assert.assertEquals("Column C1 should be...", C1 + i, result.get(i, "C1"));
				Assert.assertEquals("Column C2 should be...", C2 + i, result.get(i, "C2"));
				Assert.assertEquals("Column C3 should be...", C3 - i, result.get(i, "C3"));
				Assert.assertEquals("Column C4 should be...", (double) C4 + i, (double) result.get(i, "C4"), _delta);

			} catch (Exception e) {
				// Se genera eccezione la tabella non è stata creata
				Assert.assertNotNull("Table test05 created", null);
			}
	}

	@Test
	@SkipIntegrationTests
	public void test06_getEntityList() {
		log.debug("test06_getEntityList()");
		List<Test01> entities = dbq.getEntityList("SELECT * FROM test01 ORDER BY C0", Test01.class);

		log.debug("List: [{}]", StringUtils.join(entities.toArray(), ","));

		// Test number of rows
		Assert.assertEquals("EntityList should have 2 items", 2, entities.size());

		String expected = date.format(C0);
		String actual = date.format((Date) entities.get(0).getC0());

		// Test 1st row values
		Assert.assertEquals("Column C0 should be...", expected, actual);
		Assert.assertEquals("Column C1 should be...", C1, entities.get(0).getC1());
		Assert.assertEquals("Column C2 should be...", C2, entities.get(0).getC2());
		Assert.assertEquals("Column C3 should be...", C3, entities.get(0).getC3());
		Assert.assertEquals("Column C4 should be...", C4, entities.get(0).getC4(), _delta);

		expected = date.format(addDays(C0, 1));
		actual = date.format((Date) entities.get(1).getC0());

		// Test 2nd row values
		Assert.assertEquals("Column C0 should be...", expected, actual);
		Assert.assertEquals("Column C1 should be...", C1 + "1", entities.get(1).getC1());
		Assert.assertEquals("Column C2 should be...", C2 + 1, entities.get(1).getC2());
		Assert.assertEquals("Column C3 should be...", C3 - 1, entities.get(1).getC3());
		Assert.assertEquals("Column C4 should be...", C4 + 1, entities.get(1).getC4(), _delta);
	}

	@Test
	@SkipIntegrationTests
	public void test99_dropAll() {
		if (!_dropAllAtTheEnd)
			return;

		_log.debug("test99_dropAll()");
		List<String> queries = new ArrayList<String>();

		queries.add("DROP TABLE IF EXISTS test01");
		queries.add("DROP TABLE IF EXISTS test05");

		_dbq.batch(queries);
	}

	private Date addDays(Date fromDate, int deltaDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(fromDate);
		cal.add(Calendar.DAY_OF_YEAR, deltaDays);
		return cal.getTime();
	}

}
