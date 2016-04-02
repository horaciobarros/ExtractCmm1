package cmm.entidadesOrigem;

public class DadosCadastroAcesso {
	
	private String cnpj;
	private String acesso;
	
	public DadosCadastroAcesso(String cnpj, String acesso) {
		this.cnpj = cnpj;
		this.acesso = acesso;
	}
	
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getAcesso() {
		return acesso;
	}
	public void setAcesso(String acesso) {
		this.acesso = acesso;
	}
	
	

}
