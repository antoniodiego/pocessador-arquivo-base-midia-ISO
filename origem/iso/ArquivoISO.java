package iso;

import java.util.ArrayList;

import iso.caixas.Caixa;

public class ArquivoISO {
	public ArrayList<Caixa> caixas;

	public ArquivoISO(){
		caixas = new ArrayList<>();
	}
	
	public void adicionaCaixa(Caixa c){
		caixas.add(c);
	}
}
