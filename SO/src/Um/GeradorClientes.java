package Um;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class GeradorClientes implements Runnable {
    private final Random random = new Random();
    private int proximoClienteId = 1;

    // Passa os recursos para que possam ser entregues a cada novo cliente
    private final int capacidadeFila;
    private final AtomicInteger cadeirasOcupadas;
    private final Semaphore clientes;
    private final Semaphore barbeiros;
    private final Semaphore mutex;

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
                // Clientes chegam rápido para testar a fila
                Thread.sleep((random.nextInt(2) + 1) * 1000);

                // Cria um novo cliente em sua própria thread
                Cliente cliente = new Cliente(proximoClienteId++, capacidadeFila, cadeirasOcupadas, clientes, barbeiros, mutex);
                new Thread(cliente, "Cliente-" + (proximoClienteId - 1)).start();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("O gerador de clientes parou.");
    }
}