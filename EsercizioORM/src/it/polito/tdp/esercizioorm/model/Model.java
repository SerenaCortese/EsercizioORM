package it.polito.tdp.esercizioorm.model;

import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.esercizioorm.dao.CorsoDAO;
import it.polito.tdp.esercizioorm.dao.StudenteDAO;

public class Model {

	
	private CorsoDAO cdao;
	private StudenteDAO sdao;
	
	private List<Corso> corsi;
	private List<Studente> studenti;
	
	private CorsoIdMap corsoMap;
	private StudenteIdMap studenteMap;
	
	public Model(){
		
		cdao = new CorsoDAO();
		sdao = new StudenteDAO();
		
		corsoMap = new CorsoIdMap();
		studenteMap = new StudenteIdMap();
		
		//porto tutto il database in memoria:
		
		//1) info sui corsi
		this.corsi = cdao.getTuttiCorsi(corsoMap); //così non creo oggetti duplicati
		System.out.println(corsi.size());
		
		//2) info sugli studenti
		this.studenti = sdao.getTuttiStudenti(studenteMap);
		System.out.println(studenti.size());
		
		//3) info sulle relazioni corsi-studenti in modo bidirezionale
		for(Studente s: studenti) {
			//aggiungo riferimenti incrociati: aggiungo corsi agli studenti
			cdao.getCorsiFromStudente(s,corsoMap);
		}
		
		for(Corso c : corsi) {
			sdao.getStudentiFromCorso(c,studenteMap);
		}
	}
	

	public List<Studente> getStudentiFromCorso(String codins) {
		Corso c = corsoMap.get(codins);
		//controllo che studente inserisca codice di un corso esistente
		if (c == null) {
			return new ArrayList<Studente>(); 
			//ritorna lista vuota, così non crasha il testModel e non stampa niente
		}
		
		return c.getStudenti();
	}
	
	public List<Corso> getCorsiFromStudente(int matricola) {
		Studente s = studenteMap.get(matricola);
		
		if (s == null) {
			return new ArrayList<Corso>();
		}
		
		return s.getCorsi();		
	}
	
	public int getTotCreditiFromStudente(int matricola) {
		//mi basta sapere la lista dei corsi per ciascuno studente
		//non devo fare query, è tutto in memoria
		int sum = 0;
		
		for (Studente s : studenti) {
			if (s.getMatricola() == matricola) {
				for (Corso c : s.getCorsi()) {
					sum += c.getCrediti();
				}
				return sum;
			}
		}
		
		return -1; //se non trovo studente
	}

	public boolean iscriviStudenteACorso(int matricola, String codins) {
		
		Studente studente = studenteMap.get(matricola);
		Corso corso = corsoMap.get(codins);
		
		if (studente == null || corso == null) {//se uno dei due oggetti non esiste
			// non posso iscrivere uno studente ad un corso
			return false;
		}
		
		//1) Aggiorno il DB per primo perché potrebbe dare problemi
		boolean result = sdao.iscriviStudenteACorso(studente, corso);
								//model passa oggetti completi al DAO perché model non sa come vengono salvati nel database (solo matricola o completo)
		if (result) {
			// aggiornamento db effettuato con successo
			
			//2) Aggiorno i riferimenti in memoria RAM usata nel ciclo di vita del programma
			if (!studente.getCorsi().contains(corso)) {
				studente.getCorsi().add(corso);
			}
			if (! corso.getStudenti().contains(studente)) {
				corso.getStudenti().add(studente);
			}
			return true;
		}
		
		return false;
	}

	/**
	 * Questo metodo viene utilizzato solo per testare le performance di ConnectDBCP.
	 */
	public void testCP() {
		double avgTime = 0;
		for (int i = 0; i < 10; i++) {
			long start = System.nanoTime();
			List<Studente> studenti = sdao.getTuttiStudenti(new StudenteIdMap());
			for (Studente s : studenti) {
				sdao.studenteIscrittoACorso(s.getMatricola(), "01NBAPG");
			}
			double tt = (System.nanoTime() - start) / 10e9;
			System.out.println(tt);
			avgTime += tt;
		}
		System.out.println("AvgTime (mean on 10 loops): " + avgTime/10);
	}
	

}
