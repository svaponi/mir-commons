package it.miriade.commons.web.resources;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Base class per tutte le classi Resources, protocollo HATEOAS. Nello specifico tali classi incapsulano le informazioni
 * da veicolare al client e dunque poi trasformati in JSON dall'infrastruttura di Spring MVC.
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:08:10 PM
 */
public class BaseResourceJsonIgnoreGetters<PK extends Serializable> extends BaseResource<PK> {

	private static final long serialVersionUID = 1L;

	public BaseResourceJsonIgnoreGetters() {
		super();
	}

	public BaseResourceJsonIgnoreGetters(BaseResource<PK> dto) {
		super(dto);
	}

	@Override
	@JsonIgnore
	public List<Link> getLinks() {
		return super.getLinks();
	}

	@Override
	@JsonIgnore
	public PK getUid() {
		return uid;
	}

}