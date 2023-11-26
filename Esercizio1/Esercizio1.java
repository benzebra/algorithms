/**
 * @author 	Filippo Brajucha
 * 			MATRICOLA: 0000920975
 * 			filippo.brajucha@studio.unibo.it
 * 			A.A. 2020/21
 */

/**
 * Esercizio 1 : Palindroma più lunga
 */

import java.io.FileReader;
import java.util.LinkedList;
import java.util.Scanner;

public class Esercizio1 {
	
	int numCheck;					//numero di input
	LinkedList<int[]> inputList = new LinkedList<int[]>(); 
	
	/**
	 * costruttore
	 * 
	 * @param input		file in input
	 */
	public Esercizio1(String input) {
		readInput(input);
	}
	
	/**
	 * readInput
	 * Metodo che gestisce la lettura dell'input attraverso la creazione di due scanner, uno riga
	 * per riga del file e l'altro all'interno della riga.
	 * Viene inizializzato lo scanner sulla linea e un array temporaneo di lunghezza (Line.length()+1)/2
	 * in modo da eliminare gli spazi bianchi e ottenere un array di interi con solo i numeri utili.
	 * Infine aggiungo l'array temporaneo nella prima cella disponibile della LinkedList chiamata
	 * inputList.
	 * 
	 * @param input 	String che indica il nome del file su cui applicare il FileReader
	 */
	public void readInput(String input) {
		String Line;
		try {
			Scanner scan = new Scanner(new FileReader(input));
			
			//controllo che il file non sia vuoto
			if(scan.hasNext()) {
				//numero di array di interi da controllare
				numCheck = scan.nextInt();
				
				scan.nextLine();										//riga spazzatura
				
				for(int i=0; i<numCheck; i++) {
					Line = scan.nextLine(); 
					//creazione di uno scanner all'interno di ogni linea
					Scanner scanLine = new Scanner(Line);
					
					//crezione array temporaneo di dimensione (Line.length()+1)/2
					int[] temporary = new int[(Line.length()+1)/2];
					int j=0;
					
					//scanner elemento per elemento della riga
					while(scanLine.hasNextInt()) {
						int element = scanLine.nextInt();
						temporary[j] = element;
						j++;
					}
					
					//aggiungo l'array temporaneo alla LinkedList inputList
					inputList.add(i, temporary);
					scanLine.close();
				}
				scan.close();
			}else {
				System.err.println("ERRORE : Il file e' vuoto");
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.println("ERRORE : File inesistente");
			System.exit(1);
		}
	}

	/**
	 * solution
	 * Per cercare la palindroma maggiore, vengono creati e passati al metodo checkPalindroma 
	 * tutti i possibili sottovettori con gli estremi uguali.
	 * Per la creazione dei sottovettori si utilizzano due indici, il primo (i) che scorre da sinistra 
	 * verso destra, parte da 0 ed arriva fino alla lunghezza dell'array-1, mentre il secondo 
	 * (j), scorre da destra verso sinistra, partendo dall'ultimo elemento dell'array e restando 
	 * sempre maggiore del primo indice i.
	 * 
	 * Inoltre sono presenti dei controlli per i casi base: il primo controlla che la lunghezza 
	 * sia effettivamente > 1, il secondo controlla la dimensione del sottovettore (se è 2 non 
	 * passo dal metodo di controllo), l'ultimo controlla che la lunghezza sia maggiore di 3 
	 * (in questo caso non ci interessa che numero c'è in mezzo tra altri due uguali).
	 * 
	 * Infine il metodo stampa la lunghezza massima memorizzata nella variabile maxPalindroma.
	 */
	public void solution() {
		
		//itero ogni array nella lista
		for(int x=0; x<inputList.size(); x++) {
			int[] A = inputList.get(x);
			int maxPalindroma = 1;				//inizializzazione della lunghezza massima
			int i = 0;
			boolean flag = false;
			int tmp = 0;
			
			//check della lunghezza dell'array
			if(A.length > 1){
				//inizializzazione degli indici
				while(i < A.length-1) {
					for(int j=A.length-1; j>i; j--) {
					
						//controllo principale, se trovo due elementi uguali
						if(A[i] == A[j]) {
						
							//palindroma di due elementi
							if(i == j-1  &&  maxPalindroma == 1) {
								maxPalindroma = 2;
							}else {
								tmp = j-i+1;
								//palindroma di 3 eleemnti
								if(tmp==3){
									flag = true;
								}else {
									int a = i+1;
									int b = j-1;
									flag = checkPalindroma(A,a,b);
								}
								
								//assegnazione della dimensione dopo il risultato di checkPalindroma
								if(flag == true){
									if(maxPalindroma < tmp) {
										maxPalindroma = tmp;
										flag = false;
									}else{
										flag = false;
									}
								}
								tmp = 0;
							}
						}
					}
					i++;
				}
				System.out.println(maxPalindroma);
			}else{
				System.out.println(maxPalindroma);
			}
		}
	}
	
	
	/**
	 * checkPalindroma
	 * Metodo che si occupa di controllare se un sottovettore di quello che stiamo iterando nel metodo
	 * solution è palindromo oppure no. 
	 * A questo metodo vengono passati l'array A (che stiamo controllando), l'indice di inizio+1 e 
	 * quello di fine-1 (dal metodo solution so già che gli elementi A[i] e A[j] sono uguali,
	 * altrimenti non sarebbe nemmeno entrato in questo metodo di controllo).
	 * 
	 * Inizialmente controllo che la palindroma sia di lunghezza pari o diparsi (utilizzando aritmetica
	 * modulo).
	 * 
	 * @param A 		Array di interi che sto controllando
	 * @param i 		indice +1 del palindromo (inizio)
	 * @param j 		indice -1 del palindromo (fine)
	 * @return 			booleano che determina se il sub-array è palindromo (true) o no (false)
	 */
	public boolean checkPalindroma(int[] A, int i, int j){
		if((j-i+1) % 2 == 0) {
			//caso pari
			for(int n=0; n<=((j-i+1)/2); n++) {
				if(A[i+n] != A[j-n]) {
					return false;
				}
			}
			return true;
		}else {
			//caso dispari
			for(int n=0; n<((j-i)/2); n++) {
				if(A[i+n] != A[j-n]) {
					return false;
				}
			}
			return true;
		}
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("ERRORE : Indicare il file sulla riga di comando");
			System.exit(1);
		}
		Esercizio1 es = new Esercizio1(args[0]);
		es.solution();
	}
}