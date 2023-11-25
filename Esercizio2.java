/**
 * @author 	Filippo Brajucha
 * 			MATRICOLA: 0000920975
 * 			filippo.brajucha@studio.unibo.it
 * 			A.A. 2020/21
 */

/**
 * Esercizio 2 : Trasporto di materiali
 */

import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Scanner;

public class Esercizio2 {
	
	LinkedList<Arco> edges = new LinkedList<>();
	static int DST = -1;	//indice destinazione (quindi l'ospedale)
	int source;				//sorgente percorso massimizzato
	int numNodes;			//numero nodi in input
	int[] finalPath;		//path percorso massimizzato
	int[] SRC;				//array delle sorgenti (tutti i magazzini, "M")
	int[] SRC_support;		//array temporaneo per memorizzazione delle sorgenti dall'input
	int numSources = 0;		//numero sorgenti

	/**
	 * ARCO
	 * Utilizzata in fase di input per trasformare l'input di nodi in una LinkedList di archi,
	 * necessaria per implementare l'algoritmo di Bellman-Ford
	 *
	 */
	private class Arco {
        final int src;			//sorgente dell'arco
        final int dst;			//destinazione dell'arco
        final double w;			//peso dell'arco

        public Arco(int src, int dst, double w)
        {
            this.src = src;
            this.dst = dst;
            this.w = w;
        }
    }
	
	/**
	 * costruttore
	 * 
	 * @param input String che indica il file in input
	 */
	public Esercizio2(String input) {
		readInput(input);
	}
	
	/** 
	 * readInput
	 * Metodo che gestisce l'input con due scanner (uno è un FileReader, mentre l'altro è inizializzato 
	 * riga per riga) e li crea una LinkedList di archi.
	 * 
	 * @param input		file in input
	 */
	public void readInput(String input) {
		
		Locale.setDefault(Locale.US);
		
		try {
			Scanner scanFile = new Scanner(new FileReader(input));
			if(scanFile.hasNext()) {
				numNodes = scanFile.nextInt();
				
				scanFile.nextLine();					//linea spazzatura (per poter passare alla linea successiva)
				
				//inizializzo variabili che mi servono successivamente
				String line, type;						
				Scanner scanLine;						
				double w;
				SRC_support = new int[numNodes];		//creo un array di supporto per memorizzare le sorgenti (M)
				
				for(int i=0; i<numNodes; i++) {
					
					//inizializzo uno scaner all'interno del file (riga per riga, per ogni nodo)
					line = scanFile.nextLine();
					scanLine = new Scanner(line);
					
					//type è una variabile che indica la tipologia di nodo (magazzino, carico/scarico o ospedale)
					type = scanLine.next();
					
					//check se il primo nodo è un magazzino
					if(i==0){
						if(!(type.equals("M"))){
							System.out.println("-1");
							System.exit(1);
						}
					}
					
					if(type.equals("M")) {
						SRC_support[numSources] = i;
						numSources++;
					}else if(type.equals("O")) {
						DST = i;
					}
					
					//per ogni nodo creo degli archi con i nodi a cui è collegato con peso quello del nodo stesso,
					//sorgente il nodo stesso e destinazione presa con lo scanner
					w = scanLine.nextDouble();
					int numEdges = scanLine.nextInt();
					for(int j=0; j<numEdges; j++) {
						Arco e = new Arco(i, scanLine.nextInt(), w);
						edges.add(e);
					}
					scanLine.close();
				}
				
				//qualora non esista un ospedale
				if(DST == -1) {
					System.out.println("-1");
					System.exit(1);
				}
				
				//inizializzo e creo l'array SRC (delle sorgenti, i magazzini) con gli indici delle sorgenti stesse
				SRC = new int[numSources];
				for(int k=0; k<SRC.length; k++) {
					SRC[k] = SRC_support[k];
				}
				
				scanFile.close();
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
	 * Implementazione dell'algoritmo di Bellman-Ford rivisitato. 
	 * All'interno è presente un ciclo for che itera tutte le sorgenti con un controllo finale per 
	 * memorizzare la sorgente e il path in corrispondenza del percorso massimizzato.
	 * Inoltre alla fine è presente un controllo per verificare la presenza di cicli positivi,che se 
	 * presenti, (proprio come quelli negativi per Bellman-Ford) viene restituito il valore -1.
	 * 
	 * @return MAX 		rappresenta la quantità di merce
	 */
	public double solution() {
		//array di risultati che cambiano a seconda della sorgente
		double MAX = Double.NEGATIVE_INFINITY; 
		boolean checkCycle = false;
		
		for(int i=0; i<numSources; i++) {
			int s = SRC[i];
			double[] d = new double[numNodes];
			int[] p = new int[numNodes];
			
			Arrays.fill(d, Double.NEGATIVE_INFINITY);
			Arrays.fill(p, -1);
			
			d[s] = 0.0;
			for(int j=0; j<numNodes-1; j++) {
				for(Arco e: edges) {
					final int src =  e.src;
					final int dst = e.dst;
					final double w = e.w;
					
					if(d[src] + w > d[dst]) {
						d[dst] = d[src] + w;
						p[dst] = src;
					}
				}
			}
			
			//controllo l'esistenza di cicli positivi
			for (Arco e: edges) {
	            final int src = e.src;
	            final int dst = e.dst;
	            final double w = e.w;

	            if (d[src] + w > d[dst]) {
	                checkCycle = true;
	            }
	        }
			
			//assegnazione della variabile MAX (se sono presenti cicli positivi non viene assegnata)
			//memorizzo sia il valore el percorso massimo, che il percorso e la sorgente
			if(d[DST] > MAX && checkCycle == false) {
				MAX = d[DST];
				finalPath = p;
				source = SRC[i];
			}else {
				checkCycle = false;
			}
		}
		return MAX;
	}
	
	/**
	 * printpath
	 * Stampa ricorsiva del percorso (finalPath) che massimizza la quantità di merce.
	 * 
	 * @param d rappresenta il nodo da stampare, volta per volta
	 */
	public void printpath(int d) {
		if(d == source) {
			System.out.println(d);
		}else if(finalPath[d] == -1){
			System.out.print(finalPath[d]);
		}else{
			printpath(finalPath[d]);
			System.out.println(d);
		}
	}
	
	public static void main(String[] args) {
		
		if(args.length != 1) {
			System.err.println("ERRORE : Indicare il file sulla riga di comando");
			System.exit(1);
		}
		
		Esercizio2 es = new Esercizio2(args[0]);
		double MAX = es.solution();
		
		if(MAX == Double.NEGATIVE_INFINITY) {
			//caso in cui non esista un cammino tra sorgenti e destinazione
			System.out.println("-1");
		}else {
			es.printpath(DST);
		}
	}
}