package it.miriade.commons.dbutils.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Test01 implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat isoDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");

	private Date C0 = new Date();
	private String C1 = "foo";
	private int C2 = 1;
	private long C3 = Long.MAX_VALUE;
	private double C4 = 0.23;

	@Override
	public String toString() {
		return String.format("{\n  class: %s,\n  properties: {\n    C0: '%s',\n    C1: '%s',\n    C2: %s,\n    C3: %s,\n    C4: %s\n  }\n}", Test01.class.getName(), isoDate.format(C0), Objects.toString(C1, ""), C2, C3, C4);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Test01))
			return false;
		Test01 foo = (Test01) obj;
		return Objects.toString(C1, "").equals(foo.C1) && Integer.compare(C2, foo.C2) == 0 && Long.compare(C3, foo.C3) == 0 && Double.compare(C4, foo.C4) == 0;
	}

	public Date getC0() {
		return C0;
	}

	public void setC0(Date c0) {
		C0 = c0;
	}

	public String getC1() {
		return C1;
	}

	public void setC1(String c1) {
		C1 = c1;
	}

	public int getC2() {
		return C2;
	}

	public void setC2(int c2) {
		C2 = c2;
	}

	public long getC3() {
		return C3;
	}

	public void setC3(long c3) {
		C3 = c3;
	}

	public double getC4() {
		return C4;
	}

	public void setC4(double c4) {
		C4 = c4;
	}

}
