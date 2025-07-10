public class banco_dados_tester {
    public static void main(String[] args) {
        questao_4 banco = new questao_4();

        System.out.println(" TESTE INICIADO ");

        Thread[] reading = new Thread[10];
        for (int i = 0; i < 10; i++) {
            int id = i + 1;
            reading[i] = new Thread(() -> {
                try {
                    String dadosLidos = banco.read();
                    System.out.println("Thread leitura" + id + " completou: " +dadosLidos);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            reading[i].start(); 
        }

        for (int i = 0; i < 3; i++) {
            int id = i + 1;

            Thread creating = new Thread(() -> {
                try {
                    banco.create("Dado " + id);
                    System.out.println("Thread criação" + id + " completou");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            creating.start();

            Thread updating = new Thread(() -> {
                try {
                    banco.update("Dado " + id);
                    System.out.println("Thread atualizar" + id + " completou");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            updating.start();


            Thread deleting = new Thread(() -> {
                try {
                    banco.delete("Dado " + id);
                    System.out.println("Thread deletar" + id + " completou");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            deleting.start(); 
        }

       
        for (Thread reader : reading) {
            try {
                reader.join(); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
