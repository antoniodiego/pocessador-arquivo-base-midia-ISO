package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaTipoArquivo extends Caixa {
	private String majorBrand;

	public String recebeMajorBrand() {
		return majorBrand;
	}

	public void setMajorBrand(String majorBrand) {
		this.majorBrand = majorBrand;
		this.tamanho += majorBrand.length();
	}

	// int 32 sem sinal
	public long minorVersion;

	public long getMinorVersion() {
		return minorVersion;
	}

	public void setMinorVersion(long minorVersion) {
		this.minorVersion = minorVersion;
		this.tamanho += 4;
	}

	public String[] compatibleBrands;

	
	public CaixaTipoArquivo() {
		super("ftyp");
		compatibleBrands = new String[0];
	}

	public String[] getCompatibleBrands() {
		return compatibleBrands;
	}

	public void setCompatibleBrands(String[] compatibleBrands) {
		this.compatibleBrands = compatibleBrands;
		this.tamanho += 4*compatibleBrands.length;
	}

	public String toString() {
		StringBuilder constroi = new StringBuilder();
		constroi.append("Brand maior: ").append(majorBrand).append("\n");
		constroi.append("Versão menor: ").append(minorVersion).append("\n");

		for (int c = 0; c < compatibleBrands.length; c++) {
			constroi.append("Brand compativel: " + compatibleBrands[c] + "\n");
		}

		return constroi.toString();
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);

		csi.escreveString32(majorBrand);
		csi.escreveInt32(minorVersion);

		for (int c = 0; c < compatibleBrands.length; c++) {
			csi.escreveString32(compatibleBrands[c]);
		}
	}
}