package cmm.entidadesOrigem;

public class DadosContador {
	/*
	 * id_codigo|cnpj|inscricao_municipal|inscricao_estadual|razao_social|
	 * nome_fantasia|endereco|endereco_numero
	 * |endereco_complemento|endereco_bairro
	 * |municipio|endereco_uf|endereco_cep|telefone|fax
	 * |email|status|cd_crc|crc_uf|crc_dt_registro
	 */

	private Long idCodigo;
	private String cnpj;
	private String inscricaoMunicipal;
	private String inscricaoEstadual;
	private String razaoSocial;
	private String nomeFantasia;
	private String endereco;
	private String enderecoNumero;
	private String enderecoComplemento;
	private String enderecoBairro;
	private String municipio;
	private String enderecoUf;
	private String enderecoCep;
	private String telefone;
	private String fax;
	private String email;
	private String status;
	private String cdCrc;
	private String crcUf;
	private String crcDtRegistro;

	public DadosContador(Long idCodigo, String cnpj, String inscricaoMunicipal, String inscricaoEstadual, String razaoSocial, String nomeFantasia, String endereco,
			String enderecoNumero, String enderecoComplemento, String enderecoBairro, String municipio, String enderecoUf, String enderecoCep, String telefone,
			String fax, String email, String status, String cdCrc, String crcUf, String crcDtRegistro) {

		this.idCodigo = idCodigo;
		this.cnpj = cnpj;
		this.inscricaoMunicipal = inscricaoMunicipal;
		this.inscricaoEstadual = inscricaoEstadual;
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
		this.endereco = endereco;
		this.enderecoNumero = enderecoNumero;
		this.enderecoComplemento = enderecoComplemento;
		this.enderecoBairro = enderecoBairro;
		this.municipio = municipio;
		this.enderecoUf = enderecoUf;
		this.enderecoCep = enderecoCep;
		this.telefone = telefone;
		this.fax = fax;
		this.email = email;
		this.status = status;
		this.cdCrc = cdCrc;
		this.crcUf = crcUf;
		this.crcDtRegistro = crcDtRegistro;

	}

	public Long getIdCodigo() {
		return idCodigo;
	}

	public void setIdCodigo(Long idCodigo) {
		this.idCodigo = idCodigo;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoMunicipal() {
		return inscricaoMunicipal;
	}

	public void setInscricaoMunicipal(String inscricaoMunicipal) {
		this.inscricaoMunicipal = inscricaoMunicipal;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEnderecoNumero() {
		return enderecoNumero;
	}

	public void setEnderecoNumero(String enderecoNumero) {
		this.enderecoNumero = enderecoNumero;
	}

	public String getEnderecoComplemento() {
		return enderecoComplemento;
	}

	public void setEnderecoComplemento(String enderecoComplemento) {
		this.enderecoComplemento = enderecoComplemento;
	}

	public String getEnderecoBairro() {
		return enderecoBairro;
	}

	public void setEnderecoBairro(String enderecoBairro) {
		this.enderecoBairro = enderecoBairro;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public String getEnderecoUf() {
		return enderecoUf;
	}

	public void setEnderecoUf(String enderecoUf) {
		this.enderecoUf = enderecoUf;
	}

	public String getEnderecoCep() {
		return enderecoCep;
	}

	public void setEnderecoCep(String enderecoCep) {
		this.enderecoCep = enderecoCep;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCdCrc() {
		return cdCrc;
	}

	public void setCdCrc(String cdCrc) {
		this.cdCrc = cdCrc;
	}

	public String getCrcUf() {
		return crcUf;
	}

	public void setCrcUf(String crcUf) {
		this.crcUf = crcUf;
	}

	public String getCrcDtRegistro() {
		return crcDtRegistro;
	}

	public void setCrcDtRegistro(String crcDtRegistro) {
		this.crcDtRegistro = crcDtRegistro;
	}

}
