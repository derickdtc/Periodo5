package Um;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
	//A classe main será a barbearia 
    private static final int TEMPO_SIMULACAO_MS = 60000;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		System.out.println("Hello world");
		MinhaThreadEx m1 = new MinhaThreadEx(1, 20, "Merticorten");
		MinhaThreadEx m2 = new MinhaThreadEx(4, 8, "Predisin");
		MinhaThreadEx m3 = new MinhaThreadEx(5, 10, "Berotec");*/
		 // Capacidade da fila de espera 
        int capacidadeFila = 10;

        // Contador para as cadeiras de espera ocupadas.
        // Usamos AtomicInteger para garantir que as operações sejam atômicas
        // e seguras entre as threads.
        AtomicInteger cadeirasOcupadas = new AtomicInteger(0);

        // Semáforos para sincronização
        // clientes: Contabiliza clientes esperando. O barbeiro espera neste semáforo.
        Semaphore clientes = new Semaphore(0);

        // barbeiros: Contabiliza barbeiros livres. O cliente espera neste semáforo. 
        Semaphore barbeiros = new Semaphore(2); // Inicia com 2 barbeiros livres

        // mutex: Garante acesso exclusivo à variável 'cadeirasOcupadas'
        Semaphore mutex = new Semaphore(1);

        System.out.println("A barbearia abriu com 2 barbeiros e " + capacidadeFila + " cadeiras de espera.");

        // Criando e iniciando as threads dos barbeiros
        for (int i = 1; i <= 2; i++) {
            Barbeiro barbeiro = new Barbeiro(i, clientes, barbeiros, mutex);
            Thread threadBarbeiro = new Thread(barbeiro);
            threadBarbeiro.start();
        }

        // Criando e iniciando a thread que gera os clientes
        GeradorClientes gerador = new GeradorClientes(capacidadeFila, cadeirasOcupadas, clientes, barbeiros, mutex);
        Thread threadGerador = new Thread(gerador);
        threadGerador.start();

        // Deixa a simulação rodar pelo tempo definido
        try {
            Thread.sleep(TEMPO_SIMULACAO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Interrompe as threads após o tempo de simulação
        threadGerador.interrupt();
        System.out.println("\nA barbearia fechou. Não entram mais clientes.");
    }
		

	

}
