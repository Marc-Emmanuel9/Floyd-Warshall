package projet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import projet.exception.EmptyFileException;
import projet.exception.NonCompletGrapheException;


public class Main {

	public static void main(String[] args) {
		try {
			System.out.println("I = Infinity");
			Scanner scc = new Scanner(System.in);
			String rep = "";
			String folderWay;
			System.out.println("Veuiller entrer le chemin d'accès au "
					+ "fichier contenant les graphes (finir par un backslash)");
			System.out.println();
			folderWay = scc.nextLine();
			scc.close();
			
			do {
				Scanner sc = new Scanner(System.in);
				String folderName;
				//Choix du graphe
				System.out.println("Veuiller entrer le nom du fichier à lire : ");
				//lllfolderName = sc.nextLine();
				
				FileInputStream file = new FileInputStream("C:\\Users\\marti\\Desktop\\Graphe.txt");
				Graphe graphe = new Graphe(file);
				
				//Affichage du graphe 
				System.out.println(graphe);
				
				//Floyd Warshall
				double[][] floyd = algoFloydWarshall(graphe);
				//Affichage si pas de circuit absorbant, sinon on reviens au début.
				if(!graphe.circuitAbsorbant()) {
					System.out.println("Matrice des plus courts chemin :");
					System.out.println(afficherFloyd(floyd));
					System.out.print("Voulez-vous travailler sur un autre graphe ? (N/O)");
					rep = sc.nextLine();
				}else {
					System.out.print("Voulez-vous travailler sur un autre graphe ? (N/O)");
					rep = sc.nextLine();
				}
				
				
			}while(rep != "N");

		} catch (FileNotFoundException | EmptyFileException | NonCompletGrapheException e) {
			System.out.println(e.getMessage());
		}
		

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
}