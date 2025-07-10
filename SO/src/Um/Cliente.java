package Um;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class Cliente implements Runnable {
    private final int id;
    private final int capacidadeFila;
    private final AtomicInteger cadeirasOcupadas;
    private final Semaphore clientes;
    private final Semaphore barbeiros;
    private final Semaphore mutex;

    public Cliente(int id, int capacidadeFila, AtomicInteger cadeirasOcupadas, Semaphore clientes, Semaphore barbeiros, Semaphore mutex) {
        this.id = id;
        this.capacidadeFila = capacidadeFila;
        this.cadeirasOcupadas = cadeirasOcupadas;
        this.clientes = clientes;
        this.barbeiros = barbeiros;
        this.mutex = mutex;
    }

    @Override
    public void run() {
        System.out.println(">> Cliente " + id + " chegou.");
        try {
            mutex.acquire(); // Pega a chave para verificar a fila

            if (cadeirasOcupadas.get() < capacidadeFila) {
                cadeirasOcupadas.incrementAndGet();
                System.out.println("   Cliente " + id + " sentou. Cadeiras ocupadas: " + cadeirasOcupadas.get());
                clientes.release(); // Avisa que há um cliente novo na fila
                mutex.release(); // Devolve a chave da fila

                System.out.println("   Cliente " + id + " está esperando um barbeiro.");
                barbeiros.acquire(); // Espera um barbeiro ficar livre (aqui só a thread deste cliente bloqueia)

                System.out.println("   Cliente " + id + " foi para o corte.");
                // O barbeiro que o pegou já está cuidando dele. A lógica de decrementar a cadeira
                // pode ser movida para o barbeiro para simplificar.
            } else {
                System.out.println("!! Fila cheia! Cliente " + id + " foi embora.");
                mutex.release(); // Devolve a chave e vai embora
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}