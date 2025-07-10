package Um;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class Barbeiro implements Runnable {
    private final int id;
    private final Semaphore clientes;
    private final Semaphore barbeiros;
    private final Random random = new Random();

    
    // Barbeiro conhece a fila e o mutex
    private final AtomicInteger cadeirasOcupadas;
    private final Semaphore mutex;

    public Barbeiro(int id, Semaphore clientes, Semaphore barbeiros, AtomicInteger cadeirasOcupadas, Semaphore mutex) {
        this.id = id;
        this.clientes = clientes;
        this.barbeiros = barbeiros;
        this.cadeirasOcupadas = cadeirasOcupadas;
        this.mutex = mutex;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                clientes.acquire(); //  Dorme até que um cliente chegue e o sinalize.

                
                //  O barbeiro foi acordado e vai atender. Ele agora libera a cadeira que o cliente usava.
                mutex.acquire();
                cadeirasOcupadas.decrementAndGet();
                System.out.println("   -> Cliente levantou para o corte. Cadeiras de espera ocupadas: " + cadeirasOcupadas.get());
                mutex.release();

                // 3. Corta o cabelo do cliente que o chamou.
                int tempoCorte = (random.nextInt(6) + 5); // 5 a 9 segundos
                System.out.println("Barbeiro " + id + " está cortando o cabelo. Duração: " + tempoCorte + "s.");
                Thread.sleep(tempoCorte * 1000);
                System.out.println("Barbeiro " + id + " terminou o corte.");

                // 4. Libera a si mesmo para o próximo cliente que está esperando por um barbeiro.
                barbeiros.release();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Barbeiro " + id + " está indo para casa.");
    }
}
