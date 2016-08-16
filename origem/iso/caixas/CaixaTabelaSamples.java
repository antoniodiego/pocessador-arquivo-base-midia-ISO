package iso.caixas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import iso.CorregoSaidaISO;

public class CaixaTabelaSamples extends Caixa {

	public CaixaDescricaoSample ds;
	public CaixaTempoSample tempoS;

	public CaixaTabelaSamples(CaixaDescricaoSample ds, CaixaTempoSample ct) {
		super("stbl");
		this.ds = ds;
		this.tempoS = ct;

		this.tamanho = 8 + ds.tamanho + tempoS.tamanho;
	}

	public CaixaTabelaSamples() {
		super("stbl");
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		ds.salva(csi);
		tempoS.salva(csi);
	}

	public static class CaixaTempoSample extends CaixaCompleta {

		long contadorEntrada;
		public List<Entrada> entradas;

		public CaixaTempoSample(int versao, int flag) {
			super("stts", versao, flag);

			entradas = new ArrayList<Entrada>((int) contadorEntrada);
		}

		public void salva(CorregoSaidaISO csi) throws IOException {
			super.salva(csi);
			csi.escreveInt32(contadorEntrada);

			Entrada en;
			for (int c = 0; c < entradas.size(); c++) {
				en = entradas.get(c);
				csi.escreveInt32(en.contadorSample);
				csi.escreveInt32(en.deltaSample);
			}
		}
	}
}