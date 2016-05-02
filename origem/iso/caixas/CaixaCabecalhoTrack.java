package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

//Fica dentro CaixaTrack
public class CaixaCabecalhoTrack extends CaixaCompleta {
	public long tempo_criacao;
	public long tempo_modificacao;
	public long idTrack;
	public long reservado1 = 0;
	public long duracao;

	public long[] reservado = new long[] { 0, 0 };
	public int camada = 0;
	public int grupoAlternado;
	public int volume = 0x0100;
	public int reserved = 0;
	public long[] matriz = { 0x00010000, 0, 0, 0, 0x00010000, 0, 0, 0, 0x00010000 };
	public long largura;
	public long altura;

	public CaixaCabecalhoTrack(int versao, int flag) {
		super("tkhd", versao, flag);
		this.tamanho = 12 + 80;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		csi.escreveInt32(tempo_criacao);
		csi.escreveInt32(tempo_modificacao);
		csi.escreveInt32(idTrack);
		csi.escreveInt32(reservado1);
		csi.escreveInt32(duracao);
		csi.escreveInt32(reservado[0]);
		csi.escreveInt32(reservado[1]);
		csi.escreveInt16(camada);
		csi.escreveInt16(grupoAlternado);
		csi.escreveInt16(volume);
		csi.escreveInt16(reserved);
		//36 b
		for (int c = 0; c < matriz.length; c++) {
			csi.escreveInt32(matriz[c]);
		}
	//72
		csi.escreveInt32(largura);
		csi.escreveInt32(altura);
		//80
	}
}
