package it.polito.tdp.esercizioorm.model;

import java.util.HashMap;
import java.util.Map;

public class StudenteIdMap {

	private Map<Integer, Studente> map;
	
	public StudenteIdMap() {
		map = new HashMap<>();
	}
	
	public Studente get(int matricola) {
		return map.get(matricola);
	}
	/**
	 * Prende lo studente cercato dalla IdentityMap e se non c'è lo inserisce nella IdentityMap prima di ritornarlo.
	 * @param studente è lo Studente che si vuole ottenere
	 * @return Oggetto di tipo Studente: lo studente cercato
	 */
	public Studente get(Studente studente) {
		//cerca nella mappa se esiste già studente associato a quella matricola
		Studente old = map.get(studente.getMatricola());
		if (old == null) {
			// nella mappa non c'è questo studente!
			map.put(studente.getMatricola(), studente);
			return studente;
		}
		return old;
	}
	
	public void put(Integer matricola, Studente studente) {
		map.put(matricola, studente);
	}

}
