package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaMidia extends Caixa {

	public CaixaCabecalhoMidia ccm;
	public CaixaHandlerReferencia hr;
	public CaixaInformacaoMidia cim;

	public CaixaMidia(CaixaCabecalhoMidia ccm, CaixaHandlerReferencia ch, CaixaInformacaoMidia cim) {
		super("mdia");

		this.ccm = ccm;
		this.hr = ch;
		this.cim = cim;

		this.tamanho = 8 + ccm.tamanho + this.hr.tamanho + this.cim.tamanho;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		ccm.salva(csi);
		hr.salva(csi);
		cim.salva(csi);
	}

	public static class CaixaHandlerReferencia extends CaixaCompleta {
		long pre_defined = 0;
		public String handler_type;
		final long[] reserved = new long[] { 0, 0, 0 };
		public String name;

		public CaixaHandlerReferencia() {
			super("hdlr", 0, 0);

			this.tamanho = 12 + 24;
		}

		public void salva(CorregoSaidaISO csi) throws IOException {
			super.salva(csi);
			csi.escreveInt32(pre_defined);
			csi.escreveString32(handler_type);
			csi.escreveInt32(reserved[0]);
			csi.escreveInt32(reserved[1]);
			csi.escreveInt32(reserved[2]);
			csi.escreveString32(name);
		}
	}
}