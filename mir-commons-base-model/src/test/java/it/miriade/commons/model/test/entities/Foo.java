package it.miriade.commons.model.test.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import it.miriade.commons.model.entities.ModelEntity;
import it.miriade.commons.model.test.Constants;
import it.miriade.commons.utils.StringHandler;

@Entity
@Table(name = Constants.FOO_TABLE, schema = Constants.DB_SCHEMA)
public class Foo implements ModelEntity<String> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "name", updatable = false)
	protected String name;

	@Column(name = "insert_date")
	@Temporal(TemporalType.DATE)
	protected Date insertDate;

	public Foo() {
		super();
		this.insertDate = new Date();
	}

	public Foo(String name) {
		super();
		this.name = name;
		this.insertDate = new Date();
	}

	@Override
	public String toString() {
		return StringHandler.toString(name) + "" + StringHandler.toString(insertDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Foo))
			return false;
		Foo foo = (Foo) obj;
		return StringHandler.toString(name).equals(foo.getName());
	}

	@Override
	public String getUid() {
		return name;
	}

	@Override
	public void setUid(String uid) {
		this.name = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
}
