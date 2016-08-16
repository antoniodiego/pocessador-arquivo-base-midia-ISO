package iso.caixas;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import iso.CorregoSaidaISO;

public class CaixaFilme extends Caixa {

//public CaixaCabecalhoFilme ccf;
	private List<CaixaTrack> tracks;
	private List<Caixa> caixas;

	public CaixaFilme() {
		super("moov");
		//this.ccf
		this.tamanho = 8;
		tracks = new ArrayList<>();
		caixas = new ArrayList<>();
	}

//	public void mudaCabecalh(CaixaCabecalhoFilme cabecalho){
//		this.ccf = cabecalho;
//	}
	
	public ArrayList<CaixaTrack> recebeTraks(){
		ArrayList<CaixaTrack> lista = new ArrayList<>();
		Caixa ca;
		for(int c=0;c<caixas.size();c++){
			ca = caixas.get(c);
			if(ca.tipo.equals("trak")){
				lista.add((CaixaTrack) ca);
			}
		}
		return lista;
	}
	
	public void adicionaTrack(CaixaTrack track) {
		tracks.add(track);
		this.tamanho += track.tamanho;
	}

	public void salva(CorregoSaidaISO csi) throws IOException {
		super.salva(csi);
		//ccf.salva(csi);
		for (int c = 0; c < tracks.size(); c++) {
			tracks.get(c).salva(csi);
		}
		
		for (int c = 0; c < caixas.size(); c++) {
			caixas.get(c).salva(csi);
		}
	}
}
