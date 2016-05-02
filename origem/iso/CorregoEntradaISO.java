package iso;

import java.io.IOException;
import java.io.InputStream;

public class CorregoEntradaISO {

	private InputStream en;

	public CorregoEntradaISO(InputStream en) {
		this.en = en;
	}

	public int leNumero24() throws IOException {
		byte[] quatroBytes = new byte[3];
		en.read(quatroBytes);
		int tamanho = (quatroBytes[0] << 16) + (quatroBytes[1] << 8) + quatroBytes[2];
		return tamanho;
	}

	public long leNumero64() throws IOException {
		byte[] oitoBytes = new byte[8];
		en.read(oitoBytes);
		long numero = oitoBytes[0] << 64 + oitoBytes[1] << 56 + (oitoBytes[2] << 48) + (oitoBytes[3] << 40)
				+ (oitoBytes[4] << 32) + oitoBytes[5] << 24 + oitoBytes[6] << 16;
		// Fazer: terminar
		return numero;
	}

	public int leNumero8() throws IOException {
		byte[] b = new byte[1];
		en.read(b);
		int numero8 = b[0];
		return numero8;
	}

	public int leN10() throws IOException {
		byte[] doisB = new byte[2];
		en.read(doisB);
		int n = (doisB[0] << 2) + doisB[1];
		return n;
	}

	public int leNumero16() throws IOException {
		byte[] quatroBytes = new byte[2];
		en.read(quatroBytes);
		int tamanho = (quatroBytes[0] << 8) + (quatroBytes[1]);
		return tamanho;
	}

	public String leString32() throws IOException {
		byte[] quatroBytes = new byte[4];
		en.read(quatroBytes);
		return new String(quatroBytes);
	}

	// Numero sem sinal de 32 bits
	public long leNumero32() throws IOException {
		byte[] quatroBytes = new byte[4];
		en.read(quatroBytes);
		long tamanho = ((quatroBytes[0]&0xff) << 24)  + ((quatroBytes[1]&0xff) << 16) + ((quatroBytes[2]&0xff) << 8) + (quatroBytes[3]&0xff);
		return tamanho;
	}

	public void leTotal(byte[] dados) throws IOException {
		en.read(dados);
	}

	public void pula(long n) throws IOException {
		en.skip(n);
	}
}
