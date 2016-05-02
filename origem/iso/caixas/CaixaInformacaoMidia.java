package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaInformacaoMidia extends Caixa {
	public CaixaCabecalhoMidiaNula ccn;
	public CaixaTabelaSamples ct;
	public CaixaCabecalhoMidiaSom cs;

	public CaixaInformacaoMidia(CaixaTabelaSamples ct,CaixaCabecalhoMidiaSom cs) {
		super("minf");

		this.ct = ct;
		this.cs = cs;
		this.tamanho = 8 + ct.tamanho + cs.tamanho;
	}

	public void salva(CorregoSaidaISO csi) throws IOException{
		super.salva(csi);
		ct.salva(csi);
		cs.salva(csi);
	}
}
