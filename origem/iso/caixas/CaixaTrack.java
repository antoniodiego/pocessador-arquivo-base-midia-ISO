package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

//Fica dentro da Caixa Filme
public class CaixaTrack extends Caixa {
	public CaixaCabecalhoTrack cct;
	public CaixaMidia caixaMidia;

	public CaixaTrack(CaixaCabecalhoTrack cct,CaixaMidia cm) {
		super("trak");
		this.cct = cct;
		this.caixaMidia = cm;
		this.tamanho = 8 + cct.tamanho+caixaMidia.tamanho;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		cct.salva(csi);
		caixaMidia.salva(csi);
	
		for (int c = 0; c < caixas.size(); c++) {
			caixas.get(c).salva(csi);
		}
	}
}
