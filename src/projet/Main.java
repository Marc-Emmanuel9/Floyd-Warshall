package projet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

import projet.exception.EmptyFileException;
import projet.exception.NonCompletGrapheException;


public class Main {

	public static void main(String[] args) {
		
		Scanner scc = new Scanner(System.in);
		String rep;
		String folderWay;
		String folderName = "";
		
		try {
			System.out.println("I = Infinity");
			System.out.println("Veuiller entrer le chemin d'accès au "
					+ "fichier contenant les graphes");
			System.out.println();
			folderWay = scc.nextLine();
			folderWay += "\\";
			do {
				//Choix du graphe
				System.out.println("Graphe présent dans le fichier selectionné : ");
				System.out.println(afficheGraphe(new File(folderWay)));
				do{
					System.out.println("Veuiller entrer le nom du fichier à lire (sans l'extension) : ");
					folderName = scc.nextLine();
				}while(!getRepertoire(new File(folderWay)).contains(folderName));
				
				
				//Enregistrement du graphe dans une structure de donnée
				FileInputStream file = new FileInputStream(folderWay + folderName + ".txt");
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
				System.out.println("Voulez-vous travailler sur un autre graphe ? (N/O)");
				rep = scc.nextLine();
			}while(rep != "N");
		} catch (FileNotFoundException | EmptyFileException | NonCompletGrapheException e) {
			System.out.println(e.getMessage());
		}
		
		scc.close();
	}
	
	public static double[][] algoFloydWarshall(Graphe graphe) {
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

	public static String afficherFloyd(double[][] matrice) {
		String str = "";
		for(double[] tab: matrice) {
			for(double elt: tab) {
				str += elt + "\t";
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
			if(rep.endsWith(".txt") && rep.startsWith("Graphe")) {
				toString += "\t"+ i+". " +rep + "\n";
				i++;
			}
		}
		return toString;
		
	}
	
	public static List<String> getRepertoire(final File file) {
		List<String> toString = new ArrayList<>();
		for(String rep: file.list()) {
			if(rep.endsWith(".txt") && rep.startsWith("Graphe")) toString.add(rep);
		}
		return toString;	
	}
}