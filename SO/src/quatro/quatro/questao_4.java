
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class questao_4 {
    
    private Semaphore leituraSemaforo = new Semaphore(10);
    private ReadWriteLock readWrite = new ReentrantReadWriteLock(true);

    public void create(String dado) throws InterruptedException{
        readWrite.writeLock().lock();
        try{
            Thread.sleep(100);
            System.out.println(dado + " criado");
        }finally{
            readWrite.writeLock().unlock();
        }
    }

    public String read() throws InterruptedException{
        leituraSemaforo.acquire();
        readWrite.readLock().lock();
        try{
        Thread.sleep(50);
        return "dados lidos"; 
        } finally {
            readWrite.readLock().unlock();
            leituraSemaforo.release();
        }

    }

    public void update(String dado) throws InterruptedException {
        readWrite.writeLock().lock();
        try{
            Thread.sleep(100);
            System.out.println(dado + " atualizado");
        }finally{
            readWrite.writeLock().unlock();
        }
    }

    public void delete(String dado) throws InterruptedException {
        readWrite.writeLock().lock();
        try{
            Thread.sleep(100);
            System.out.println(dado + " apagado");
        }finally{
            readWrite.writeLock().unlock();
        }
    }



}
