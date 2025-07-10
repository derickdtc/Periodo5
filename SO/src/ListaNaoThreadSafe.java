import java.util.ArrayList;
import java.util.List;

public class ListaNaoThreadSafe<T> {
    private final List<T> lista = new ArrayList<>();

    public void adicionar(T elemento) {
        lista.add(elemento);
    }

    public void remover(T elemento) {
        lista.remove(elemento);
    }

    public T consultar(int indiceLista) {
        return lista.get(indiceLista);
    }

    public int tamanho() {
        return lista.size();
    }
}
