package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaMidia extends Caixa {

	public CaixaCabecalhoMidia ccm;
	public CaixaHandler hr;
	public CaixaInformacaoMidia cim;

	public CaixaMidia(CaixaCabecalhoMidia ccm, CaixaHandler ch, CaixaInformacaoMidia cim) {
		super("mdia");

		this.ccm = ccm;
		this.hr = ch;
		this.cim = cim;

		this.tamanho = 8 + ccm.tamanho + this.hr.tamanho + this.cim.tamanho;
	}

	public CaixaMidia() {
		super("mdia");
	}

	public CaixaCabecalhoMidia getCcm() {
		return ccm;
	}

	public void setCcm(CaixaCabecalhoMidia ccm) {
		this.ccm = ccm;
		this.tamanho += ccm.tamanho;
	}

	public CaixaHandler getHr() {
		return hr;
	}

	public void setHr(CaixaHandler hr) {
		this.hr = hr;
		this.tamanho += hr.tamanho;
	}

	public CaixaInformacaoMidia getCim() {
		return cim;
	}

	public void setCim(CaixaInformacaoMidia cim) {
		this.cim = cim;
		this.tamanho += cim.tamanho;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		try {
			ccm.salva(csi);
			hr.salva(csi);
			cim.salva(csi);
		} catch (NullPointerException e) {
			throw e;
		}
	}

	public static class CaixaHandler extends CaixaCompleta {
		public long pre_defined = 0;
		public long handler_type;
		public final long[] reserved = new long[] { 0, 0, 0 };
		public String name;

		public CaixaHandler(int versao, int bandeira) {
			super("hdlr", versao, bandeira);
			this.tamanho = 12 + 24;
		}

		public void salva(CorregoSaidaISO csi) throws IOException {
			super.salva(csi);
			csi.escreveInt32(pre_defined);
			csi.escreveInt32(handler_type);
			csi.escreveInt32(reserved[0]);
			csi.escreveInt32(reserved[1]);
			csi.escreveInt32(reserved[2]);
			csi.escreveString32(name);
		}
	}
}