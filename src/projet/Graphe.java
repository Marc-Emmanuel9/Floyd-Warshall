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
	private boolean[] visited;
	private List<List<Integer>> allSommetSuccesseur;
	
	/**
	 * @param file contenant un graphe
	 * @throws NonCompletGrapheException 
	 *
	 */
	public Graphe(final FileInputStream file) throws FileNotFoundException, 
										EmptyFileException, NonCompletGrapheException {
		this.graphe = new ArrayList<>();
		/*
		 * On va stock� le graphe rentr� en param�tre en m�moire.
		 * nbSommet contient le nombre de sommet
		 * nbArc contient le nombre d'arc
		 * graphe contient Extr�mit� initiale, suivie de l�extr�mit� terminale, suivie de la valeur 
		 * de l�arc : [Extr�mit� initiale, Extr�mit� final, Valeur de l'arc].
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
		}else throw new EmptyFileException("Aucun graphe n'est pr�sent !");
		this.fichier.close();
		
		this.allSommetSuccesseur = new ArrayList<>();
		for (int j = 0; j < this.nbSommet; j++) {
			this.allSommetSuccesseur.add(makeSommetSuccesseur(j));
		}
	}
	
	public List<Integer> makeSommetSuccesseur(final int sommet) {
		List<Integer> successeur = new ArrayList<>();
		for(int[] s: this) {
			if(s[0] == sommet) {
				successeur.add(s[1]);
			}
		}
		return successeur;
	}
	
	/**
	 * 
	 * @param stringTab
	 * @return integerTab
	 */
	public int[] stringTabToIntegerTab(String[] stringTab) {
		/*
		 * La fonction prend en param�tre un tableau de String constitu� de chiffre et renvoir un tableau
		 * de int constitu� de ces m�me int.
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
		 * On cr�e la matrice d'adjacence, si aucune valeur est attribuer au tableau 2D la 
		 * valeur 0 est donner par d�faut
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
		 * On cr�e la matrice d'adjacence, si aucune valeur est attribuer au tableau 2D la 
		 * valeur 0 est donner par d�faut
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
	 * @param s1
	 * @param s2
	 * @return boolean
	 */
	public boolean existChemin(final int s1, final int s2) {
		this.visited = new boolean[this.nbSommet];
		for (int i = 0; i < visited.length; i++) {
			this.visited[i] = false;
		}
		
		
		int n = this.matriceAdjacent().length;
		List<Integer> file = new ArrayList<>();
		file.add(s1);
		int courant;
		while(!file.isEmpty()) {
			courant = file.remove(0);
			this.visited[courant] = true;
			
			for(int i = 0; i < n; i++) {
				if(this.matriceAdjacent()[courant][i] > 0 && this.visited[i] == false) {
					file.add(i);
					this.visited[i] = true;
				}else if (this.matriceAdjacent()[courant][i] > 0 && i == s2) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	/*
	 * On va maintenant coder une fontion renvoyant si le graphe possede un circuit �l�mentaire les 
	 * extr�mit� initial composant le circuit
	 */
	public double aCircuit() {
		for(int i = 0; i < this.nbSommet; i++) {
			if(existChemin(i, i)) {
				return i;
			}
		}
		
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * 
	 * @return boolean: true si le graphe possede un circuit absorbant, false sinon
	 */
	public boolean circuitAbsorbant() {
		/*
		 * Pour savoir si un graphe poss�de un circuit absorbant
		 */
		if(aCircuit() != Double.POSITIVE_INFINITY) {
			int sommetDepart = (int) aCircuit();
			System.out.println(sommetDepart);
			int valeurTotalArc = 0;
			List<Integer> successeur = this.allSommetSuccesseur.get(sommetDepart);
			
			successeur.add(sommetDepart);
			
			for(int[] sommet: this) {
				if(successeur.contains(sommet[0])) {
					valeurTotalArc += sommet[2];
				}
			}
			
			return valeurTotalArc < 0;
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
		 * On a choisie de retourner la matrice adjacente et de valeur en m�me temps, l'une 
		 * � c�t� de l'autre.
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
