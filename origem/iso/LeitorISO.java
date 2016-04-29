package iso;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import iso.caixas.ArquivoISO;
import iso.caixas.Caixa;
import iso.caixas.CaixaCabecalhoFilme;
import iso.caixas.CaixaDadosMidia;
import iso.caixas.CaixaFilme;
import iso.caixas.CaixaTipoArquivo;
import iso.caixas.mp4.CaixaDescritorObjeto;
import iso.caixas.mp4.DecritorObjetoInicial;
import iso.caixas.mp4.DescritorObjeto;

public class LeitorISO {
	private FileInputStream en;

	public LeitorISO(File arquivo) {
		try {
			en = new FileInputStream(arquivo);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ArquivoISO processa() {
		ArquivoISO arquivo = new ArquivoISO();
		try {
			arquivo.caixas.add(leCaixa());
			leCaixa();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return arquivo;
	}

	// Numero sem sinal de 32 bits
	private long leNumero32(InputStream en) throws IOException {
		byte[] quatroBytes = new byte[4];
		en.read(quatroBytes);
		long tamanho = (quatroBytes[0] << 24) + (quatroBytes[1] << 16) + (quatroBytes[2] << 8) + (quatroBytes[3]);
		return tamanho;// & 0xFFFFFFFFl;
	}

	private String leString32(InputStream en) throws IOException {
		byte[] quatroBytes = new byte[4];
		en.read(quatroBytes);
		return new String(quatroBytes);
	}

	public Caixa leCaixa() throws IOException {
		System.out.println("Lendo caixa");
		
		long tamanho = leNumero32(en);
		System.out.println("Tamanho caixa: " + tamanho+ " bytes");

		if (tamanho == 1) {

		}

		String tipo = leString32(en);
		System.out.println("Tipo: " + tipo);

		if (tipo.equals("ftyp")) {
			CaixaTipoArquivo caixa = new CaixaTipoArquivo();
			caixa.majorBrand = leString32(en);
			caixa.minorVersion = leNumero32(en);
			long bitscompatiblebrands = tamanho * 8 - 128;
			System.out.println("Bits restantes: " + bitscompatiblebrands);

			caixa.compatibleBrands = new String[(int) (bitscompatiblebrands / 32)];
			for (int c = 0; c < caixa.compatibleBrands.length; c++) {
				caixa.compatibleBrands[c] = leString32(en);
			}

			System.out.println(caixa);

			return caixa;
		} else if (tipo.equals("mdat")) {
			// Agora está o Corrego de Bits do video
			CaixaDadosMidia caixa = new CaixaDadosMidia();
			caixa.dados = new byte[(int) tamanho];
			en.read(caixa.dados);
			return caixa;
		} else if (tipo.equals("moov")) {
			CaixaFilme caixaFilme = new CaixaFilme();
			return caixaFilme;
		} else if (tipo.equals("mvhd")) {
			int versao = leNumero8(en);
			System.out.println("Versão: "+versao);
			
			int flag = leNumero24(en);
			//9 b
			CaixaCabecalhoFilme cc = new CaixaCabecalhoFilme(versao,flag);
			if (versao == 1) {
				cc.tempo_criacao = leNumero64(en);
			} else {
				cc.tempo_criacao = leNumero32(en);
				cc.tempo_modificacao = leNumero32(en);
				cc.escala = leNumero32(en);
				System.out.println("Escala: "+cc.escala);
				cc.duracao = leNumero32(en);
				System.out.println("Duração: "+cc.duracao);
				//16 b
			}
			
			//25 b
			
			cc.rate = leNumero32(en);
			cc.volume = leNumero16(en);
			cc.reserved = leNumero16(en);
			cc.reservado[0] = leNumero32(en);
			cc.reservado[1] = leNumero32(en); // 28 b
			// +16 b
			// 41 b
			
			int n = 0;
			
			while(n<9){
				cc.matriz[n] = leNumero32(en); // 74
				n++;
				// 36 b
				
			}
			
			// 77 b
			n = 0;
			while(n<6){
				cc.predefinido[n] = leNumero32(en);
				n++;
				// 24
			}
			
			// 101
			cc.idProximoTrack = leNumero32(en);
			// 105 b
			return cc;
		}else if(tipo.equals("iods")){
			int versao = leNumero8(en);
			int flag= leNumero24(en);
			CaixaDescritorObjeto c = new CaixaDescritorObjeto(versao, flag);
			int tag = leNumero8(en);

			System.out.println(tag);
			
			if(tag == 16){
				DecritorObjetoInicial dei = new DecritorObjetoInicial();
				dei.tag = tag;
				dei.iddescritorobjeto = leN10(en);
				c.deo =dei;
			}
			
		}
		return new Caixa(tipo);
	}

	private int leN10(InputStream en) throws IOException{
		byte[] doisB = new byte[2];
		en.read(doisB);
		int n = (doisB[0]<<2) + doisB[1]; 
		return n;
	}
	
	private int leNumero16(FileInputStream en) throws IOException {
		byte[] quatroBytes = new byte[2];
		en.read(quatroBytes);
		int tamanho = (quatroBytes[0] << 8) + (quatroBytes[1]);
		return tamanho;
	}


	private int leNumero24(FileInputStream en) throws IOException {
		byte[] quatroBytes = new byte[3];
		en.read(quatroBytes);
		int tamanho = (quatroBytes[0] << 16) + (quatroBytes[1]<<8) +quatroBytes[2];
		return tamanho;
	}

	private long leNumero64(FileInputStream en2) throws IOException {
		byte[] oitoBytes = new byte[8];
		en.read(oitoBytes);
		long numero = oitoBytes[0] << 64 +oitoBytes[1]<<56+(oitoBytes[2]<<48)+(oitoBytes[3]<<40)+(oitoBytes[4]<<32)+oitoBytes[5]<<24+oitoBytes[6]<<16;
		//Fazer: terminar
		return numero;
	}

	private int leNumero8(FileInputStream en2) throws IOException {
		byte[] b = new byte[1];
		en.read(b);
		int numero8 = b[0];
		return numero8; 
	}
	
	public void recebeCaixaTipoArquivo() {

	}
}
