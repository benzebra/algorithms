/**
 * @author 	Filippo Brajucha
 * 			MATRICOLA: 0000920975
 * 			filippo.brajucha@studio.unibo.it
 * 			A.A. 2020/21
 */

/**
 * Esercizio 4 : Binary Search Tree
 */

import java.io.FileReader;
import java.util.Scanner;

public class Esercizio4 {
	
	Scanner scanLine;
	Tree T;					//Tree in input
	
	/**
	 * Tree
	 * L'albero in input viene memorizzato grazie a questa classe creata appositamente.
	 * al suo interno troviamo i setter di left e right e il getter del value.
	 *
	 */
	private class Tree{
		Integer value;				//valore del nodo che sto creando
		Tree parent;				//riferimento al nodo padre
		Tree left;					//figlio sinistro
		Tree right;					//figlio destro
		
		public Tree(Integer value) {
			this.value = value;
		}
		
		public void setLeft(Tree ALB) {
			ALB.parent = this;
			left = ALB; 
		}
		
		public void setRight(Tree ALB) {
			ALB.parent = this;
			right = ALB;
		}	
		
		public Integer getVal() {
			return value;
		}
	}

	
	public Esercizio4(String input) {
		readInput(input);
	}
	
	public void readInput(String input) {
		try {
			
			Scanner scanFile = new Scanner(new FileReader(input));
			
			if(scanFile.hasNext()) {
				
				String line = scanFile.nextLine();
				
				scanLine = new Scanner(line);
				
				scanLine.next(); //elimino quella che sicuramente è una "("
				
				//inizializzo sapendo che il primo intero è sempre la radice dell'albero
				T = new Tree(scanLine.nextInt());
				T.setLeft(new Tree(null));
				T.setRight(new Tree(null));
				
				scanLine.next(); //elimino quella che sicuramente è una "("
				
				T.setLeft(insertLeft(T, scanLine.next()));
				
			}else {
				System.err.println("ERRORE : Il file e' vuoto");
				System.exit(1);
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.err.println("ERRORE : File inesistente");
			System.exit(1);
		}
	}
	
	/**
	 * insertLeft
	 * Metodo utilizzato nella gestione dell'input, inserisce figlio sinistro ricorsivamente.
	 * Nel caso in cui successivamente trovi una "," richiama il metodo che inserisce un 
	 * fratello destro (quindi come parent avrà il parent del nodo e come valore quello
	 * del numero successivo), nel caso in cui trovi un altro carattere richiama ricorsivamente 
	 * il metodo così da creare un altro figlio sinistro con parent il nodo creato.
	 * 
	 * @param parent	Tree parent del Tree che sto creando
	 * @param str		value del Tree da creare e inserire
	 * @return			Tree, value come da input, figlio destro o sinistro con i valori attribuiti o nulli
	 */
	public Tree insertLeft(Tree parent, String str) {
		Tree supportLeft;
		Integer obj;
		if (str.equals("-")) {
			obj = null;
			
			supportLeft = new Tree(obj);
			supportLeft.parent = parent;
		}else{
			obj = Integer.parseInt(str);
			
			supportLeft = new Tree(obj);
			supportLeft.parent = parent;
			supportLeft.setLeft(new Tree(null));
			supportLeft.setRight(new Tree(null));
		}
		
		str = scanLine.next();
		
		if(str.equals(",")) {
			String val = scanLine.next();
			supportLeft.parent.setRight(insertRight(supportLeft.parent, val));
		}else {
			supportLeft.setLeft(insertLeft(supportLeft, scanLine.next()));
		}
		
		//parte finale della ricorsione, quando deve ritornare indietro
		if(scanLine.next().equals(",")){
			String val = scanLine.next();
			supportLeft.parent.parent.setRight(insertRight(supportLeft.parent.parent, val));
		}
		
		return supportLeft;
	}
	
	/**
	 * insertRight
	 * Stesso principio dell'insertLeft solo che inserisce nodo a destra sempre ricorsivamente.
	 * Nel caso in cui il carattere successivo a quello del figlio destro sia "(" chiamo il metodo che
	 * genera il sinistro.
	 * 
	 * @param parent	Tree parent del Tree che sto creando
	 * @param str		value del Tree da creare e inserire
	 * @return			Tree, value come da input, figlio destro o sinistro con i valori attribuiti o nulli
	 */
	public Tree insertRight(Tree parent, String str) {
		Tree supportRight;
		Integer obj;
		if(str.equals("-")) {
			obj = null;
			
			supportRight = new Tree(obj);
			supportRight.parent = parent;
		}else{
			obj = Integer.parseInt(str);
			
			supportRight = new Tree(obj);
			supportRight.parent = parent;
			supportRight.setLeft(new Tree(null));
			supportRight.setRight(new Tree(null));
		}
		
		str = scanLine.next();
		
		if(str.equals("(")) {
			supportRight.setLeft(insertLeft(supportRight, scanLine.next()));
		}
		
		return supportRight;
	}
	
	/**
	 * solution
	 * Chiama il metodo checkBST(Tree T, MIN, MAX) per conotrollare che l'albero sia effettivamente
	 * un BST. Nel caso in cui il metodo restituisca TRUE viene stampato il minimo, altrimenti
	 * viene stampato "NON BST".
	 */
	public void solution() {
		if(checkBST(T, Integer.MIN_VALUE, Integer.MAX_VALUE)){
			System.out.println(min(T));
		}else{
			System.out.println("NON BST");
		}
	}
	
	/**
	 * checkBST 
	 * Metodo ricorsivo che percorre tutto l'albero e verifica che il figlio
	 * sinistro non sia maggiore del parent e il figlio destro non sia minore.
	 * 
	 * @param A			Albero o porzione di albero da verificare
	 * @param MIN		
	 * @param MAX
	 * @return			true o false a seconda se l'albero è BST oppure no
	 */
	public boolean checkBST(Tree A, Integer MIN, Integer MAX){
		if(A.getVal() == null){
			return true;
		}
		if(A.getVal() < MIN || A.getVal() > MAX){
			return false;
		}
		return checkBST(A.left, MIN, A.getVal()) && checkBST(A.right, A.getVal(), MAX);
	}
	
	/**
	 * min
	 * Calcolo il valore min di un Tree.
	 * 
	 * @param tmp		Tree da cui devo estrarre il min
	 * @return			valore minimo
	 */
	public Integer min(Tree tmp) {
		Tree A = tmp;
		while(A.left.getVal() != null) {
			A = A.left;
		}
		return A.getVal();
	}
	
	/**
	 * max
	 * Calcolo il valore max di un Tree.
	 * 
	 * @param tmp		Tree da cui devo estrarre il max
	 * @return			valore massimo
	 */
	public Integer max(Tree tmp){
		Tree A = tmp;
		if(A.getVal() != null){
			while(A.right.getVal() != null){
				A = A.right;
			}
			return A.getVal();
		}else{
			return A.getVal();
		}
		
	}
	
	public static void main(String args[]) {
		if(args.length != 1) {
			System.err.println("Inserire il file nella riga di comando");
			System.exit(1);
		}
		
		Esercizio4 es = new Esercizio4(args[0]);
		es.solution();
	}
}