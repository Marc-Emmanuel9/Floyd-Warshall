package projet;

import java.io.*;
import java.util.*;
import projet.exception.*;


public class Main {

	public static void main(String[] args) {
		
		try {
			scenario();
		} catch (FileNotFoundException | EmptyFileException | NonCompletGrapheException 
				| UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public static void scenario() 
			throws EmptyFileException, NonCompletGrapheException, 
			FileNotFoundException, UnsupportedEncodingException{
		
		Scanner scc = new Scanner(System.in);
		int rep;
		String folderWay;
		int folderIndice;
		
		//On demande � l'utilisateur de mettre l'adresse de son fichier graphe.
		System.out.println("I = Infinity");
		System.out.println("Veuiller entrer le chemin d'acc�s au "
				+ "fichier contenant les graphes");
		System.out.println();
		folderWay = scc.nextLine();
		folderWay += "\\";
		do {
			
			/*repertoire et graphe vont stocker le nom de tout les fichiers pr�sent dans le 
			 * fichier stockant les graphe ou les traces.
			*/
			List<String> repertoire = getRepertoire(new File(folderWay));
			List<String> trace = getTrace(new File(folderWay + "\\trace"));
			
			
			/*Choix du graphe en entrant un indice compris entre 1 et le nombre de graphe pr�sent 
			 * dans le fichier
			 */
			System.out.println("Graphe pr�sent dans le fichier selectionn� : ");
			System.out.println(afficheGraphe(new File(folderWay)));
			System.out.println("Veuiller entrer l'id du fichier � lire : ");
			folderIndice = scc.nextInt();
			System.out.println();
			System.out.println("Graphe s�lectionn� => " + repertoire.get(folderIndice-1));
			System.out.println();
			/*
			 * Maintenant que l'on connais le graphe � traiter on cr�e un objet FileInputStream qui 
			 * prend en param�tre l'adresse jusqu'au fichier choisie pr�c�dement.
			 */
			FileInputStream file = new FileInputStream(folderWay + repertoire.get(folderIndice-1));
			
			//On rentre donc en param�tre le file dans l'objet graphe
			Graphe graphe = new Graphe(file);
			
			//Affichage du graphe gr�ce � la red�finition de toString dans la classe Graphe.
			System.out.println(graphe);
			
			//On stocke la matrice obtenue apr�s applications de Floyd-Warshall
			double[][] floyd = algoFloydWarshall(graphe);
			
			//Affichage si pas de circuit absorbant, sinon on reviens au d�but.
			if(!graphe.circuitAbsorbant()) {
				System.out.println("Matrice obtenue apr�s floyd :");
				System.out.println();
				System.out.println(afficherFloyd(floyd));
				System.out.println();
			}else {
				System.out.println("Pr�sence d'un circuit absorbant !!");
				System.out.println();
			}
			
			
			/*
			 * On enregistre tout ce qui a �t� affich� dans la console dans un fichier.
			 */
			saveTrace(folderWay + "\\trace\\",  trace.get(folderIndice-1), graphe);
			System.out.println();
			
			/*
			 * On demande � l'utilisateur si il veut traiter un autre graphe.
			 */
			System.out.println("Voulez-vous travailler sur un autre graphe ? (1/0)");
			rep = scc.nextInt();
			
		}while(rep != 0);
		System.out.println("Fin du programme !");
		scc.close();
	}
	
	public static double[][] algoFloydWarshall(final Graphe graphe) {
		/*
		 * Algorithme de Floyd-Warshall
		 */
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
		int i = 0;
		str += "    ";
		for(int a = 0; a < matrice.length; a++) {
			str += a + "\t";
		}
		str += "\n\n";
		for(double[] tab: matrice) {
			str += i +" | ";
			for(double elt: tab) {
				if(elt == Double.POSITIVE_INFINITY) {
					str += "I\t";
				}else {
					str += (int) elt + "\t";
				}
			}
			str += "\n";
			i++;
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
	
	public static void saveTrace(final String completFolderWay, final String file, final Graphe graphe) 
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(completFolderWay + file, "UTF-8");
		String toString = graphe.toString();
		double[][] floyd = algoFloydWarshall(graphe);
		String floydToString = "Pr�sence d'un circuit absorbant !!";
		System.out.println("Enregistrement de la trace dans le fichier : "+ "\n" + "\t-" +file);
		writer.println(toString);
		if(!graphe.circuitAbsorbant()) {
			floydToString = afficherFloyd(floyd);
			writer.println("Matrice obtenue apr�s floyd :");
			writer.println(floydToString);
		}else {
			writer.println(floydToString);
		}
		writer.close();
	}
	
}