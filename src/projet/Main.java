package projet;

import java.io.*;
import java.util.*;

import projet.exception.*;


public class Main {

	public static void main(String[] args) {
		
		try {
			scenario();
		} catch (FileNotFoundException | EmptyFileException | NonCompletGrapheException | UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void scenario() throws FileNotFoundException, EmptyFileException, NonCompletGrapheException, UnsupportedEncodingException {
		Scanner scc = new Scanner(System.in);
		int rep;
		String folderWay;
		int folderIndice;
		
		
		System.out.println("I = Infinity");
		System.out.println("Veuiller entrer le chemin d'accès au "
				+ "fichier contenant les graphes");
		System.out.println();
		folderWay = scc.nextLine();
		folderWay += "\\";
		do {
			List<String> repertoire = getRepertoire(new File(folderWay));
			List<String> trace = getTrace(new File(folderWay + "\\trace"));
			
			//Choix du graphe
			System.out.println("Graphe présent dans le fichier selectionné : ");
			System.out.println(afficheGraphe(new File(folderWay)));
			System.out.println("Veuiller entrer l'id du fichier à lire : ");
			folderIndice = scc.nextInt();
			
			
			//Enregistrement du graphe dans une structure de donnée
			FileInputStream file = new FileInputStream(folderWay + repertoire.get(folderIndice-1));
			Graphe graphe = new Graphe(file);
			
			//Affichage du graphe 
			System.out.println(graphe);
			
			//Floyd Warshall
			double[][] floyd = algoFloydWarshall(graphe);
			
			//Affichage si pas de circuit absorbant, sinon on reviens au début.
			if(!graphe.circuitAbsorbant()) {
				System.out.println("Matrice des plus courts chemin :");
				System.out.println(afficherFloyd(floyd));
				System.out.println();
			}else {
				System.out.println("Présence d'un circuit absorbant !!");
				System.out.println();
			}
			
			saveTrace(folderWay + "\\trace\\",  trace.get(folderIndice-1), graphe);
			System.out.println();
			System.out.println("Voulez-vous travailler sur un autre graphe ? (1/0)");
			rep = scc.nextInt();
			
		}while(rep != 0);
		System.out.println("Fin du programme !");
		scc.close();
	}
	public static double[][] algoFloydWarshall(final Graphe graphe) {
		double[][] matrice = graphe.matriceValeur();
		for (int k = 0; k < graphe.getNbSommet(); k++) {
			for (int i = 0; i < graphe.getNbSommet(); i++) {
				for (int j = 0; j < graphe.getNbSommet(); j++) {
					matrice[i][j] = Math.min(matrice[i][j], (matrice[i][k] + matrice[k][j]));
				}
			}
		}
		return matrice;
	}

	public static String afficherFloyd(final double[][] matrice) {
		String str = "";
		for(double[] tab: matrice) {
			for(double elt: tab) {
				if(elt == Double.POSITIVE_INFINITY) {
					str += "I\t";
				}else {
					str += elt + "\t";
				}
			}
			str += "\n";
		}
		return str;
	}
	
	public static String afficheGraphe(final File file) {
		String[] repertoire = file.list();
		String toString = "";
		int i = 1;
		for(String rep: repertoire) {
			if(rep.endsWith(".txt") && rep.contains("graphe")) {
				toString += "\t"+ i+". " +rep + "\n";
				i++;
			}
		}
		return toString;
		
	}
	
	public static List<String> getRepertoire(final File file) {
		List<String> toString = new ArrayList<>();
		for(String rep: file.list()) {
			if(rep.endsWith(".txt") && rep.contains("graphe")) toString.add(rep);
		}
		return toString;	
	}
	
	public static List<String> getTrace(final File file) {
		List<String> toString = new ArrayList<>();
		for(String rep: file.list()) {
			if(rep.endsWith(".txt") && rep.contains("trace")) toString.add(rep);
		}
		return toString;	
	}
	
	public static void saveTrace(final String completFolderWay, final String file, final Graphe graphe) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(completFolderWay + file, "UTF-8");
		String toString = graphe.toString();
		double[][] floyd = algoFloydWarshall(graphe);
		String floydToString = "Présence d'un circuit absorbant !!";
		System.out.println("Enregistrement de la trace dans le fichier : "+ "\n" + "\t-" +file);
		writer.println(toString);
		if(!graphe.circuitAbsorbant()) {
			floydToString = afficherFloyd(floyd);
			writer.println("Matrice obtenue après floyd :");
			writer.println(floydToString);
		}else {
			writer.println(floydToString);
		}
		//Enregistrement dans le fichier

		writer.close();
	}
}