package iso.caixas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import iso.CorregoSaidaISO;

public class Caixa {
	public long tamanho;
	public String tipo;
	long tamanhoExtendido;
	byte[] user_type;

	protected List<Caixa> caixas;

	public Caixa(String tipoCaixa) {
		this.tipo = tipoCaixa;
		this.tamanho = 8;
		caixas = new ArrayList<>();
	}

	public Caixa recebeCaixa(String tipo) {
		Caixa ca;
		for (int c = 0; c < caixas.size(); c++) {
			ca = caixas.get(c);
			if (ca.tipo.equals(tipo)) {
				return ca;
			}
		}
		return null;
	}

	public void adiciona(Caixa c) {
		caixas.add(c);
		this.tamanho += c.tamanho;
	}

	/* Tipo caixa 4 bytes */
	public Caixa(String tipoCaixa, byte[] tipoextendido) {
		this.tipo = tipoCaixa;

		if (tamanho == 0) {
			// Fim arquivo
		}

		if (tipo.equals("uuid")) {
			user_type = tipoextendido;
		}
	}

	@Override
	public String toString() {
		return tipo;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		System.out.println("Salvando caixa. Tamanho: " + tamanho);
		csi.escreveInt32(tamanho);
		csi.escreveString32(tipo);
	}

	
}