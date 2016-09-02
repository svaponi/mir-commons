package it.miriade.commons.web.resources;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

/**
 * Base class per tutte le classi Resources, protocollo HATEOAS. Nello specifico tali classi incapsulano le informazioni
 * da veicolare al client e dunque poi trasformati in JSON dall'infrastruttura di Spring MVC.
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:08:10 PM
 */
public class BaseResource<PK extends Serializable> extends ResourceSupport implements Serializable {

	private static final long serialVersionUID = 1L;

	protected PK uid;

	public BaseResource() {
		super();
	}

	public BaseResource(BaseResource<PK> dto) {
		super();
		this.uid = dto.getUid();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "#" + uid;
	}

	/**
	 * Metodo clone usato nui controller RESTful per instanziare nuovi oggetti equivalenti
	 */
	@Override
	public BaseResource<PK> clone() throws CloneNotSupportedException {
		try {
			return new BaseResource<PK>(this);
		} catch (Exception e) {
			BaseResource<PK> clone = new BaseResource<PK>();
			clone.setUid(this.uid);
			return clone;
		}
	}

	/**
	 * Metodo clone usato nui controller RESTful per instanziare nuovi oggetti equivalenti
	 */
	public BaseResource<PK> clone2() throws CloneNotSupportedException {
		BaseResource<PK> clone = new BaseResource<PK>(this);
		return clone;
	}

	public PK getUid() {
		return uid;
	}

	public void setUid(PK uid) {
		this.uid = uid;
	}

	/**
	 * Controlla se il valore passato è non null e non vuoto.
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isValid(String value) {
		return value == null || "".equals(value);
	}

	/**
	 * Controlla se il valore passato è valido (non null e non vuoto) e, in caso contrario, ritorna un valore passato
	 * come secondo parametro
	 * Metodo pensato per controllare i valori ritornati dai GETTERs nelle Resources.
	 * 
	 * @param value
	 * @return
	 */
	public static String replaceNotValid(String value, String unknown) {
		return value == null || "".equals(value) ? unknown : value;
	}
}