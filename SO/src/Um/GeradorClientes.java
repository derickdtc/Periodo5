package Um;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
public class GeradorClientes implements Runnable {
    private final int capacidadeFila;
    private final AtomicInteger cadeirasOcupadas;
    private final Semaphore clientes;
    private final Semaphore barbeiros;
    private final Semaphore mutex;
    private final Random random = new Random();
    private int proximoClienteId = 1;

    public GeradorClientes(int capacidadeFila, AtomicInteger cadeirasOcupadas, Semaphore clientes, Semaphore barbeiros, Semaphore mutex) {
        this.capacidadeFila = capacidadeFila;
        this.cadeirasOcupadas = cadeirasOcupadas;
        this.clientes = clientes;
        this.barbeiros = barbeiros;
        this.mutex = mutex;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                // Um novo cliente chega em um tempo aleatório 
                int tempoChegada = random.nextInt(3) + 4; // Entre 4 e 6 segundos
                Thread.sleep(tempoChegada * 1000);

                int clienteId = proximoClienteId++;
                System.out.println(">> Cliente " + clienteId + " chegou.");

                try {
                    // Garante que a verificação e o incremento das cadeiras seja atômico
                    mutex.acquire();

                    // Verifica se a fila de espera está cheia 
                    if (cadeirasOcupadas.get() < capacidadeFila) {
                        cadeirasOcupadas.incrementAndGet();
                        System.out.println("   Cliente " + clienteId + " sentou na cadeira de espera. Cadeiras ocupadas: " + cadeirasOcupadas.get());

                        // Avisa que tem um cliente novo esperando
                        clientes.release();
                        mutex.release(); // Libera o acesso à fila

                        // Tenta acordar um barbeiro (ou espera se todos estiverem ocupados)
                        System.out.println("   Cliente " + clienteId + " está esperando por um barbeiro.");
                        barbeiros.acquire();

                        // O cliente foi chamado pelo barbeiro, então libera a cadeira da fila de espera
                        mutex.acquire();
                        cadeirasOcupadas.decrementAndGet();
                        mutex.release();
                        System.out.println("   Cliente " + clienteId + " foi para o corte. Cadeiras ocupadas: " + cadeirasOcupadas.get());

                    } else {
                        // A fila está cheia, o cliente vai embora 
                        System.out.println("!! Fila cheia! Cliente " + clienteId + " foi embora. !!");
                        mutex.release(); // Libera o acesso, pois o cliente não vai entrar na fila
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("O gerador de clientes parou.");
        }
    }
}