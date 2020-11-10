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
	 * @param file contenant un graphe
	 * @throws NonCompletGrapheException 
	 *
	 */
	public Graphe(final FileInputStream file) throws FileNotFoundException, 
										EmptyFileException, NonCompletGrapheException {
		this.graphe = new ArrayList<>();
		
		/*
		 * On va stocké le graphe rentré en paramètre en mémoire.
		 * nbSommet contient le nombre de sommet
		 * nbArc contient le nombre d'arc
		 * graphe contient Extrémité initiale, suivie de l’extrémité terminale, suivie de la valeur 
		 * de l’arc : [Extrémité initiale, Extrémité final, Valeur de l'arc].
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
	
	/**
	 * @return int[][]: la matrice d'adjacence du graphe
	 */
	
	public int[][] matriceAdjacent() {
		/*
		 * On crée la matrice d'adjacence, si aucune valeur est attribuer au tableau 2D la 
		 * valeur 0 est donner par défaut
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
		 * valeur 0 est donner par défaut
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
	
	@Override
	public String toString() {
		/*
		 * Quand on sysout le graphe on retournera sa matrice d'adjacence et de valeur. 
		 * On a choisie de retourner la matrice adjacente et de valeur en même temps, l'une 
		 * à côté de l'autre.
		 */
		String toString = "Matrice Adjacente :\n";

		for(int[] matrice: matriceAdjacent()) {
			for(int element: matrice) {
				toString += element + "\t";
			}
			toString += "\n";
		}
		

		toString += "\nMatrice de Valeur :\n";
		for(double[] matrice: matriceValeur()) {
			for(double element: matrice) {
				if(element == Double.POSITIVE_INFINITY) {
					toString += "I\t";
				}else {
					toString += element + "\t";
				}
			}
			toString += "\n";
		}
		return toString;
	}
	
	
	/*
	 * On va maintenant coder une fontion renvoyant si le graphe possede un circuit élémentaire les 
	 * extrémité initial composant le circuit
	 */
	public List<Integer> aCircuit() {
		List<Integer> circuit = null;
		int[] circuitEnCourt = new int[graphe.size()];
		
		int cpt = 0;
		for(int[] i: this) {
			circuitEnCourt[cpt] = i[1];
		}
		
		
		return circuit;
	}
	
	// [nb_circuit, sommet]
	/**
	 * 
	 * @return boolean: true si le graphe possede un circuit absorbant, false sinon
	 */
	public boolean circuitAbsorbant() {
		/*
		 * Pour savoir si un graphe possède un circuit absorbant
		 */

		return true;
	}
	
	/**
	 * @return the nbSommet
	 */
	public int getNbSommet() {
		return nbSommet;
	}

	/**
	 * @param nbSommet the nbSommet to set
	 */
	public void setNbSommet(int nbSommet) {
		this.nbSommet = nbSommet;
	}

	/**
	 * @return the nbArc
	 */
	public int getNbArc() {
		return nbArc;
	}

	/**
	 * @param nbArc the nbArc to set
	 */
	public void setNbArc(int nbArc) {
		this.nbArc = nbArc;
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
