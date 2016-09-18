package cmm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class TrataTxts {

	String pastaOrigem = "C:\\Temp\\lagoa\\";
	String pastaDestino = "C:\\Temp\\lagoa\\tratados\\";

	public static void main(String args[]) {
		new TrataTxts().processa();
	}

	public void processa() {
		File dirOrigem = new File(pastaOrigem);
		File dirDestino = new File(pastaDestino);
		dirDestino.mkdirs();
		File[] listaArquivos = dirOrigem.listFiles();
		for (File arqOrigem : listaArquivos) {
			switch (arqOrigem.getName()) {
				case "dados_livro_prestador.txt":
					lerArquivo(arqOrigem, 63);
					break;
				case "dados_cadastro.txt":
					lerArquivo(arqOrigem, 22);
					break;
				case "dados_cadastro_acesso.txt":
					lerArquivo(arqOrigem, 2);
					break;
				case "dados_cadastro_atividade.txt":
					lerArquivo(arqOrigem, 11);
					break;
				case "dados_contador.txt":
					lerArquivo(arqOrigem, 18);
					break;
				case "dados_guia.txt":
					lerArquivo(arqOrigem, 40);
					break;
				case "dados_livro_tomador.txt":
					lerArquivo(arqOrigem, 64);
					break;
				case "plano_conta.txt":
					lerArquivo(arqOrigem, 13);
					break;
				default:
					break;
			}
		}
	}

	public List<String> lerArquivo(File arqOrigem, int qtdeCampos) {
		try {
			BufferedReader br;
			int contaLinhas = 0;
			OutputStreamWriter bo = new OutputStreamWriter(new FileOutputStream(pastaDestino + arqOrigem.getName()), "UTF-8");
			List<String> dadosList = new ArrayList<String>();
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(arqOrigem), "UTF-8"));
				br.readLine(); // cabe�alho
				while (br.ready()) {

					StringBuilder linhaDefinitiva = new StringBuilder();
					String[] campos = {};

					while (campos != null && campos.length < qtdeCampos) {
						contaLinhas++;
						if (contaLinhas == 8304) {
							System.out.println();
						}
						String linha = br.readLine();
						if (linha != null && !linha.trim().isEmpty()) {
							linha = preparaParaSplit(linha);
							linhaDefinitiva = new StringBuilder(linhaDefinitiva.toString() + linha);
							campos = linhaDefinitiva.toString().split("@@|");
						}

					}

					String linhaAux = linhaDefinitiva.toString();
					linhaAux = linhaAux.replace("\"", "");
					dadosList.add(linhaAux);
					bo.write(linhaAux + "\n");

				}
				br.close();
				bo.close();
				return dadosList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String preparaParaSplit(String linha) {
		while (linha.contains("@@\"|")) {
			linha = linha.replace("@@\"|", "@@|");
		}
		while (linha.contains("@@|@@|")) {
			linha = linha.replace("@@|@@|", "@@| @@|");
		}
		try {
			String ultCarac = linha.substring(linha.length() - 3);
			if ("@@|".equals(ultCarac)) {
				linha = linha + " ";
			}
		} catch (Exception e) {

		}
		return linha;
	}

	public String preparaParaSplit2(String linha) {
		while (linha.contains("\\|\\|")) {
			linha = linha.replace("\\|\\||", "\\| \\|");
		}
		try {
			String ultCarac = linha.substring(linha.length() - 3);
			if ("\\|".equals(ultCarac)) {
				linha = linha + " ";
			}
		} catch (Exception e) {

		}
		return linha;
	}
}
