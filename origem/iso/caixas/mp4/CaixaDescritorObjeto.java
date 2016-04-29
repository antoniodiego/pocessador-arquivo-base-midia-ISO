package iso.caixas.mp4;

import iso.caixas.CaixaCompleta;

public class CaixaDescritorObjeto extends CaixaCompleta {
	public DescritorObjeto deo;

	public CaixaDescritorObjeto(int versao, int flag) {
		super("iods", versao, flag);
	}

}
