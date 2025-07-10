package dois;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ListaThreadSegura<T> {
    private final List<T> lista = new ArrayList<>();
    private final ReentrantReadWriteLock travaLeituraEscrita = new ReentrantReadWriteLock();

    public void adicionar(T elemento) {
        travaLeituraEscrita.writeLock().lock();
        try {
            lista.add(elemento);
        } finally {
            travaLeituraEscrita.writeLock().unlock();
        }
    }

    public void remover(T elemento) {
        travaLeituraEscrita.writeLock().lock();
        try {
            lista.remove(elemento);
        } finally {
            travaLeituraEscrita.writeLock().unlock();
        }
    }
    public T consultar(int indiceLista) {
        travaLeituraEscrita.readLock().lock();
        try {
            return lista.get(indiceLista);
        } finally {
            travaLeituraEscrita.readLock().unlock();
        }
    }
    public int tamanho() {
        travaLeituraEscrita.readLock().lock();
        try {
            return lista.size();
        } finally {
            travaLeituraEscrita.readLock().unlock();
        }
    }

}



