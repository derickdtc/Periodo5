package Um;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    private static final int TEMPO_SIMULACAO_MS = 90000;

    public static void main(String[] args) {
        int capacidadeFila = 10;
        int numBarbeiros = 2;

        Semaphore clientes = new Semaphore(0);
        Semaphore barbeiros = new Semaphore(numBarbeiros);
        Semaphore mutex = new Semaphore(1);
        AtomicInteger cadeirasOcupadas = new AtomicInteger(0);

        System.out.println("A barbearia abriu com " + numBarbeiros + " barbeiros e " + capacidadeFila + " cadeiras de espera.");

        
        // Passando os recursos da fila (cadeirasOcupadas e mutex) para o Barbeiro
        for (int i = 1; i <= numBarbeiros; i++) {
            Barbeiro barbeiro = new Barbeiro(i, clientes, barbeiros, cadeirasOcupadas, mutex); // Mudança aqui
            new Thread(barbeiro, "Barbeiro-" + i).start();
        }

        GeradorClientes gerador = new GeradorClientes(capacidadeFila, cadeirasOcupadas, clientes, barbeiros, mutex);
        Thread threadGerador = new Thread(gerador, "GeradorClientes");
        threadGerador.start();

        try {
            Thread.sleep(TEMPO_SIMULACAO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        threadGerador.interrupt();
        // A interrupção dos barbeiros e clientes será tratada nas próprias threads
        System.out.println("\nA barbearia fechou. Não entram mais clientes.");
    }
}
