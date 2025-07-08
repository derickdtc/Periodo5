package Um;

public class MinhaThreadEx  extends Thread{
	private int intervalo;
	private int total;
	private String medicamento;
	public MinhaThreadEx(int intervalo, int total, String medicamento) {
		super();
		this.intervalo = intervalo * 1000;
		this.total = total;
		this.medicamento = medicamento;
		
		this.start();
	}
	
	public void run() {
		for (int i = 1; i <= this.total; i++) {
			System.out.println(this.medicamento + "-" + i);
			try {
				Thread.sleep(this.intervalo);
			} catch (InterruptedException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
