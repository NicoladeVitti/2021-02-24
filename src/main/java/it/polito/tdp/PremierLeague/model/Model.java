package it.polito.tdp.PremierLeague.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	PremierLeagueDAO dao;
	Graph<Player, DefaultWeightedEdge> grafo;
	Map<Integer, Player> idMap;
	
	public Model() {
		dao = new PremierLeagueDAO();
		idMap = new HashMap<>();
	}
	
	public List<Match> getAllMatch(){
		return dao.listAllMatches();
	}
	
	public void creaGrafo(Match m) {
		dao.listAllPlayers(idMap);
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//AGGIUNGO I VERTICI 
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, m));
		
		//AGGIUNGO GLI ARCHI 
		for(Adiacenza a : dao.getAdiacenze(idMap, m)) {
			if(a.getPeso() >= 0) {
				Graphs.addEdgeWithVertices(grafo, a.getP1(), a.getP2(), a.getPeso());
			}
			else {
				Graphs.addEdgeWithVertices(grafo, a.getP2(), a.getP1(), (-1)*a.getPeso());
			}
		}
		
	}
	
	public Integer getNumVertici(){
		return grafo.vertexSet().size();
	}
	
	public Integer getNumArchi() {
		return grafo.edgeSet().size();
	}
	
	public String getGiocatoreMigliore() {
		
		Player migliore = null;
		Double diffMax = 0.0;
		
		for(Player p : grafo.vertexSet()) {
			Double sommaEntranti = 0.0;
			for(DefaultWeightedEdge e : grafo.incomingEdgesOf(p)) {
				Double peso = grafo.getEdgeWeight(e);
				sommaEntranti += peso;
			}
			
			Double sommaUscenti = 0.0;
			for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(p)) {
				Double peso = grafo.getEdgeWeight(e);
				sommaUscenti += peso;
			}
			
			Double diff = (sommaUscenti - sommaEntranti);
			
			if(diff > diffMax) {
				diffMax = diff;
				migliore = p;
			}
		}
		
		String result = migliore.name+" "+diffMax;
		
		return result;
	}

	
	
	
	public void init(Integer n) {
		
	}

	public void simula() {
		// TODO Auto-generated method stub
		
	}


}
