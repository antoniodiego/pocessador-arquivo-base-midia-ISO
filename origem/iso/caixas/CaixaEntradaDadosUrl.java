package iso.caixas;

public class CaixaEntradaDadosUrl extends CaixaCompleta {

	private String local;

	public CaixaEntradaDadosUrl(int versao, int flag) {
		super("url", versao, flag);
	}

	public void mudaLocal(String string) {
		this.local = string;
	}

	public String recebeLocal() {
		return local;
	}

}
