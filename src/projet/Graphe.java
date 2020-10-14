package projet;

import java.util.Iterator;
import java.util.*;

public class Graphe implements Iterable<Integer[]> {

	private List<Integer[]> graphe;
	
	public Graphe() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Iterator<Integer[]> iterator() {
		// TODO Auto-generated method stub
		return this.graphe.iterator();
	}

}
