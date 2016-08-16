package iso;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JTextArea;

import iso.caixas.Caixa;
import iso.caixas.CaixaCabecalhoFilme;
import iso.caixas.CaixaCabecalhoMidia;
import iso.caixas.CaixaCabecalhoMidiaNulo;
import iso.caixas.CaixaCabecalhoMidiaVideo;
import iso.caixas.CaixaCabecalhoTrack;
import iso.caixas.CaixaDadosMidia;
import iso.caixas.CaixaDescricaoSample;
import iso.caixas.CaixaEntradaDadosUrl;
import iso.caixas.CaixaEntradaDadosUrn;
import iso.caixas.CaixaFilme;
import iso.caixas.CaixaInformacaoMidia;
import iso.caixas.CaixaInformaçãoDados;
import iso.caixas.CaixaMidia;
import iso.caixas.CaixaReferenciaDados;
import iso.caixas.CaixaTabelaSamples;
import iso.caixas.CaixaMidia.CaixaHandler;
import iso.caixas.CaixaTipoArquivo;
import iso.caixas.CaixaTrack;
import iso.caixas.EntradaSample;
import iso.caixas.EntradaSampleMPDVisual;

public class LeitorISO {
	private CorregoEntradaISO en;
	// private long tamanho;
	// private String tipo;

