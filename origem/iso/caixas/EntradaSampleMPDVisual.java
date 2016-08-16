package iso.caixas;

public class EntradaSampleMPDVisual extends Caixa {

	byte[] reservado;
	// 16 bits
	int referenciaDados;
	int[] reservadoB;
	int largura, altura;
	// C de Trinta e dois
	int reservadoC;
	int reservadoD;
	int reservadoE;
	int reservadoJF;
	// LB bytes
	byte[] reservadoLB;
	int resevadoB;
	int reservdoF;

	public EntradaSampleMPDVisual() {
		super("mp4v");
		reservado = new byte[6];
		reservadoB = new int[4];
	}

}
