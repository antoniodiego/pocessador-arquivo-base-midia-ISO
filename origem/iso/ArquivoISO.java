package iso;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import iso.caixas.Caixa;

public class ArquivoISO {
	public ArrayList<Caixa> caixas;

	public ArquivoISO() {
		caixas = new ArrayList<>();
	}

	public void adicionaCaixa(Caixa c) {
		caixas.add(c);
	}

	public Caixa recebe(String string) {
		Caixa ca;
		for (int c = 0; c < caixas.size(); c++) {
			ca = caixas.get(c);
			if (ca.tipo.equalsIgnoreCase(string)) {
				return ca;
			}
		}
		return null;
	}

	public void salva(OutputStream out) throws IOException {
		CorregoSaidaISO csi = new CorregoSaidaISO(out);
		Caixa caixa;
		for (int c = 0; c < caixas.size(); c++) {
			caixa = caixas.get(c);
			caixa.salva(csi);
		}
	}
}
