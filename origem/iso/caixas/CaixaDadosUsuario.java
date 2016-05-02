package iso.caixas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import iso.CorregoSaidaISO;

public class CaixaDadosUsuario extends Caixa {

	public List<Caixa> caixas;

	public CaixaDadosUsuario() {
		super("udta");
		caixas = new ArrayList<>();
	}

	public void adicionaCaixa(Caixa c) {
		this.caixas.add(c);
		this.tamanho += c.tamanho;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		for (int c = 0; c < caixas.size(); c++) {
			caixas.get(c).salva(csi);
		}
	}

	public static class CaixaTitulo extends CaixaCompleta {

		int pad = 0;
		public int[] linguagem = new int[]{'p','o','r'};
		private String titulo="";

		public CaixaTitulo() {
			super("titl", 0, 0);
			this.tamanho += 2+titulo.length();
		}

		public void mudaTitulo(String titulo){
			this.titulo = titulo;
			this.tamanho+=titulo.length();
		}
		
		public void salva(CorregoSaidaISO csi) throws IOException {
			super.salva(csi);
			csi.escreveByte(pad);
			for (int i = 0; i < linguagem.length; i++) {
				csi.escreveByte(linguagem[i]);
			}
			csi.escreveString(titulo);
		}
	}
}
