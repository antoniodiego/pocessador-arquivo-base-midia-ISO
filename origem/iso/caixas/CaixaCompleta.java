package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaCompleta extends Caixa {

	// 1 byte
	public int versao;
	public int bandeira;

	public CaixaCompleta(String tipoCaixa, int versao, int flag) {
		super(tipoCaixa);
		this.versao = versao;
		this.bandeira = flag;
		this.tamanho = 12;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		csi.escreveByte(versao);
		csi.escreveInt24(bandeira);
	}
}
