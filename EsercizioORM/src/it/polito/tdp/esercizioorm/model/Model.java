package it.polito.tdp.esercizioorm.model;

import java.util.List;

import it.polito.tdp.esercizioorm.dao.CorsoDAO;
import it.polito.tdp.esercizioorm.dao.StudenteDAO;

public class Model {

	
	private CorsoDAO cdao;
	private StudenteDAO sdao;
	private CorsoIdMap corsoMap;
	private List<Corso> corsi;
	private List<Studente> studenti;
	
	public Model(){
		cdao = new CorsoDAO();
		sdao = new StudenteDAO();
		
		corsoMap = new CorsoIdMap();
		
		this.corsi = cdao.getTuttiCorsi(corsoMap); //così non creo oggetti duplicati
		System.out.println(corsi.size());
		
		this.studenti = sdao.getTuttiStudenti();
		System.out.println(studenti.size());
		
		for(Studente s: studenti) {
			//aggiungo riferimenti crociati
			cdao.getCorsiFromStudente(s,corsoMap);
		}
	}
	
	// non creo classe IdMap per studente perché farò solo una chiamata a StuednteDAO per sapere tutti gli studenti
	
	public int getTotCreditiFromStudente(int matricola) {//mi basta sapere la lista dei corsi per ciascuno studente
		
		return 0;
	}

}
