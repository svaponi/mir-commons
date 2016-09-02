package it.miriade.commons.model.entities;

import java.io.Serializable;

/**
 * Interfaccia base per tutte le entities della nostra gerarchia. In realtà l'unico vero scopo pratico di questa
 * interfaccia è di identificatore comune per tutte le entities, in modo da poter maneggiare e riconoscere tali oggetti
 * non ricorrendo all'uso della classe Object come base-class.
 * 
 * @author svaponi
 * @created Mar 16, 2015 12:04:41 PM
 */
public interface ModelEntity<PK extends Serializable> extends Serializable {

	public PK getUid();

	public void setUid(PK uid);
}
