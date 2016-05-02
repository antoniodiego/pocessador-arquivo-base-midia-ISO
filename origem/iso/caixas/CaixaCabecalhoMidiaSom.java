package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaCabecalhoMidiaSom extends CaixaCompleta {
	int balanco= 0;
	int reservado;

	public CaixaCabecalhoMidiaSom() {
		super("smhd", 0, 0);
		
		this.tamanho = 12+4;
	}

	public void salva(CorregoSaidaISO csi) throws IOException{
		super.salva(csi);
		csi.escreveInt16(balanco);
		csi.escreveInt16(reservado);
	}
}
