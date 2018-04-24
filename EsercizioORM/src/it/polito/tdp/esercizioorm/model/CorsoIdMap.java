package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;
/**
 * Voglio creare un'identity map di corso ovvero un wrapper di mappa
 * quindi devo implementare i metodi get e put tipici delle mappe
 * @author sere9
 *
 */
public class CorsoIdMap {
	
	private Map<String,Corso> map;
	
	public CorsoIdMap(){
		this.map = new HashMap<>();
	}
	
	/**
	 * 
	 * @param corso Oggetto corso passato che se non trovo, creo.
	 * @return
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
