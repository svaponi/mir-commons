package it.miriade.commons.utils;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import it.miriade.commons.utils.DateHandler.Format;

public class DateHandlerTest {

	Date date_2010_01_03;
	Date date_2010_01_05;
	Date date_2010_01_17;
	Date date_2010_05_03;
	Date date_null;

	public DateHandlerTest() {
		super();
		this.date_2010_01_03 = new GregorianCalendar(2010, 0, 3).getTime(); // 3 gennaio 2010
		this.date_2010_01_05 = new GregorianCalendar(2010, 0, 5).getTime(); // 5 gennaio 2010
		this.date_2010_01_17 = new GregorianCalendar(2010, 0, 17).getTime(); // 17 gennaio 2010
		this.date_2010_05_03 = new GregorianCalendar(2010, 4, 3).getTime(); // 3 maggio 2010
	}

	@Test
	public void parseDateTest() {

		Assert.assertEquals("StringHandler.toString non torna mail NULL", date_2010_01_03, DateHandler.parseDate("2010-01-03", Format.DATE));
	}

	@Test
	public void formatDateTest() {

		// yyyy-MM-dd
		Assert.assertEquals("StringHandler.toString non torna mail NULL", DateHandler.NULL_DATE_TOSTRING, DateHandler.formatDate(date_null, Format.DATE));

		// yyyy-MM-dd
		Assert.assertEquals("StringHandler.toString non torna mail NULL", "2010-01-03", DateHandler.formatDate(date_2010_01_03, Format.DATE));

		// MM-dd-yyyy
		Assert.assertEquals("StringHandler.toString non torna mail NULL", "01-03-2010", DateHandler.formatDate(date_2010_01_03, Format.ENGLISH_DATE));

		// dd-MM-yyyy
		Assert.assertEquals("StringHandler.toString non torna mail NULL", "03-01-2010", DateHandler.formatDate(date_2010_01_03, Format.ITALIAN_DATE));

		// yyyyMMdd
		Assert.assertEquals("StringHandler.toString non torna mail NULL", "20100103", DateHandler.formatDate(date_2010_01_03, Format.CONDENSED_DATE));

		// yyyy-MM-dd'T'HH:mm'Z
		Assert.assertEquals("StringHandler.toString non torna mail NULL", "2010-01-03T00:00Z", DateHandler.formatDate(date_2010_01_03, Format.ISO_DATE));

		// yyyy-MM-dd HH:mm:ss.SSS
		Assert.assertEquals("StringHandler.toString non torna mail NULL", "2010-01-03 00:00:00.000", DateHandler.formatDate(date_2010_01_03, Format.TIMESTAMP));

		// yyyy-MM-dd-HH-mm-ss-SSS
		Assert.assertEquals("StringHandler.toString non torna mail NULL", "2010-01-03-00-00-00-000",
				DateHandler.formatDate(date_2010_01_03, Format.EASY_DECODE_TIMESTAMP));
	}

	@Test
	public void addTest() {
		// delta positivo
		Assert.assertEquals("StringHandler.toString non torna mail NULL", date_2010_01_05, DateHandler.addDays(date_2010_01_03, 2));
		Assert.assertEquals("StringHandler.toString non torna mail NULL", date_2010_01_17, DateHandler.addWeeks(date_2010_01_03, 2));
		Assert.assertEquals("StringHandler.toString non torna mail NULL", date_2010_05_03, DateHandler.addMonths(date_2010_01_03, 4));

		// delta negativo
		Assert.assertEquals("StringHandler.toString non torna mail NULL", date_2010_01_03, DateHandler.addDays(date_2010_01_05, -2));
		Assert.assertEquals("StringHandler.toString non torna mail NULL", date_2010_01_03, DateHandler.addWeeks(date_2010_01_17, -2));
		Assert.assertEquals("StringHandler.toString non torna mail NULL", date_2010_01_03, DateHandler.addMonths(date_2010_05_03, -4));
	}
}
