package iso.caixas;

public class CaixaCabecalhoFilme extends CaixaCompleta {

	public long tempo_criacao;
	public long rate = 0x00010000;
	public int volume = 0x0100;
	public int reserved = 0;
	public long[] reservado = new long[2];
	public long[] matriz = { 0x00010000, 0, 0, 0, 0x00010000, 0, 0, 0, 0x00010000 };
	public long[] predefinido;
	public long tempo_modificacao;
	public long escala;
	public long duracao;
	public long idProximoTrack;

	public CaixaCabecalhoFilme(int versao,int flag) {
		super("mvhd", versao, flag);
		predefinido = new long[6];
	}

}
