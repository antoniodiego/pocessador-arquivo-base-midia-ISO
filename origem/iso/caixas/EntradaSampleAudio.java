package iso.caixas;

public class EntradaSampleAudio extends EntradaSample{

	long[] reservado;
	int contadorCanais = 2;
	int tamanhoSmple = 16;
	int predefinido;
	int reservado2;
	long sampleRate = 128<<16;
	
	public EntradaSampleAudio(String tipo) {
		super(tipo);
	}

}
