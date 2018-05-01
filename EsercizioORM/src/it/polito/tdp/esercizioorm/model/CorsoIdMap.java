package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;
/**
 * Voglio creare un'identity map di Corso ovvero un wrapper di mappa
 * quindi devo implementare i metodi get e put tipici delle mappe.
 * @author Serena
 *
 */
public class CorsoIdMap {
	
	private Map<String,Corso> map;
	
	public CorsoIdMap(){
		
		this.map = new HashMap<>();
	}
	
	public Corso get(String codins) {
		return map.get(codins);
	}
	
	/**
	 * Prende il corso cercato nella IdentityMap e se non c'è lo inserisce nella IdentityMap prima di ritornarlo.
	 * @param corso è l'oggetto di tipo Corso che si vuole ottenere
	 * @return Oggetto di tipo Corso: il corso cercato
	 */
	public Corso get(Corso corso) {
		Corso old = map.get(corso.getCodIns());
		if(old==null) {
			//nella mappa non c'è questo corso => LO AGGIUNGO
			map.put(corso.getCodIns(), corso);
			return corso;
		}
		
		//avevo già inserito quell'oggetto
		return old;
	}
	
	public void put(String codIns, Corso corso) {
		map.put(codIns, corso);
	}

}
