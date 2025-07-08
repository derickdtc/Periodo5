
public class TestesdaLista {

        public static void main(String[] args) throws InterruptedException {
           ListaThreadSegura<Integer> lista = new ListaThreadSegura<>();

            Thread t1 = new Thread(() -> {
                for (int i = 0; i < 1000; i++) {
                    lista.adicionar(i);
                }
            });


            Thread t2 = new Thread(() -> {
                for (int i = 1000; i < 2000; i++) {
                    lista.adicionar(i);
                }
            });

            // inicia as threads
            t1.start();
            t2.start();

            // espera elas terminarem
            t1.join();
            t2.join();

            // ve se o tamanho esta igual o esperado
            System.out.println("tamanho esperado: 2000");
            System.out.println("tamanho da nossa lista: " + lista.tamanho());

            // verificando o consultar
            System.out.println("primeiro elemento: " + lista.consultar(0));
            System.out.println("ultimo elemento: " + lista.consultar(lista.tamanho() - 1));
        }

}
