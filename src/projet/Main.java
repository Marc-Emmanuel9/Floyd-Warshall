package projet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import projet.exception.EmptyFileException;
import projet.exception.NonCompletGrapheException;


public class Main {

	public static void main(String[] args) {
		try {
			FileInputStream file = new FileInputStream("C:\\Users\\marti\\Desktop\\Graphe.txt");
			Graphe graphe = new Graphe(file);
			System.out.println(graphe);
			System.out.println();
		} catch (FileNotFoundException | EmptyFileException | NonCompletGrapheException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
		

	}
	
	public static void algoFloydWarshall(Graphe graphe) {
		if(!graphe.circuitAbsorbant()) {
			
		}
	}

}
