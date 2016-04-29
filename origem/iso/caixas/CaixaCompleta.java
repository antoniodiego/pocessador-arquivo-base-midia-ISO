package iso.caixas;

public class CaixaCompleta extends Caixa {

	//1 byte
	public int versao;
	public int bandeira;

	public CaixaCompleta(String tipoCaixa, int versao, int flag) {
		super(tipoCaixa);
		this.versao = versao;
		this.bandeira = flag;
	}

}
