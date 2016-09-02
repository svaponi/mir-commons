package it.miriade.commons.utils;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringHandlerTest {

	Logger log = LoggerFactory.getLogger(getClass());

	@Test
	public void toStringTest() {

		Assert.assertEquals("StringHandler.toString never returns NULL", "", StringHandler.toString(null));
		Assert.assertEquals("StringHandler.toString cuts result to maxLength", "1234", StringHandler.toString("123456789", 4));
	}

	@Test
	public void subTest() {

		Assert.assertEquals("StringHandler.sub never returns NULL", "", StringHandler.sub(null, 5, -1));
		Assert.assertEquals("StringHandler.sub never throws exception, even though args don't make sense", "", StringHandler.sub("123456789", 4, 2));
		Assert.assertEquals("StringHandler.sub never throws exception, even though args don't make sense", "789", StringHandler.sub("123456789", 6, 12));
	}

	@Test
	public void capitalizeTest() {

		Assert.assertEquals("StringHandler.capitalize works with lower-case", "All eat apples", StringHandler.capitalize("all eat apples"));
		Assert.assertEquals("StringHandler.capitalize works with upper-case", "All eat apples", StringHandler.capitalize("ALL EAT APPLES"));
		Assert.assertEquals("StringHandler.capitalizeAll works with lower-case", "All Eat Apples", StringHandler.capitalizeAll("all eat apples"));
		Assert.assertEquals("StringHandler.capitalizeAll works with upper-case", "All Eat Apples", StringHandler.capitalizeAll("ALL EAT APPLES"));
	}

	@Test
	public void toStringJsonTest() {

		Pojo pojo = null;
		Assert.assertNull("pojo is not null", pojo);
		String json = StringHandler.toStringJson(pojo);
		Assert.assertTrue("result is wrong", json.equals(StringHandler.NULL));

		Date d = new Date();
		pojo = new Pojo("foo", Integer.MAX_VALUE, Long.MAX_VALUE, d);
		Assert.assertNotNull("pojo is null", pojo);
		json = StringHandler.toStringJson(pojo);
		log.debug("json is {}", json);

		Assert.assertNotNull("result is null", json);
		Assert.assertTrue("result is null", !json.isEmpty());
		Assert.assertTrue("result is wrong", !json.equals(StringHandler.NULL));

		// Remove all useless chars an keep only "key:value,key:value,etc.." then split by ',' and ':'
		String[] arr = StringHandler.removeIdentation(json.replaceAll("\"|\\{|\\}", "")).replaceAll(" ", "").split(",|:");
		int i = 0;
		for (String a : arr)
			System.out.println(i++ + "|" + a);
		Assert.assertTrue("Number of components is wrong", arr.length > 6);
		Assert.assertEquals(StringHandler.ord(1) + "field is string", "string", arr[0].trim());
		Assert.assertEquals(StringHandler.ord(1) + "value is foo", "foo", arr[1].trim());
		Assert.assertEquals(StringHandler.ord(2) + "field is numint", "numint", arr[2].trim());
		Assert.assertEquals(StringHandler.ord(2) + "value is ...", Integer.MAX_VALUE, Integer.parseInt(arr[3].trim()));
		Assert.assertEquals(StringHandler.ord(3) + "field is numlong", "numlong", arr[4].trim());
		Assert.assertEquals(StringHandler.ord(3) + "value is ...", Long.MAX_VALUE, Long.parseLong(arr[5].trim()));
		Assert.assertEquals(StringHandler.ord(4) + "field is date", "date", arr[6].trim());
	}

	public class Pojo {
		String string;
		int numint;
		long numlong;
		Date date;

		public Pojo() {
		}

		public Pojo(String string, int numint, long numlong, Date date) {
			super();
			this.string = string;
			this.numint = numint;
			this.numlong = numlong;
			this.date = date;
		}

		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}

		public int getNumint() {
			return numint;
		}

		public void setNumint(int numint) {
			this.numint = numint;
		}

		public long getNumlong() {
			return numlong;
		}

		public void setNumlong(long numlong) {
			this.numlong = numlong;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}
	}
}
