package it.miriade.commons.model.test;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import it.miriade.commons.model.dao.GenericDao;
import it.miriade.commons.model.test.entities.Foo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(Constants.CONTEXT_XML_FILE)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GenericDaoImplTests {

	final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("FooDao")
	GenericDao<Foo, String> fooDao;

	@Test
	public void test00_nullObjects() {
		Assert.assertNotNull("GenericDao<Foo, String> must be not null", fooDao);
	}

	@Test
	public void test01_deleteAll() {
		log.debug("TEST: deleteAll()");

		fooDao.deleteAll();
		int count = fooDao.getCount();
		Assert.assertEquals(0, count);
	}

	@Test
	public void test02_create() {
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
