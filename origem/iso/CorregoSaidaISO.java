package iso;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CorregoSaidaISO {
	private OutputStream out;

	public CorregoSaidaISO(OutputStream out) {
		this.out = out;
		//dou = new DataOutputStream(out);
	}

	public void escreveByte(int b) throws IOException {
		out.write(b);
	}

	public void escreveInt16(long num) throws IOException {
		out.write((byte) num >> 8);
		out.write((byte) num);
	}

	public void escreveInt24(long num) throws IOException {
		out.write((byte) (num >> 16));
		out.write((byte) (num >> 8));
		out.write((byte) (num));
	}

	public void escreveInt32(long num) throws IOException {
		// ultimos 4 bytes
		System.out.println("Escrevendo: " + num);
		out.write((byte) ((num >> 24)) & 0xff);
		out.write((byte) (num >> 16) & 0xff);
		out.write((byte) (num >> 8) & 0xff);
		out.write((byte) (num) & 0xff);
	}

	public void escreveString32(String string) throws IOException {
		byte[] trechoBytes = string.getBytes();
		out.write(trechoBytes, 0, 4);
	}

	public void escreveString(String titulo) throws IOException {
		out.write(titulo.getBytes());
	}

}
