import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class Benchmark16Thread {

    static final int THREADS = 16;
    static final int OPERACOES_POR_TIPO = 1000;

    enum Operacao { INSERIR, BUSCAR, REMOVER }

    static class OperacaoValor {
        Operacao op;
        int valor;
        OperacaoValor(Operacao op, int valor) {
            this.op = op;
            this.valor = valor;
        }
    }

    static class Resultados {
        AtomicLong tempoInsercao = new AtomicLong();
        AtomicLong tempoBusca = new AtomicLong();
        AtomicLong tempoRemocao = new AtomicLong();
        AtomicLong totalInsercao = new AtomicLong();
        AtomicLong totalBusca = new AtomicLong();
        AtomicLong totalRemocao = new AtomicLong();
    }

    public static void main(String[] args) throws InterruptedException {
        List<OperacaoValor> todasOperacoes = gerarOperacoesBalanceadas();
        System.out.println("== Benchmark com ListaThreadSafe ==");
        testarThreadSafe(new ListaThreadSafe<>(), todasOperacoes);

        System.out.println("\n== Benchmark com Vector ==");
        testarVector(new Vector<>(), todasOperacoes);
    }

    static List<OperacaoValor> gerarOperacoesBalanceadas() {
        List<OperacaoValor> lista = new ArrayList<>();
        Random rnd = new Random(12345);

        for (int i = 0; i < OPERACOES_POR_TIPO; i++) {
            lista.add(new OperacaoValor(Operacao.INSERIR, rnd.nextInt(OPERACOES_POR_TIPO)));
            lista.add(new OperacaoValor(Operacao.BUSCAR, rnd.nextInt(OPERACOES_POR_TIPO)));
            lista.add(new OperacaoValor(Operacao.REMOVER, rnd.nextInt(OPERACOES_POR_TIPO)));
        }

        Collections.shuffle(lista, rnd);
        return lista;
    }

    static void testarThreadSafe(ListaThreadSafe<Integer> lista, List<OperacaoValor> operacoes) throws InterruptedException {
        Queue<OperacaoValor> fila = new ConcurrentLinkedQueue<>(operacoes);
        Resultados r = new Resultados();
        CountDownLatch latch = new CountDownLatch(THREADS);
        long inicio = System.nanoTime();

        for (int i = 0; i < THREADS; i++) {
            new Thread(() -> {
                OperacaoValor opv;
                while ((opv = fila.poll()) != null) {
                    long ini, fim;
                    if (opv.op == Operacao.INSERIR) {
                        ini = System.nanoTime();
                        lista.adicionar(opv.valor);
                        fim = System.nanoTime();
                        r.tempoInsercao.addAndGet(fim - ini);
                        r.totalInsercao.incrementAndGet();
                    } else if (opv.op == Operacao.BUSCAR) {
                        int tam = lista.tamanho();
                        if (tam > 0) {
                            int idx = opv.valor % tam;
                            ini = System.nanoTime();
                            lista.consultar(idx);
                            fim = System.nanoTime();
                            r.tempoBusca.addAndGet(fim - ini);
                            r.totalBusca.incrementAndGet();
                        }
                    } else if (opv.op == Operacao.REMOVER) {
                        ini = System.nanoTime();
                        lista.remover(opv.valor);
                        fim = System.nanoTime();
                        r.tempoRemocao.addAndGet(fim - ini);
                        r.totalRemocao.incrementAndGet();
                    }
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        double totalMs = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.printf("Tempo total: %.2f ms\n", totalMs);
        imprimirResultados(r);
    }

    static void testarVector(Vector<Integer> vector, List<OperacaoValor> operacoes) throws InterruptedException {
        Queue<OperacaoValor> fila = new ConcurrentLinkedQueue<>(operacoes);
        Resultados r = new Resultados();
        CountDownLatch latch = new CountDownLatch(THREADS);
        long inicio = System.nanoTime();

        for (int i = 0; i < THREADS; i++) {
            new Thread(() -> {
                OperacaoValor opv;
                while ((opv = fila.poll()) != null) {
                    long ini, fim;
                    if (opv.op == Operacao.INSERIR) {
                        ini = System.nanoTime();
                        vector.add(opv.valor);
                        fim = System.nanoTime();
                        r.tempoInsercao.addAndGet(fim - ini);
                        r.totalInsercao.incrementAndGet();
                    } else if (opv.op == Operacao.BUSCAR) {
                        int tam = vector.size();
                        if (tam > 0) {
                            int idx = opv.valor % tam;
                            ini = System.nanoTime();
                            vector.get(idx);
                            fim = System.nanoTime();
                            r.tempoBusca.addAndGet(fim - ini);
                            r.totalBusca.incrementAndGet();
                        }
                    } else if (opv.op == Operacao.REMOVER) {
                        ini = System.nanoTime();
                        vector.remove(Integer.valueOf(opv.valor)); // Remover por objeto é mais seguro aqui
                        fim = System.nanoTime();
                        r.tempoRemocao.addAndGet(fim - ini);
                        r.totalRemocao.incrementAndGet();
                    }
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        double totalMs = (System.nanoTime() - inicio) / 1_000_000.0;
        System.out.printf("Tempo total: %.2f ms\n", totalMs);
        imprimirResultados(r);
    }

    static void imprimirResultados(Resultados r) {
        System.out.printf("Insercao: %d | Tempo: %.2f ms | Ops/s: %.2f\n",
                r.totalInsercao.get(),
                r.tempoInsercao.get() / 1_000_000.0,
                r.totalInsercao.get() / (r.tempoInsercao.get() / 1_000_000_000.0));

        System.out.printf("Busca: %d | Tempo: %.2f ms | Ops/s: %.2f\n",
                r.totalBusca.get(),
                r.tempoBusca.get() / 1_000_000.0,
                r.totalBusca.get() / (r.tempoBusca.get() / 1_000_000_000.0));

        System.out.printf("Remocao: %d | Tempo: %.2f ms | Ops/s: %.2f\n",
                r.totalRemocao.get(),
                r.tempoRemocao.get() / 1_000_000.0,
                r.totalRemocao.get() / (r.tempoRemocao.get() / 1_000_000_000.0));
    }
}