package iso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import iso.caixas.Caixa;
import iso.caixas.CaixaCabecalhoFilme;
import iso.caixas.CaixaDadosMidia;
import iso.caixas.CaixaFilme;
import iso.caixas.CaixaTipoArquivo;

public class LeitorISO {
	private CorregoEntradaISO en;

	public LeitorISO(File arquivo) {
		try {
			en = new CorregoEntradaISO(new FileInputStream(arquivo));
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

	public Caixa leCaixa() throws IOException {
		System.out.println("Lendo caixa...");

		long tamanho = en.leNumero32();
		System.out.println("Tamanho caixa: " + tamanho + " bytes");

		if (tamanho == 1) {

		}

		String tipo = en.leString32();
		System.out.println("Tipo: " + tipo+".");

		if (tipo.equals("ftyp")) {
			CaixaTipoArquivo caixa = new CaixaTipoArquivo();
			caixa.majorBrand = en.leString32();
			caixa.minorVersion = en.leNumero32();
			long bitscompatiblebrands = tamanho * 8 - 128;
			System.out.println("Bits restantes: " + bitscompatiblebrands);

			caixa.compatibleBrands = new String[(int) (bitscompatiblebrands / 32)];
			for (int c = 0; c < caixa.compatibleBrands.length; c++) {
				caixa.compatibleBrands[c] = en.leString32();
			}

			System.out.println(caixa);

			return caixa;
		} else if (tipo.equals("mdat")) {
			// Agora está o Corrego de Bits do video
			CaixaDadosMidia caixa = new CaixaDadosMidia();
			caixa.dados = new byte[(int) tamanho];
			en.leTotal(caixa.dados);
			return caixa;
		} else if (tipo.equals("moov")) {
			CaixaFilme caixaFilme = new CaixaFilme(new CaixaCabecalhoFilme(0));
			return caixaFilme;
		} else if (tipo.equals("mvhd")) {
			int versao = en.leNumero8();
			System.out.println("Versão: " + versao);

			int flag = en.leNumero24();
			// 9 b
			CaixaCabecalhoFilme cc = new CaixaCabecalhoFilme(versao);
			if (versao == 1) {
				cc.tempo_criacao = en.leNumero64();
			} else {
				cc.tempo_criacao = en.leNumero32();
				cc.tempo_modificacao = en.leNumero32();
				cc.escala = en.leNumero32();
				System.out.println("Escala: " + cc.escala);
				cc.duracao = en.leNumero32();
				System.out.println("Duração: " + cc.duracao);
				// 16 b
			}

			// 25 b

			cc.rate = en.leNumero32();
			cc.volume = en.leNumero16();
			cc.reserved = en.leNumero16();
			cc.reservado[0] = en.leNumero32();
			cc.reservado[1] = en.leNumero32(); // 28 b
			// +16 b
			// 41 b

			int n = 0;

			while (n < 9) {
				cc.matriz[n] = en.leNumero32(); // 74
				n++;
				// 36 b

			}

			// 77 b
			n = 0;
			while (n < 6) {
				cc.predefinido[n] = en.leNumero32();
				n++;
				// 24
			}

			// 101
			cc.idProximoTrack = en.leNumero32();
			// 105 b
			return cc;
		}

		// Fazer: pular iods

		if (tipo.equals("tkhd") || tipo.equals("mdhd")) {
			en.pula(tamanho-8);
		}
		
		return new Caixa(tipo);
	}

	public void recebeCaixaTipoArquivo() {

	}
}
