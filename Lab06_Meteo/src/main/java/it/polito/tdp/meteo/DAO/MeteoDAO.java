package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {

	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {

		final String sql = "SELECT Localita, DATA, Umidita FROM situazione WHERE MONTH(DATA)=? AND Localita=?";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setString(2, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Set<Citta> getCitta() {
		final String sql = "Select Localita from situazione";

		Set<Citta> citta = new HashSet<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Citta c = new Citta(rs.getString("Localita"));
				citta.add(c);
			}

			conn.close();
			return citta;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public int getUmiditaByGiornoLocalita(int giorno, int mese, String localita) {
		final String sql = "SELECT Localita, DATA, Umidita FROM situazione WHERE MONTH(DATA)=? AND DAY(DATA)=? AND Localita=?";

		Rilevamento r = null;
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			st.setInt(2, giorno);
			st.setString(3, localita);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
			}

			conn.close();

			return r.getUmidita();

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public List<Rilevamento> getRilevPrimi15GiorniMese(int mese){
		final String sql="SELECT Localita, DATA, Umidita FROM situazione WHERE MONTH(DATA)=?  AND DAY(DATA)<=15";
		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data"), rs.getInt("Umidita"));
				rilevamenti.add(r);
				
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