	public LeitorISO(File arquivo) {
		try {
			en = new CorregoEntradaISO(new FileInputStream(arquivo));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public ArquivoISO processa(JTextArea registro) {
		ArquivoISO arquivo = new ArquivoISO();

		// Fazer caixas podem conter outras
		// CaixaTipoArquivo

		DataInputStream enD = new DataInputStream(en);

		try {
			int leituraCaixas = 0;
			while (leituraCaixas < 14) {
				Cabecalho cabecalho = leCabecalho();
				// bytesLidos = 8;

				if (cabecalho.tipo.equalsIgnoreCase("ftyp")) {
					// CaixaTipoArquivo
					CaixaTipoArquivo caixaTipoArquivo = new CaixaTipoArquivo();
					caixaTipoArquivo.setMajorBrand(en.leString32());
					caixaTipoArquivo.setMinorVersion(en.leNumero32());

					int bytesRestantes = (int) (cabecalho.tamanho - 16);
					// System.out.println("Bytes restantes: " + bytesRestantes);

					// long bitscompatiblebrands = cabecalho.tamanho * 8 - 128;
					// System.out.println("Bits restantes: " +
					// bitscompatiblebrands);

					String[] cb = new String[bytesRestantes / 4];
					for (int c = 0; c < cb.length; c++) {
						cb[c] = en.leString32();
					}

					caixaTipoArquivo.setCompatibleBrands(cb);
					// System.out.println(caixaTipoArquivo);

					arquivo.adicionaCaixa(caixaTipoArquivo);

					registro.append("Brand maior: " + caixaTipoArquivo.recebeMajorBrand() + "\n");
					registro.append("Versão menor: " + caixaTipoArquivo.getMinorVersion() + "\n");
					registro.append("Brands compatíveis: ");
					String[] brComp = caixaTipoArquivo.getCompatibleBrands();
					for (int c = 0; c < brComp.length; c++) {
						registro.append(brComp[c] + " ");
					}

					registro.append("\n");

					// registro.append(");
				} else if (cabecalho.tipo.equalsIgnoreCase("moov")) {
					CaixaFilme caixaFilme = new CaixaFilme();
					int bytesLidos = 8;
					while (bytesLidos < cabecalho.tamanho) {
						Cabecalho cabecalhoB = leCabecalho();
						if (cabecalhoB.tipo.equals("mvhd")) {
							int versao = en.leNumero8();
							int bandeira = en.leNumero24();

							CaixaCabecalhoFilme cabecalhoFilme = new CaixaCabecalhoFilme(versao, bandeira);

							// bytesLidos += 4;

							System.out.println("Cabecalho filme:");
							System.out.println("Versão: " + versao);
							System.out.println("Bandeira: " + bandeira);

							// 9 b
							// CaixaCabecalhoFilme cc = new
							// CaixaCabecalhoFilme(versao);
							if (versao == 1) {
								cabecalhoFilme.tempo_criacao = en.leNumero64();
							} else {
								// Tempo criacão em segundos
								cabecalhoFilme.tempo_criacao = en.leNumero32();
								// Calendar.getInstance().
								cabecalhoFilme.tempo_modificacao = en.leNumero32();
								cabecalhoFilme.escala = en.leNumero32();
								// System.out.println("Escala: " +
								// cabecalhoFilme.escala);
								cabecalhoFilme.duracao = en.leNumero32();
								// System.out.println("Duração: " +
								// cabecalhoFilme.duracao);
								registro.append(
										"Duração: " + (cabecalhoFilme.duracao / cabecalhoFilme.escala) + " s\n");
								// bytesLidos += 16;
								// 16 b
							}

							// 25 b

							cabecalhoFilme.rate = en.leNumero32();
							cabecalhoFilme.volume = en.leNumero16();
							cabecalhoFilme.reserved = en.leNumero16();
							cabecalhoFilme.reservado[0] = en.leNumero32();
							cabecalhoFilme.reservado[1] = en.leNumero32();
							// bytesLidos += 16;
							// 28 b
							// +16 b
							// 41 b

							int n = 0;

							while (n < 9) {
								cabecalhoFilme.matriz[n] = en.leNumero32(); // 74
								n++;
								// 36 b
							}

							// bytesLidos += 36;

							// 77 b
							n = 0;
							while (n < 6) {
								cabecalhoFilme.predefinido[n] = en.leNumero32();
								n++;
								// 24
							}

							// bytesLidos += 24;

							// 101
							cabecalhoFilme.idProximoTrack = en.leNumero32();
							registro.append("ID Próximo Track: " + cabecalhoFilme.idProximoTrack);
							// bytesLidos += 4;
							// 105 b
							caixaFilme.adiciona(cabecalhoFilme);
						} else if (cabecalhoB.tipo.equals("trak")) {
							CaixaTrack ct = new CaixaTrack();
							int lidoTrack = 8;
							while (lidoTrack < cabecalhoB.tamanho) {
								Cabecalho caixaSubTrak = leCabecalho();
								if (caixaSubTrak.tipo.equals("tkhd")) {
									CaixaCabecalhoTrack cct = new CaixaCabecalhoTrack(en.leNumero8(), en.leNumero24());
									System.out.println("Lendo cabeçalho de track.");
									if (cct.versao == 1) {

									} else if (cct.versao == 0) {
										// Tempo criacão em segundos
										cct.tempo_criacao = en.leNumero32();
										// Calendar.getInstance().
										cct.tempo_modificacao = en.leNumero32();
										cct.idTrack = en.leNumero32();
										System.out.println("Id trak: " + cct.idTrack);
										cct.reservado1 = en.leNumero32();
										cct.duracao = en.leNumero32();
										// System.out.println("Duração: " +
										// cct.duracao);
									}
									cct.reservado[0] = en.leNumero32();
									cct.reservado[1] = en.leNumero32();

									// Modelos
									cct.camada = en.leNumero16();
									cct.grupoAlternado = en.leNumero16();
									cct.volume = en.leNumero16();

									cct.reserved = en.leNumero16();

									int nB = 0;

									while (nB < 9) {
										cct.matriz[nB] = en.leNumero32(); // 74
										nB++;
										// 36 b
									}

									cct.largura = en.leNumero32();
									cct.altura = en.leNumero32();

									// lido += cct.tamanho;
									ct.adiciona(ct);
								} else if (caixaSubTrak.tipo.equals("mdia")) {
									int lidoΜidia = 8;
									CaixaMidia cm = new CaixaMidia();
									System.out.println("Caixa midia.");
									long tipoHandlerA = 0;
									while (lidoΜidia < caixaSubTrak.tamanho) {
										Cabecalho caixaSubMidia = leCabecalho();
										if (caixaSubMidia.tipo.equals("mdhd")) {
											CaixaCabecalhoMidia ccM = new CaixaCabecalhoMidia(en.leNumero8(),
													en.leNumero24());
											System.out.println("Versão: " + ccM.versao);
											if (ccM.versao == 0) {
												ccM.tempo_criacao_32 = en.leNumero32();
												ccM.tempo_modificacao = en.leNumero32();
												ccM.escala = en.leNumero32();
												ccM.duracao = en.leNumero32();
											}
											int jf = en.leNumero16();
											ccM.pad_1 = (byte) (jf >> 15);
											int bitsLinguagem = jf & Integer.parseInt("111111111111111", 2);
											ccM.language_5 = new byte[] { (byte) (bitsLinguagem >> 10),
													(byte) ((bitsLinguagem >> 5) & Integer.parseInt("11111", 2)),
													(byte) (bitsLinguagem & Integer.parseInt("11111", 2)) };
											ccM.predefinido_16 = en.leNumero16();
											cm.adiciona(ccM);
											// lido += cB.tamanho;
										} else if (caixaSubMidia.tipo.equals("hdlr")) {
											CaixaHandler hdl = new CaixaHandler(en.leNumero8(), en.leNumero24());
											hdl.pre_defined = en.leNumero32();
											hdl.handler_type = en.leNumero32();
											byte[] bytesH = { (byte) (hdl.handler_type >> 24),
													(byte) (hdl.handler_type >> 16), (byte) (hdl.handler_type >> 8),
													(byte) hdl.handler_type };
											System.out.println(
													"Tipo handler: " + hdl.handler_type + " " + new String(bytesH));
											tipoHandlerA = hdl.handler_type;
											hdl.reserved[0] = en.leNumero32();
											hdl.reserved[1] = en.leNumero32();
											hdl.reserved[2] = en.leNumero32();
											StringBuffer bNome = new StringBuffer();
											char c = (char) en.read();
											while (c != '\0') {
												bNome.append(c);
												c = (char) en.read();
											}
											hdl.name = bNome.toString();
											System.out.println("Nome track: " + hdl.name);
											cm.adiciona(hdl);
											// lido += cB.tamanho;
											// if (hdl.name.contains("Vid")) {
											// return arquivo;
											// }
										} else if (caixaSubMidia.tipo.equals("minf")) {
											CaixaInformacaoMidia cim = new CaixaInformacaoMidia();
											int lidosInfo = 8;
											while (lidosInfo < caixaSubMidia.tamanho) {
												System.out.println("Lendo caixa sub info midia.");

												Cabecalho caixaSubInfo = leCabecalho();
												if (caixaSubInfo.tipo.equals("nmhd")) {
													CaixaCabecalhoMidiaNulo cn = new CaixaCabecalhoMidiaNulo(
															caixaSubInfo.tipo, en.leNumero8(), en.leNumero24());
													cim.adiciona(cn);
												} else if (caixaSubInfo.tipo.equals("dinf")) {
													CaixaInformaçãoDados cid = new CaixaInformaçãoDados();
													// Fazer: completar
													Cabecalho subInfo = leCabecalho();
													if (subInfo.tipo.equals("dref")) {
														CaixaReferenciaDados cd = new CaixaReferenciaDados("dref",
																en.leNumero8(), en.leNumero24());
														cd.mudaTotalEntradas(en.leNumero32());
														int total = (int) cd.recebeTotalEntradas();
														System.out
																.println("Total entradas: " + cd.recebeTotalEntradas());

														for (int c = 1; c <= total; c++) {
															Cabecalho subRef = leCabecalho();
															if (subRef.tipo.equals("url ")) {
																CaixaEntradaDadosUrl entradaDados = new CaixaEntradaDadosUrl(
																		en.leNumero8(), en.leNumero24());
																System.out.println(entradaDados.versao + " "
																		+ entradaDados.bandeira);
																if (entradaDados.bandeira != 1) {
																	StringBuffer burl = new StringBuffer();
																	char car = (char) en.read();
																	while (car != '\0') {
																		burl.append(car);
																		car = (char) en.read();
																	}
																	entradaDados.mudaLocal(burl.toString());
																	System.out.println(
																			"Local: " + entradaDados.recebeLocal());
																	// cm.adiciona(hdl);
																	// lido +=
																	// cB.tamanho;
																	// if
																	// (hdl.name.contains("Vid"))
																	// {
																	// return
																	// arquivo;
																	// }
																}
																cd.adiciona(entradaDados);
															} else if (subRef.tipo.equals("urn ")) {
																CaixaEntradaDadosUrn url = new CaixaEntradaDadosUrn(
																		en.leNumero8(), en.leNumero16());
																url.nome = leStringFNulo();
																url.mudaLocal(leStringFNulo());
																cd.adiciona(url);
															} else {
																en.pula(subRef.tamanho - 8);
															}
														}
													}
													cim.adiciona(cid);
												} else if (caixaSubInfo.tipo.equals("vmhd")) {
													CaixaCabecalhoMidiaVideo cmv = new CaixaCabecalhoMidiaVideo(
															caixaSubInfo.tipo, en.leNumero8(), en.leNumero24());
													cmv.gfmode = en.leNumero16();
													cmv.opcolor[0] = en.leNumero16();
													cmv.opcolor[1] = en.leNumero16();
													cmv.opcolor[2] = en.leNumero16();
													cim.adiciona(cmv);
												} else if (caixaSubInfo.tipo.equals("stbl")) {
													System.out.println("Caixa tabela samples.");
													CaixaTabelaSamples tabelaSamples = new CaixaTabelaSamples();
													int lidoSample = 8;
													while (lidoSample < caixaSubInfo.tamanho) {
														Cabecalho cd = leCabecalho();
														if (cd.tipo.equals("stsd")) {
															CaixaDescricaoSample desc = new CaixaDescricaoSample(
																	tipoHandlerA, en.leNumero8(), en.leNumero24());
															desc.entradas = en.leNumero32();
															EntradaSample es = null;
															for (int c = 1; c <= desc.entradas; c++) {
																Cabecalho cSample = leCabecalho();
																// System.out.println(cSample.tipo);

																if (cSample.tipo.equals("mp4v")) {
																	EntradaSampleMPDVisual mpd;
																}

																desc.adiciona(es);
															}

															tabelaSamples.adiciona(desc);
														}
														lidoSample += cd.tamanho;
													}
												} else {
													en.pula(caixaSubInfo.tamanho - 8);
												}
												lidosInfo += caixaSubInfo.tamanho;
											}
											cm.adiciona(cim);
											// lido += cB.tamanho;
											// if(c)
										} else {
											en.pula(caixaSubMidia.tamanho - 8);
										}
										lidoΜidia += caixaSubMidia.tamanho;
									}
									System.out.println("Fim caixa mídia");
								} else {
									en.pula(caixaSubTrak.tamanho - 8);
								}
								lidoTrack += caixaSubTrak.tamanho;
							}
						}
						bytesLidos += cabecalhoB.tamanho;
					}
				} else if (cabecalho.tipo.equalsIgnoreCase("free")) {
					// CaixaEspacoLivre
					byte[] free = new byte[(int) (cabecalho.tamanho - 8)];
					enD.readFully(free);
				} else if (cabecalho.tipo.equalsIgnoreCase("mdat")) {
					CaixaDadosMidia dadosMidia = new CaixaDadosMidia();
					// ByteArrayOutputStream saida = new
					// ByteArrayOutputStream();

					long bm = cabecalho.tamanho - 8;
					// System.out.println("Bytes midia: " + bm);

					byte[] buf = new byte[(int) bm];
					enD.readFully(buf);

					// int lido;
					// int total = 0;
					// while (total<bm) {
					// enD.leTotal(buf);
					// saida.write(buf,0,lido);
					// total += lido;
					// }

					// System.out.println("Total: "+total);

					dadosMidia.dados = buf;
					arquivo.adicionaCaixa(dadosMidia);
				} else {
					// Caixa desconhecida.
					if (cabecalho.tamanho == 0) {
						// Ultima caixae desconhecida
						break;
					} else {
						en.pula(cabecalho.tamanho - 8);
					}
				}
				leituraCaixas++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// while (true) {
		// try {
		// caixa = leCaixa();
		// if (caixa != null) {
		// arquivo.caixas.add(caixa);
		// }
		// } catch (Exception e) {
		// break;
		// }
		// }
		return arquivo;
	}

	// private CaixaTabelaSamples leCaixaTabeleSamples(int tipoHandler) {
	// CaixaTabelaSamples tabelaSamples = new CaixaTabelaSamples();
	// Cabecalho cd = leCabecalho();
	// CaixaDescricaoSample desc = new CaixaDescricaoSample(tipoHandler,
	// en.leNumero8(), en.leNumero24());
	// desc.entradas = en.leNumero32();
	// EntradaSample es = null;
	// for (int c = 1; c <= desc.entradas; c++) {
	// Cabecalho cSample = leCabecalho();
	// System.out.println(cSample.tipo);
	// if (cSample.tipo.equals("")) {
	//
	// }
	// desc.adiciona(es);
	// }
	//
	// tabelaSamples.adiciona(desc);
	// return tabelaSamples;
	// }

	private String leStringFNulo() throws IOException {
		StringBuffer bNome = new StringBuffer();
		char c = (char) en.read();
		while (c != '\0') {
			bNome.append(c);
			c = (char) en.read();
		}
		return bNome.toString();
	}

	public Cabecalho leCabecalho() throws IOException {
		Cabecalho cabecalho = new Cabecalho();
		cabecalho.tamanho = en.leNumero32();
		// System.out.println("Tamanho caixa: " + cabecalho.tamanho + " b");

		if (cabecalho.tamanho == 1) {
			cabecalho.tamanhoLargo = en.leNumero64();
		}

		cabecalho.tipo = en.leString32();
		System.out.println("Tipo caixa: " + cabecalho.tipo + ".");

		// c.tamanho = tamanho;
		// c.tipo = tipo;

		return cabecalho;
	}

	// public Caixa lerCaixa() throws IOException {
	// if (tipo.equals("ftyp")) {
	// CaixaTipoArquivo caixa = new CaixaTipoArquivo();
	// caixa.setMajorBrand(en.leString32());
	// caixa.minorVersion = en.leNumero32();
	// long bitscompatiblebrands = tamanho * 8 - 128;
	// System.out.println("Bits restantes: " + bitscompatiblebrands);
	//
	// caixa.compatibleBrands = new String[(int) (bitscompatiblebrands / 32)];
	// for (int c = 0; c < caixa.compatibleBrands.length; c++) {
	// caixa.compatibleBrands[c] = en.leString32();
	// }
	//
	// System.out.println(caixa);
	//
	// return caixa;
	// } else if (tipo.equals("mdat")) {
	// // Agora está o Corrego de Bits do video
	// CaixaDadosMidia caixa = new CaixaDadosMidia();
	// caixa.dados = new byte[(int) tamanho];
	// en.leTotal(caixa.dados);
	// return caixa;
	// } else if (tipo.equals("moov")) {
	// CaixaFilme caixaFilme = new CaixaFilme();
	// return caixaFilme;
	// } else if (tipo.equals("mvhd")) {
	// int versao = en.leNumero8();
	// int bandeira = en.leNumero24();
	//
	// System.out.println("Versão: " + versao);
	// System.out.println("Bandeira: " + bandeira);
	//
	// // 9 b
	// CaixaCabecalhoFilme cc = new CaixaCabecalhoFilme(versao);
	// if (versao == 1) {
	// cc.tempo_criacao = en.leNumero64();
	// } else {
	// // Tempo criacão em segundos
	// cc.tempo_criacao = en.leNumero32();
	// // Calendar.getInstance().
	// cc.tempo_modificacao = en.leNumero32();
	// cc.escala = en.leNumero32();
	// System.out.println("Escala: " + cc.escala);
	// cc.duracao = en.leNumero32();
	// System.out.println("Duração: " + cc.duracao);
	// // 16 b
	// }
	//
	// // 25 b
	//
	// cc.rate = en.leNumero32();
	// cc.volume = en.leNumero16();
	// cc.reserved = en.leNumero16();
	// cc.reservado[0] = en.leNumero32();
	// cc.reservado[1] = en.leNumero32(); // 28 b
	// // +16 b
	// // 41 b
	//
	// int n = 0;
	//
	// while (n < 9) {
	// cc.matriz[n] = en.leNumero32(); // 74
	// n++;
	// // 36 b
	//
	// }
	//
	// // 77 b
	// n = 0;
	// while (n < 6) {
	// cc.predefinido[n] = en.leNumero32();
	// n++;
	// // 24
	// }
	//
	// // 101
	// cc.idProximoTrack = en.leNumero32();
	// // 105 b
	// return cc;
	// } else if (tipo.equals("trak")) {
	// CaixaTrack ct = new CaixaTrack();
	// return ct;
	// } else if (tipo.equals("tkhd")) {
	// int versao = en.leNumero8();
	// int bandeira = en.leNumero24();
	//
	// System.out.println("Versão: " + versao);
	// System.out.println("Bandeira: " + bandeira);
	//
	// CaixaCabecalhoTrack cct = new CaixaCabecalhoTrack(versao, bandeira);
	// if (versao == 0) {
	// // Tempo criacão em segundos
	// cct.tempo_criacao = en.leNumero32();
	// // Calendar.getInstance().
	// cct.tempo_modificacao = en.leNumero32();
	// cct.idTrack = en.leNumero32();
	// System.out.println("Id trak: " + cct.idTrack);
	// cct.reservado1 = en.leNumero32();
	// cct.duracao = en.leNumero32();
	// System.out.println("Duração: " + cct.duracao);
	// }
	//
	// cct.reservado[0] = en.leNumero32();
	// cct.reservado[1] = en.leNumero32();
	//
	// cct.reserved = en.leNumero16();
	// cct.largura = en.leNumero32();
	// cct.altura = en.leNumero32();
	// }
	// return null;
	//
	// }

	// Fazer: precisa ler caixas interiores
	public Caixa leCaixa() throws Exception {
		System.out.println("Lendo caixa...");

		long tamanho = en.leNumero32();
		System.out.println("Tamanho caixa: " + tamanho + " bytes");

		if (tamanho == 1) {

		} else if (tamanho == 0) {
			throw new Exception();
		}

		String tipo = en.leString32();
		System.out.println("Tipo: " + tipo + ".");

		if (tipo.equals("ftyp")) {
			CaixaTipoArquivo caixa = new CaixaTipoArquivo();
			caixa.setMajorBrand(en.leString32());
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
			en.le(caixa.dados);
			return caixa;
		} else if (tipo.equals("moov")) {
			CaixaFilme caixaFilme = new CaixaFilme();
			// Contem outras caixas
			System.out.println("Encotrou caixa filme. Resta: " + (tamanho - 8) + "b");
			Caixa subc = leCaixa();
			if (subc instanceof CaixaCabecalhoFilme) {
				caixaFilme.adiciona(subc);
			} else if (subc instanceof CaixaTrack) {
				caixaFilme.adiciona(subc);
			}
			return caixaFilme;
		} else if (tipo.equals("mvhd")) {
			int versao = en.leNumero8();
			int bandeira = en.leNumero24();

			System.out.println("Versão: " + versao);
			System.out.println("Bandeira: " + bandeira);

			// 9 b
			CaixaCabecalhoFilme cc = new CaixaCabecalhoFilme(versao, bandeira);
			if (versao == 1) {
				cc.tempo_criacao = en.leNumero64();
			} else {
				// Tempo criacão em segundos
				cc.tempo_criacao = en.leNumero32();
				// Calendar.getInstance().
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
		} else if (tipo.equals("trak")) {
			CaixaTrack ct = new CaixaTrack();
			return ct;
		} else if (tipo.equals("tkhd")) {
			int versao = en.leNumero8();
			int bandeira = en.leNumero24();

			System.out.println("Versão: " + versao);
			System.out.println("Bandeira: " + bandeira);

			CaixaCabecalhoTrack cct = new CaixaCabecalhoTrack(versao, bandeira);
			if (versao == 1) {

			}
			if (versao == 0) {
				// Tempo criacão em segundos
				cct.tempo_criacao = en.leNumero32();
				// Calendar.getInstance().
				cct.tempo_modificacao = en.leNumero32();
				cct.idTrack = en.leNumero32();
				System.out.println("Id trak: " + cct.idTrack);
				cct.reservado1 = en.leNumero32();
				cct.duracao = en.leNumero32();
				System.out.println("Duração: " + cct.duracao);
			}
			cct.reservado[0] = en.leNumero32();
			cct.reservado[1] = en.leNumero32();

			// Modelos
			cct.camada = en.leNumero16();
			cct.grupoAlternado = en.leNumero16();
			cct.volume = en.leNumero16();

			cct.reserved = en.leNumero16();

			int n = 0;

			while (n < 9) {
				cct.matriz[n] = en.leNumero32(); // 74
				n++;
				// 36 b
			}

			cct.largura = en.leNumero32();
			cct.altura = en.leNumero32();
			return cct;
		} else {

			// Caixa de tipo desconhecidom deve ser ignorada e pulada.
			en.pula(tamanho - 8);

			// Caixa c = leCaixa();

			// Fazer: pular iods

			// if (tipo.equals("tkhd") || tipo.equals("mdhd")) {
			// en.pula(tamanho - 8);
			// }

			return null;
		}
	}

	public void recebeCaixaTipoArquivo() {

	}
}
