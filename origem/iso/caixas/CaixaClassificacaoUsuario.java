package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaClassificacaoUsuario extends CaixaCompleta {

	int pad = 0;
	public int rat =2;

	public CaixaClassificacaoUsuario() {
		super("urat", 0, 0);
		this.tamanho += 4;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		csi.escreveInt24(pad);
		csi.escreveByte(rat);
	}
}
