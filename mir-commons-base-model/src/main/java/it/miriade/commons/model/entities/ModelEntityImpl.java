package it.miriade.commons.model.entities;

import java.io.Serializable;

import org.apache.commons.lang3.builder.RecursiveToStringStyle;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Interfaccia base per tutte le entities della nostra gerarchia. In realtà l'unico vero scopo pratico di questa
 * interfaccia è di identificatore comune per tutte le entities, in modo da poter maneggiare e riconoscere tali oggetti
 * non ricorrendo all'uso della classe Object come base-class.
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM
 */
public class ModelEntityImpl<PK extends Serializable> implements ModelEntity<PK> {

	private static final long serialVersionUID = 1L;

	protected PK uid;

	public ModelEntityImpl() {
		super();
	}

	public ModelEntityImpl(ModelEntity<PK> entity) {
		super();
		this.uid = entity.getUid();
	}

	public PK getUid() {
		return uid;
	}

	public void setUid(PK uid) {
		this.uid = uid;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		ModelEntityImpl<PK> clone = new ModelEntityImpl<PK>();
		clone.setUid(this.uid);
		return super.clone();
	}

	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this, new RecursiveToStringStyle()).toString();
	}

}
