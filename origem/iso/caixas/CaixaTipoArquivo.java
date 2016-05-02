package iso.caixas;

import java.io.IOException;

import iso.CorregoSaidaISO;

public class CaixaTipoArquivo extends Caixa {
	public String majorBrand;
	// int 32 sem sinal
	public long minorVersion;
	public String[] compatibleBrands;

	public CaixaTipoArquivo() {
		super("ftyp");
	}

	public String toString() {
		StringBuilder constroi = new StringBuilder();
		constroi.append("MajorBrand ").append(majorBrand).append("\n");
		constroi.append("minorVersion ").append(minorVersion).append("\n");

		for (int c = 0; c < compatibleBrands.length; c++) {
			constroi.append("Compativel Brands " + compatibleBrands[c] + "\n");
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