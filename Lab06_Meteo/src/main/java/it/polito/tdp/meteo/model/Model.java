package it.polito.tdp.meteo.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.meteo.DAO.MeteoDAO;

public class Model {
	
	private final static int COST = 100;
	private final static int NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN = 3;
	private final static int NUMERO_GIORNI_CITTA_MAX = 6;
	private final static int NUMERO_GIORNI_TOTALI = 15;
	private int mese;
	private int bestCosto;
	private List<Citta> bestSoluzione;
	
	private MeteoDAO mdao;

	public Model() {
		mdao=new MeteoDAO();
		mese=0;
		bestCosto=0;
		bestSoluzione=null;
		
	}

	// of course you can change the String output with what you think works best
	public String getUmiditaMedia(int mese) {
		String s="";
		Set<Citta> cities=new HashSet<>(mdao.getCitta());
		for(Citta c:cities) {
			List<Rilevamento> rilevamenti=new ArrayList<>(mdao.getAllRilevamentiLocalitaMese(mese, c.getNome()));
			s+="Umidita media nel mese "+mese+" nella citta' di "+c.getNome()+": "+calcolaUmiditaMedia(rilevamenti)+" %\n";
		}
		return s;
	}
	
	public int calcolaUmiditaMedia(List<Rilevamento> rilevamenti) {
		int tot=0;
		int contatore=0;
		for(Rilevamento r: rilevamenti) {
			tot=tot+r.getUmidita();
			contatore++;
		}
		return (int)(tot/contatore);
	}
	
	// of course you can change the String output with what you think works best
	public List<Citta> trovaSequenza(int mese) {
		this.mese=mese;
		List<Citta> parziale=new ArrayList<>();
		Set<Citta> disponibili=new HashSet<>(mdao.getCitta());
		int livello=1;
		cercaCostoOttimo(parziale,livello,disponibili);
		return bestSoluzione;
	}
	
	private void cercaCostoOttimo(List<Citta> parziale, int livello, Set<Citta> disponibili) {
		
		if(livello==NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN+1) {
			if(!controllaPrimi3giorni(parziale)) {
				return;
			}
		}
		
		if(livello==NUMERO_GIORNI_TOTALI) {
			if(!controlloMax6giorni(parziale,disponibili))
				return;
			if(calcolaCosto(parziale)>bestCosto) {
				bestSoluzione=new ArrayList<>(parziale);
				bestCosto=calcolaCosto(parziale);
			}
		}
		
		for(Citta c:disponibili) {
			parziale.add(c);
			cercaCostoOttimo(parziale, livello+1, disponibili);
			parziale.remove(c);
		}
	}
	
	public int calcolaCosto(List<Citta> parziale) {
		int costo=0;
		for(int i=0; i<parziale.size();i++) {
			if(i!=0 && !parziale.get(i).equals(parziale.get(i-1))) {
				costo+=COST;
			}
			costo+=mdao.getUmiditaByGiornoLocalita(i+1, mese, parziale.get(i).getNome());
		}
		return costo;
	}
	
	public boolean controllaPrimi3giorni(List<Citta> parziale) {
		Citta c=new Citta(parziale.get(0).getNome());
		for(int i=1;i<NUMERO_GIORNI_CITTA_CONSECUTIVI_MIN;i++) {
			if(!c.equals(parziale.get(i))) {
				return false;
			}
		}
		return true;	
	}
	
	public boolean controlloMax6giorni(List<Citta> parziale, Set<Citta> disponibili) {
		for(Citta c:disponibili) {
			int contatore=0;
			for(Citta ci:parziale) {
				if(ci.equals(c)) {
					contatore++;
				}
				if(contatore>NUMERO_GIORNI_CITTA_MAX) {
					return false;
				}
			}
		}
		return true;
	}
	

}
