package iso.caixas;

public class CaixaCabecalhoMidiaVideo extends CaixaCompleta {

	public int gfmode;
	public int[] opcolor;

	public CaixaCabecalhoMidiaVideo(String tipoCaixa, int versao, int flag) {
		super(tipoCaixa, versao, flag);
		
		opcolor = new int[]{0,0,0};
	}

}
