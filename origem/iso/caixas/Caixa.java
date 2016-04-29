package iso.caixas;

public class Caixa {
	int tamanho;
	public String tipo;
	long tamanhoExtendido;

	public Caixa(String tipoCaixa) {
		this.tipo = tipoCaixa;
	}

	/* Tipo caixa 4 bytes */
	public Caixa(String tipoCaixa, byte[] tipoextendido) {
		this.tipo = tipoCaixa;

		if (tamanho == 1) {
			long tamanhoExtendido;
		}

		if (tamanho == 0) {
			// Fim arquivo
		}

		if (tipo.equals("uuid")) {
			byte[] user_type = tipoextendido;
		}
	}

	@Override
	public String toString() {
		return tipo;
	}
}