package exam;
/**
 * @author 	Filippo Brajucha
 * 			MATRICOLA: 0000920975
 * 			filippo.brajucha@studio.unibo.it
 * 			A.A. 2020/21
 */

/**
 * Esercizio 5 : Resto non erogabile
 */

import java.io.FileReader;
import java.util.Scanner;

public class Esercizio5 {
	
	int[] Cassa;				//tagli in input
	int qta;					//numero di tagli in input
	int max;					//somma di tutti i tagli
	boolean[][] MatrixBool;		//matrice con tutti i resti erogabili
	
	/**
	 * costruttore
	 * 
	 * @param input		file in input
	 */
	public Esercizio5(String input) {
		readInput(input);
	}
	
	/**
	 * readInput
	 * Con lo scanner memorizza ogni intero in un array "Cassa" che indica i tagli che ho a disposizione
	 * e, contemporaneamente, memorizza anche la somma dei valori di tutti i tagli.
	 * 
	 * @param input		file input
	 */
	public void readInput(String input) {
		Scanner scan;
		try {
			scan = new Scanner(new FileReader(input));
			if(scan.hasNextInt()) {
				qta = scan.nextInt();
				Cassa = new int[qta];
			
				for(int i=0; i<qta; i++) {
					if(scan.hasNextInt()){
						Cassa[i] = scan.nextInt();
						max = max + Cassa[i];
					}else{
						System.err.println("Errore nel file input");
						System.exit(1);
					}
				}
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
	 * Utilizzo la programmazione dinamica, la soluzione si basa su una matrice booleana (MatrixBool)
	 * che aggiorno mano a mano.
	 * La MatrixBool è una matrice booleana [qta]x[max+1] , quindi tante righe quanto il numero di 
	 * tagli che ho a disposizione e tante colonne quante la somma massima dei tagli in input.
	 * Nelle colonne della matrice (che rappresentano tutti i numeri dal taglio minore alla somma dei 
	 * tagli disponibili), viene messo true se posso erogare quel resto o false se non posso.
	 * Ogni volta che l'algoritmo trova un numero o una somma che nella riga precedente (quindi la 
	 * situazione con l'input precendete) è false ma in realtà con il nuovo taglio inserito può essere
	 * erogata, sostituisce tutta la colonna con dei true.
	 * Nell'ultima riga trovo tutti i resti erogabili (rappresentati dagli inidici delle colonne), 
	 * contrassegnati con true e quelli non erogabili con false.
	 * La matrice creata viene usata nel metodo result() per stampare l'output.
	 * 
	 * Alla fine faccio un controllo, se il valore del resto erogamìbile è rimasto il MAX_VALUE, allora
	 * vuol dire che riesco ad erogare tutti i resti possibili fino al massimo, quindi il resto minimo 
	 * non erogabile è la somma dei tagli + 1. 
	 * 
	 */
	public void solution() {
		int num;
		int tmp = 0;
		int indiceRiga;
		MatrixBool = new boolean[qta][max+1];
		
		//inizializzazione 
		for(int n=0; n<qta; n++){
			MatrixBool[n][0] = true;
			for(int m=1; m<max+1; m++){
				MatrixBool[n][m] = false;
			}
		}
		//setto la colonna del primo numero
		setColonna(Cassa[0], 0);
				
		//per ogni elemento preso in input
		for(int j=1; j<qta; j++) {
			num = Cassa[j];
			setColonna(num,j);
			indiceRiga = j;
			for(int k=1; k<max; k++) {
				if(MatrixBool[indiceRiga][k] != true) {
					tmp = k - num;
					if(tmp >= 0){
						//riferimento alla riga precedente
						if(MatrixBool[indiceRiga-1][tmp] != false) {
							setColonna(k,j);
						}
					}
				}
			}
		}
		
		result();
	}
	
	/**
	 * setColonna
	 * Riempie la colonna val di true dalla start-esima riga in giu'.
	 *  
	 * @param val		indice della colonna	
	 * @param start		indice della riga
	 */
	public void setColonna(int val, int start) {
		for(int i=start; i<qta; i++) {
			MatrixBool[i][val] = true;
		}
	}

	/**
	 * result
	 * Eseguendo un ciclo su tutti gli elementi dell'ultima riga, quando trovo un false stampo l'indice 
	 * della colonna (che nella MatrixBool rappresenta il resto non erogabile).
	 * Se non trova resti non erogabili stampa la somma dei tagli + 1.
	 */
	public void result() {
		for(int i=0; i<max; i++) {
			if(MatrixBool[qta-1][i] == false) {
				System.out.println(i);
				System.exit(1);
			}
		}
		System.out.println(max+1);
	}
	
	public static void main(String[] args) {
		if(args.length != 1) {
			System.err.println("Indicare il file sulla riga di comando");
			System.exit(1);
		}
		Esercizio5 es = new Esercizio5(args[0]);
		es.solution();
	}
}