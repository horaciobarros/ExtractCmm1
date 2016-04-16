package cmm.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainTest {

	public static void main(String args[]) {

		MainTest mt = new MainTest();

		System.out.println("Lendo prestador"); // prestador dadosList =
		List<String> dadosList = mt.lerArquivo("dados_livro_prestador", 64);
		
		System.out.println("Lendo tomador"); // tomador dadosList =
		dadosList = mt.lerArquivo("dados_livro_tomador", 66);
		
		System.out.println("Processo concluido"); // prestador dadosList =

	}


	private List<String> lerArquivo(String arquivoIn, int qtdeCampos) {
		File file, fileWr;
		file = new File("c:/TEMP/lagoa/" + arquivoIn + ".txt");
		fileWr = new File("c:/TEMP/lagoa/txts_corrigidos/" + arquivoIn + "_new.txt");
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(fileWr);
			BufferedWriter bw = new BufferedWriter(fw);
			List<String> dadosList = new ArrayList<String>();
			try {
				br.readLine(); // cabeçalho
				while (br.ready()) {
					StringBuilder linhaDefinitiva = new StringBuilder();
					String[] arrayAux = { "", "" };

					while (arrayAux != null && arrayAux.length < qtdeCampos) {
						String linha = br.readLine();
						linha = preparaParaSplit(linha);
						linhaDefinitiva.append(linha);
						arrayAux = linhaDefinitiva.toString().split("#");
					}

					dadosList.add(linhaDefinitiva.toString());
					bw.write(linhaDefinitiva.toString() + "\n");

				}
				br.close();
				fr.close();
				bw.close();
				fw.close();
				return dadosList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private String preparaParaSplit(String linha) {
		linha = linha.replaceAll("\\|", "#");
		while (linha.contains("\\|")) {
			linha = linha.replaceAll("\\|", "#");
		}
		linha = linha.replaceAll("##", "# #");
		while (linha.contains("##")) {
			linha = linha.replaceAll("##", "# #");
		}
		String ultCarac = "";
		try {
			linha.substring(linha.trim().length() - 1);
			if (ultCarac.equals("#")) {
				linha = linha + " ";
			}
		} catch (Exception e) {

		}
		return linha;
	}

}
