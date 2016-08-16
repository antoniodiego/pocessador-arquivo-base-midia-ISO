package iso.caixas;

public class CaixaReferenciaDados extends CaixaCompleta {

	private long total;

	public CaixaReferenciaDados(String tipoCaixa, int versao, int flag) {
		super(tipoCaixa, versao, flag);
	}

	public void mudaTotalEntradas(long leNumero32) {
		this.total = leNumero32;
	}

	public long recebeTotalEntradas() {
		return total;
	}
}
