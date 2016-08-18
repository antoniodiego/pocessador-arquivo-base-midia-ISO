package iso.caixas;

public class CaixaDescricaoSample extends CaixaCompleta {

	public long entradas;
	public EntradaSample[] es;
	
	public CaixaDescricaoSample(long tipoHandler,int versao,int bandeira) {
		super("stsd", versao, bandeira);
	}

}
