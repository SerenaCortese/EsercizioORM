package it.polito.tdp.esercizioorm.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.esercizioorm.model.Corso;
import it.polito.tdp.esercizioorm.model.Studente;
import it.polito.tdp.esercizioorm.model.StudenteIdMap;

public class StudenteDAO {

	public List<Studente> getTuttiStudenti(StudenteIdMap studentiMap)
	{

		String sql = "SELECT matricola, nome, cognome, cds FROM studente";

		List<Studente> result = new ArrayList<Studente>();

		try {
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Studente s = new Studente(res.getInt("matricola"),res.getString("nome"),res.getString("cognome"),res.getString("cds"));
				result.add(studentiMap.get(s));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return result;
	}
	
public void getStudentiFromCorso(Corso c, StudenteIdMap studenteMap) {
		
		String sql = "SELECT s.matricola, s.nome, s.cognome, s.cds from studente as s, iscrizione as i where s.matricola = i.matricola and i.codins = ?";
		
		try {
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, c.getCodIns());
			
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Studente s = new Studente(res.getInt("matricola"), res.getString("nome"), res.getString("cognome"),
						res.getString("cds"));
				c.getStudenti().add(studenteMap.get(s));
				//posso usare add perché la lista non sarà mai nulla visto che la creo nel costruttore di corso
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		
	}

	/**
	 * Data una matricola ed il codice insegnamento, iscrive lo studente al corso.
	 * @param studente da iscrivere al corso
	 * @param corso a cui si vuole iscrivere lo studente
	 * @return <code>true</code> se iscrizione andata a buon fine; <code>false</code> altrimenti
	 */
	public boolean iscriviStudenteACorso(Studente studente, Corso corso) {

		String sql = "INSERT IGNORE INTO `iscritticorsi`.`iscrizione` (`matricola`, `codins`) VALUES(?,?)";
		//IGNORE= ignora l'errore su inserimento duplicato, non inserisce due volte lo studente al corso e non manda errore
		boolean returnValue = false;
		
		try {
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, studente.getMatricola());
			st.setString(2, corso.getCodIns());
			
			int res = st.executeUpdate();	

			if (res == 1)
				returnValue = true;

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore Db");
		}
		
		return returnValue;
	}
	
	/** Questo metodo viene utilizzato solo per testare le performance di ConnectDBCP.
	 * Verifica he studente sia iscritto al corso
	 * @param matricola dello studente
	 * @param codins è il codice d'insegnamento del corso
	 * @return <code>true</code> se studente iscritto al corso; <code>false</code> altrimenti
	 */
	public boolean studenteIscrittoACorso(int matricola, String codins) {
		
		String sql = "Select matricola, codins from iscrizione where matricola = ? and codins = ?";
		boolean result = false;
		
		try {
			
			Connection conn = ConnectDBCP.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, matricola);
			st.setString(2, codins);
			ResultSet res = st.executeQuery();

			if (res.next()) {
				result = true;
			}

			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
