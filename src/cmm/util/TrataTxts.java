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

	String pastaOrigem = "C:\\Temp\\dados_livro_prestador\\";
	String pastaDestino = "C:\\Temp\\dados_livro_prestador\\tratados\\";
	
	
	
	public static void main(String args[]){
		new TrataTxts().processa();
		TrataTxts t = new TrataTxts();
		String a = "@@|@@|";
		System.out.println(a);
		System.out.println(t.preparaParaSplit(a));
	}
	
	public void processa(){
		File dirOrigem = new File(pastaOrigem);
		File dirDestino = new File(pastaDestino);
		dirDestino.mkdirs();
		File[] listaArquivos = dirOrigem.listFiles();
		for (File arqOrigem : listaArquivos){
			switch (arqOrigem.getName()) {
				case "dados_livro_prestador.txt":
					lerArquivo(arqOrigem, 63);
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
						if (contaLinhas==8304){
							System.out.println();
						}
						String linha = br.readLine();
						if (linha == null || linha.trim().isEmpty()){
							continue;
						}
						linha = preparaParaSplit(linha);
						linhaDefinitiva = new StringBuilder(linhaDefinitiva.toString() + linha);
						campos = linhaDefinitiva.toString().split("@@\\|");
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
		while (linha.contains("@@\"|")){
			linha = linha.replace("@@\"|", "@@|");
		}
		while (linha.contains("@@|@@|")) {
			linha = linha.replace("@@|@@|", "@@| @@|");
		}
		try {
			String ultCarac = linha.substring(linha.length() - 1);
			if (ultCarac.equals("@@|")) {
				linha = linha + " ";
			}
		} catch (Exception e) {

		}
		return linha;
	}
}
