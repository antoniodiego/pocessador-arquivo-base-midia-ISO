package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaCabecalhoMidia extends CaixaCompleta {

	public long tempo_criacao_64;
	public long tempo_modificacao_64;
	public long duracao_64;
	// Versao 0
	public long tempo_criacao_32;
	public long tempo_modificacao;
	public long escala;
	public long duracao;
	public byte pad_1 = 0;
	// 3 5bits
	public byte[] language_5;
	public int predefinido_16;

	public CaixaCabecalhoMidia(int versao,int flag) {
		super("mdhd", versao, flag);
		this.language_5 = new byte[] { 'a', 'b', 'c' };
		this.tamanho = 12 + 20;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		csi.escreveInt32(tempo_criacao_32);
		csi.escreveInt32(tempo_modificacao);
		csi.escreveInt32(escala);
		csi.escreveInt32(duracao);
		csi.escreveByte(pad_1);

		for (int c = 0; c < 3; c++) {
			csi.escreveByte(language_5[c]);
		}
		csi.escreveInt16(predefinido_16);
	}
}
