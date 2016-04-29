package iso.caixas;

public class CaixaTipoArquivo extends Caixa {
	public String majorBrand;
	public long minorVersion;
	public String[] compatibleBrands;
	
	public CaixaTipoArquivo() {
		super("ftyp");
	}
	
	public String toString(){
		StringBuilder constroi = new StringBuilder();
		constroi.append("MajorBrand ").append(majorBrand).append("\n");
		constroi.append("minorVersion ").append(minorVersion).append("\n");
		
		for(int c=0;c<compatibleBrands.length;c++){
			constroi.append("Compativel Brands "+compatibleBrands[c]+"\n");
		}
		
		return constroi.toString();
		
	}
}