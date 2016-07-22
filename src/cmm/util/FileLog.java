package cmm.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileLog {
	
	private String fileName = null;
	private File file = null;
	private FileWriter fw = null;
	private BufferedWriter bw = null;
	
	public FileLog(String fileName) {
		this.fileName = fileName;
		ativaLog();
		
	}

	public String getFileName() {
		return fileName;
	}

	public File getFile() {
		return file;
	}

	public FileWriter getFw() {
		return fw;
	}

	public BufferedWriter getBw() {
		return bw;
	}
	
	private void ativaLog() {
		file = new File("c:/TEMP/lagoa/zzz_" + fileName + "_err.txt");
		try {
			fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write("-- Arquivo " + fileName + " - inicio \n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		
		try {
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void fillError(String linha, Exception e) {
		String linhaAux = linha.replaceAll("#", "|");
		try {
			bw.write("erro --> " + " conteudo da linha:" + linhaAux + "\n" + e+"\n");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public void fillError(String linha, String msg) {
		String linhaAux = linha.replaceAll("#", "|");
		try {
			bw.write("erro --> " + " msg:" + linhaAux + "\n");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	
	

}
