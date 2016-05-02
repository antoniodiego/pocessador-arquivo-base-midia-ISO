package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public abstract class EntradaSample extends Caixa {

	public int reservado[];
	public int indiceReferenciaDados;
	
	public EntradaSample(String tipo) {
		super(tipo);
	}
	
	public void salva(CorregoSaidaISO csi) throws IOException{
		super.salva(csi);
		
	}
}
