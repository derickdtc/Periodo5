import java.util.ArrayList;
import java.util.List;

public class BenchmarkSingleThread {

    public static void main(String[] args) {
        int[] tamanhos = {1_000, 10_000, 100_000};

        for (int tamanho : tamanhos) {
            System.out.println("=== Tamanho da lista: " + tamanho + " ===");

            //Insercao
            long tempoSimplesInsercao = medirInsercaoSimples(tamanho);
            long tempoSeguraInsercao = medirInsercaoSegura(tamanho);

            //Consulta
            long tempoSimplesBusca = medirBuscaSimples(tamanho);
            long tempoSeguraBusca = medirBuscaSegura(tamanho);

            //Remocao
            long tempoSimplesRemocao = medirRemocaoSimples(tamanho);
            long tempoSeguraRemocao = medirRemocaoSegura(tamanho);

            System.out.printf("Insercao Lista Simples: %.2f ms | Operacoes/s: %.2f%n",
                    converterMs(tempoSimplesInsercao), calcularOpsPorSegundo(tamanho, tempoSimplesInsercao));
            System.out.printf("Insercao Lista Segura : %.2f ms | Operacoes/s: %.2f%n",
                    converterMs(tempoSeguraInsercao), calcularOpsPorSegundo(tamanho, tempoSeguraInsercao));

            System.out.printf("Busca Lista Simples: %.2f ms | Operacoes/s: %.2f%n",
                    converterMs(tempoSimplesBusca), calcularOpsPorSegundo(tamanho, tempoSimplesBusca));
            System.out.printf("Busca Lista Segura : %.2f ms | Operacoes/s: %.2f%n",
                    converterMs(tempoSeguraBusca), calcularOpsPorSegundo(tamanho, tempoSeguraBusca));

            System.out.printf("Remocao Lista Simples: %.2f ms | Operacoes/s: %.2f%n",
                    converterMs(tempoSimplesRemocao), calcularOpsPorSegundo(tamanho, tempoSimplesRemocao));
            System.out.printf("Remocao Lista Segura : %.2f ms | Operacoes/s: %.2f%n",
                    converterMs(tempoSeguraRemocao), calcularOpsPorSegundo(tamanho, tempoSeguraRemocao));

            System.out.println();
        }
    }

    static long medirInsercaoSimples(int n) {
        List<Integer> lista = new ArrayList<>();
        long inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            lista.add(i);
        }
        return System.nanoTime() - inicio;
    }

    static long medirInsercaoSegura(int n) {
        ListaThreadSafe<Integer> lista = new ListaThreadSafe<>();
        long inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            lista.adicionar(i);
        }
        return System.nanoTime() - inicio;
    }

    static long medirBuscaSimples(int n) {
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) lista.add(i);
        long inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            lista.get(i);
        }
        return System.nanoTime() - inicio;
    }

    static long medirBuscaSegura(int n) {
        ListaThreadSafe<Integer> lista = new ListaThreadSafe<>();
        for (int i = 0; i < n; i++) lista.adicionar(i);
        long inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            lista.consultar(i);
        }
        return System.nanoTime() - inicio;
    }

    static long medirRemocaoSimples(int n) {
        List<Integer> lista = new ArrayList<>();
        for (int i = 0; i < n; i++) lista.add(i);
        long inicio = System.nanoTime();
        for (int i = n - 1; i >= 0; i--) {
            lista.remove(i);
        }
        return System.nanoTime() - inicio;
    }

    static long medirRemocaoSegura(int n) {
        ListaThreadSafe<Integer> lista = new ListaThreadSafe<>();
        for (int i = 0; i < n; i++) lista.adicionar(i);
        long inicio = System.nanoTime();
        for (int i = n - 1; i >= 0; i--) {
            lista.remover(i);
        }
        return System.nanoTime() - inicio;
    }

    static double calcularOpsPorSegundo(int n, long tempoNs) {
        if (tempoNs == 0) return 0.0;
        double tempoMs = tempoNs / 1_000_000.0;
        return (n * 1000.0) / tempoMs;
    }

    static double converterMs(long tempoNs) {
        return tempoNs / 1_000_000.0;
    }
}
