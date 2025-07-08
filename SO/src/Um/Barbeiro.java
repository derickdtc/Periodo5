package Um;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Barbeiro implements Runnable{
	 private final int id;
	    private final Semaphore clientes;
	    private final Semaphore barbeiros;
	    private final Semaphore mutex;
	    private final Random random = new Random();

	    public Barbeiro(int id, Semaphore clientes, Semaphore barbeiros, Semaphore mutex) {
	        this.id = id;
	        this.clientes = clientes;
	        this.barbeiros = barbeiros;
	        this.mutex = mutex;
	    }

	    @Override
	    public void run() {
	        try {
	            while (!Thread.currentThread().isInterrupted()) {
	                // 1. Barbeiro tenta pegar um cliente. Se não houver, ele dorme (bloqueia).
	                clientes.acquire();

	                // 2. Acordou! Agora o barbeiro está ocupado e vai atender.
	                // Não precisa de mutex aqui porque 'clientes.acquire()' já garantiu um cliente.
	                // O cliente já liberou sua cadeira na fila (lógica no GeradorClientes).

	                // 3. Corta o cabelo por um tempo aleatório
	                int tempoCorte = random.nextInt(6) + 5; // Entre 5 e 10 segundos 
	                System.out.println("Barbeiro " + id + " está cortando o cabelo. Duração: " + tempoCorte + "s.");
	                Thread.sleep(tempoCorte * 1000);
	                System.out.println("Barbeiro " + id + " terminou o corte.");

	                // 4. O barbeiro está livre novamente.
	                barbeiros.release();
	            }
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            System.out.println("Barbeiro " + id + " está indo para casa.");
	        }
	    }
}
