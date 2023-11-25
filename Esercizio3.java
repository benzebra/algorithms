/**
 * @author 	Filippo Brajucha
 * 			MATRICOLA: 0000920975
 * 			filippo.brajucha@studio.unibo.it
 * 			A.A. 2020/21
 */

/**
 * Esercizio 3 : Dividere il numero
 */

import java.io.FileReader;
import java.util.Scanner;

public class Esercizio3 {
	long[] inputNumbers;		//array con tutti gli interi in input (N.B. si utilizza long perchè alcuni
								//input sono molto grandi)
	int dim;					//quantità di numeri in input
	
	/**
	 * costruttore
	 * 
	 * @param input 	file in input
	 */
	public Esercizio3(String input) {
		readInput(input);
	}
	
	/**
	 * readInput
	 * Lettutra dell'input e memorizzazione in array
	 * @param input
	 */
	private void readInput(String input) {
		try {
			Scanner scanFile = new Scanner(new FileReader(input));
			if(scanFile.hasNext()) {
				
				dim = scanFile.nextInt();

				inputNumbers = new long[dim];
				for(int i=0; i<dim; i++) {
					inputNumbers[i] = scanFile.nextInt();
				}
				
				scanFile.close();
			}else {
				System.err.println("ERRORE : Il file e' vuoto");
				System.exit(1);
			}
		}catch(Exception e) {
			System.err.println("ERRORE : File inesistente");
			System.exit(1);
		}	
	}

	/**
	 * solution
	 * Soluzione con programmazione dinamica.
	 * Viene creato un array Support per ogni input, lungo quanto il numero in input; da 
	 * subito elimino i casi base: i primi 5 elementi (0-4) sono numeri prestabiliti, i 
	 * successivi 2 elementi sono dei casi particolari (il numero si calcola con l'indice
	 * della cella e non con il valore) e gli altri elementi si creano utlizzando i valori
	 * creati in precedenza.
	 * 
	 * per j = 5 : 6
	 * Support[j] = (j - 3) * 3
	 * 
	 * per j = 7 : ...
	 * Support[j] = Support[j-3] * 3
	 */
	public void solution() {
		for(int i=0; i<dim; i++) {
			long n = inputNumbers[i];
			long[] Support = new long[(int)n+1];
			int a = 0;
			
			Support[0] = 0;
			Support[1] = 1;
			
			if((int)n>1) {
				for(int j=2; j<(int)n+1; j++) {
					a = j;
					//casi base
					if(j < 5) {
						switch(j) {
							case (2):
								Support[j] = 1;
								break;
							case (3):
								Support[j] = 2;
								break;
							case (4):
								Support[j] = 4;
								break;
						}
					}else if(j<7){
						//j = 5 : 6
						Support[j] = (a - 3) * 3;
						a = 0;
					}else {
						//j = 7 : ...
						Support[j] = Support[a-3] * 3;
					}
				}
			}
			System.out.println(Support[(int)n]);
		}
	}

	public static void main(String args[]) {
		if(args.length != 1) {
			System.err.println("Indicare il file sulla riga di comando");
			System.exit(1);
		}
		Esercizio3 es = new Esercizio3(args[0]);
		es.solution();
	}
}