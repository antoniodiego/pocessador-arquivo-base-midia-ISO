package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

//Fica dentro da Caixa Filme
public class CaixaTrack extends Caixa {
	public CaixaCabecalhoTrack cabecalhoTrack;
	public CaixaMidia caixaMidia;

	public CaixaTrack(CaixaCabecalhoTrack cct, CaixaMidia cm) {
		super("trak");
		this.cabecalhoTrack = cct;
		this.caixaMidia = cm;
		this.tamanho = 8 + cct.tamanho + caixaMidia.tamanho;
	}

	public CaixaTrack() {
		super("trak");
	}

	public CaixaMidia getCaixaMidia() {
		return caixaMidia;
	}

	public void setCaixaMidia(CaixaMidia caixaMidia) {
		this.caixaMidia = caixaMidia;
		this.tamanho += caixaMidia.tamanho;
	}

	public CaixaCabecalhoTrack getCct() {
		return cabecalhoTrack;
	}

	public void setCct(CaixaCabecalhoTrack cct) {
		this.cabecalhoTrack = cct;
		this.tamanho += cct.tamanho;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);

		cabecalhoTrack.salva(csi);

		if (caixaMidia != null) {
			caixaMidia.salva(csi);
		} else {
			throw new IOException("Falta caixa obrigatória.");
		}

		for (int c = 0; c < caixas.size(); c++) {
			caixas.get(c).salva(csi);
		}
	}
}
