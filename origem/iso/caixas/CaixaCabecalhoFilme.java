package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaCabecalhoFilme extends CaixaCompleta {

	public long tempo_criacao;
	public long tempo_modificacao;
	public long escala;
	public long duracao;

	public long rate = 0x00010000;
	public int volume = 0x0100;
	public int reserved = 0;
	public long[] reservado = new long[2];
	public long[] matriz = { 0x00010000, 0, 0, 0, 0x00010000, 0, 0, 0, 0x00010000 };
	public long[] predefinido;

	public long idProximoTrack;

	public CaixaCabecalhoFilme(int versao) {
		super("mvhd", versao, 0);
		predefinido = new long[] { 0, 0, 0, 0, 0, 0 };

		this.tamanho = 12 + 96;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		System.out.println("Salvando cabecalho filme");
		super.salva(csi);
		
		csi.escreveInt32(tempo_criacao);
		csi.escreveInt32(tempo_modificacao);
		csi.escreveInt32(escala);
		csi.escreveInt32(duracao);
		csi.escreveInt32(rate);
		csi.escreveInt16(volume);
		csi.escreveInt16(reserved);
		csi.escreveInt32(reservado[0]);
		csi.escreveInt32(reservado[1]);

		//32
		for (int c = 0; c < matriz.length; c++) {
			csi.escreveInt32(matriz[c]);
		}
		//68
		
		//24
		for (int c = 0; c < predefinido.length; c++) {
			csi.escreveInt32(predefinido[c]);
		}
		//92
		csi.escreveInt32(idProximoTrack);
		//96
	}
}
