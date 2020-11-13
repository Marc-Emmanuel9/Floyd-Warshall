package projet;

import java.util.*;

import projet.exception.EmptyFileException;
import projet.exception.NonCompletGrapheException;

import java.io.*;

public class Graphe implements Iterable<int[]>{
	
	private Scanner fichier;
	private List<int[]> graphe;
	private int nbSommet;
	private int nbArc;
	
	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException
	 * @throws EmptyFileException
	 * @throws NonCompletGrapheException
	 */
	public Graphe(final FileInputStream file) throws FileNotFoundException, 
										EmptyFileException, NonCompletGrapheException {
		
		this.graphe = new ArrayList<>();
		
		/*
		 * On va stocké le graphe rentré en paramètre en mémoire : 
		 * 		- nbSommet contient le nombre de sommet
		 * 		- nbArc contient le nombre d'arc
		 * 		- graphe contient Extrémité initiale, suivie de l’extrémité terminale, 
		 * 		  suivie de la valeur l’arc : [Extrémité initiale, Extrémité final, Valeur de l'arc].
		 */
		
		this.fichier = new Scanner(file);
		//Lecture du fichier et enregistrement dans l'ArrayList graphe
		if(this.fichier.hasNextLine()) {
			int ligne = 1;
			while(this.fichier.hasNextLine()) {
				if(ligne == 1) 
					this.nbSommet = Integer.parseInt(this.fichier.nextLine());
				else if(ligne == 2)
					this.nbArc = Integer.parseInt(this.fichier.nextLine());
				else
					this.graphe.add(stringTabToIntegerTab(this.fichier.nextLine().split(" ")));
				ligne++;
			}
			
			if(graphe.size() != nbArc) {
				graphe.clear();
				throw new NonCompletGrapheException("Le graphe fournis n'est pas complet!");
			}
		}else throw new EmptyFileException("Aucun graphe n'est présent !");
		this.fichier.close();
		
	}
	
	/**
	 * 
	 * @param stringTab
	 * @return integerTab
	 */
	public int[] stringTabToIntegerTab(String[] stringTab) {
		/*
		 * La fonction prend en paramètre un tableau de String constitué de chiffre et renvoir un tableau
		 * de int constitué de ces même int.
		 */
		int[] integerTab = new int[stringTab.length];
		for(int y = 0; y < stringTab.length; y++) {
			integerTab[y] = Integer.parseInt(stringTab[y]);
			
		}
		return integerTab;
	}
	
	public int getPoids(final int sommet, final int successeur) {
		int poids = 0;
		for(int[] s: this) {
			if(s[0] == sommet && s[1] == successeur) {
				poids = s[2];
			}
		}
		return poids;
	}
	
	/**
	 * @return int[][]: la matrice d'adjacence du graphe
	 */
	
	public int[][] matriceAdjacent() {
		/*
		 * On crée la matrice d'adjacence, si aucune valeur est attribuer au tableau 2D la 
		 * valeur 0 est donner par défaut.
		 */
		int[][] matriceAdj = new int[this.nbSommet][this.nbSommet];
		
		for(int[] i: this) {
			matriceAdj[i[0]][i[1]] = 1;
		}
		return matriceAdj;
	}
	
	/**
	 * @return int[][]: la matrice de valeur du graphe
	 */
	public double[][] matriceValeur() {
		/*
		 * On crée la matrice d'adjacence, si aucune valeur est attribuer au tableau 2D la 
		 * valeur Infinity est donner par défaut.
		 */
		double[][] matriceVal = new double[this.nbSommet][this.nbSommet]; 
		
		for (int i = 0; i < matriceVal.length; i++) {
			for (int j = 0; j < matriceVal.length; j++) {
				matriceVal[i][j] = Double.POSITIVE_INFINITY;
				if(i == j) {
					matriceVal[i][j] = 0;
				}
			}
		}
		
		for(int[] i: this) {
			matriceVal[i[0]][i[1]] = i[2];
		}
		return matriceVal;
	}
	
	/**
	 * 
	 * @param s
	 * @return boolean 
	 */
	public boolean bellmanFord(final int s) {
		/*
		 *  Ici on utilise bellmanFord uniquement pour détecter les circuit absorbant
		 *  si : d[t[0]] > d[t[1]] + this.getPoids(t[0], t[1]) alors le graphe possède un circuit absorbant
		 */
		
		//On remplie le tableau d avec 0 pour le sommet s et infinity pour les autre.
		double[] d = new double[this.nbSommet];
		for(int[] sommet: this) {
			d[sommet[0]] = Double.POSITIVE_INFINITY;
		}
		d[s] = 0;
		
		//On va calculer toute les valeurs.
		for (int k = 1; k < this.nbSommet -1; k++) {
			for(int[] t: this) {
				d[t[1]] = Math.min(d[t[1]], (d[t[0]] + this.getPoids(t[0], t[1])));
			}
		}
		
		// Detection des circuit absorbant.
		for(int[] t: this) { 
			if(d[t[1]] > d[t[0]] + this.getPoids(t[0], t[1])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return boolean: true si le graphe possede un circuit absorbant, false sinon
	 */
	public boolean circuitAbsorbant() {
		/*
		 * Pour savoir si un graphe possède un circuit absorbant on va appliquer Bellman-Ford pour chaque 
		 * sommet du graphe.
		 */
		boolean rep;
		
		for (int i = 0; i < this.nbSommet; i++) {
			rep = bellmanFord(i);
			if (rep) {
				return rep;
			}
		}
		return false;
	}
	
	/**
	 * @return the nbSommet
	 */
	public int getNbSommet() {
		return nbSommet;
	}
	
	
	@Override
	public String toString() {
		/*
		 * Quand on sysout le graphe on retournera sa matrice d'adjacence et de valeur. 
		 * On a choisie de retourner la matrice adjacente et de valeur en même temps, l'une 
		 * à côté de l'autre.
		 */
		String toString = "Matrice Adjacente :\n\n";
		int i = 0;
		toString += "    ";
		for(int a = 0; a < this.nbSommet; a++) {
			toString += a + "\t";
		}
		toString += "\n\n";
		for(int[] matrice: matriceAdjacent()) {
			toString += i+" | ";
			for(int element: matrice) {
				toString += element + "\t";
			}
			toString += "\n";
			i++;
		}
		
		toString += "\nMatrice de Valeur :\n\n";
		i = 0;
		toString += "    ";
		for(int a = 0; a < this.nbSommet; a++) {
			toString += a + "\t";
		}
		toString += "\n\n";
		for(double[] matrice: matriceValeur()) {
			toString += i+" | ";
			for(double element: matrice) {
				if(element == Double.POSITIVE_INFINITY) {
					toString += "I\t";
				}else {
					toString += (int) element + "\t";
				}
			}
			toString += "\n";
			i++;
		}
		return toString;
	}
	
	@Override
	public Iterator<int[]> iterator() {
		return this.graphe.iterator();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fichier == null) ? 0 : fichier.hashCode());
		result = prime * result + ((graphe == null) ? 0 : graphe.hashCode());
		result = prime * result + nbArc;
		result = prime * result + nbSommet;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Graphe other = (Graphe) obj;
		if (fichier == null) {
			if (other.fichier != null)
				return false;
		} else if (!fichier.equals(other.fichier))
			return false;
		if (graphe == null) {
			if (other.graphe != null)
				return false;
		} else if (!graphe.equals(other.graphe))
			return false;
		if (nbArc != other.nbArc)
			return false;
		if (nbSommet != other.nbSommet)
			return false;
		return true;
	}
	
	

}
