package iso;

import java.util.ArrayList;

import iso.caixas.Caixa;
import iso.caixas.CaixaTipoArquivo;

public class ArquivoISOB {
	public CaixaTipoArquivo caixaTipoArquivo;
	public ArrayList<Caixa> caixas;

	public ArquivoISOB(){
		caixas = new ArrayList<>();
	}
}
