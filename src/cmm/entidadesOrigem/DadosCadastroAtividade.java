package cmm.entidadesOrigem;

public class DadosCadastroAtividade {
	/*
	 * cnpj|codigo_cadastro|atividade
	 * |atividade_municipio|atividade_federal|aliquota
	 * |dedutivel|grupo_atividade|mes_inicio|ano_inicio
	 * |mes_fim|ano_fim|tributavel
	 */

	private String cnpj;
	private String codigoCadastro;
	private String atividade;
	private String atividadeMunicipio;
	private String atividadeFederal;
	private Double aliquota;
	private String dedutivel;
	private String grupoAtividade;
	private String mesInicio;
	private String anoInicio;
	private String mesFim;
	private String anoFim;
	private String tributavel;

	public DadosCadastroAtividade(String cnpj, String codigoCadastro, String atividade, String atividadeMunicipio, String atividadeFederal, Double aliquota,
			String dedutivel, String grupoAtividade, String mesInicio, String anoInicio, String mesFim, String anoFim, String tributavel) {

		this.cnpj = cnpj;
		this.codigoCadastro = codigoCadastro;
		this.atividade = atividade;
		this.atividadeMunicipio = atividadeMunicipio;
		this.atividadeFederal = atividadeFederal;
		this.aliquota = aliquota;
		this.dedutivel = dedutivel;
		this.grupoAtividade = grupoAtividade;
		this.mesInicio = mesInicio;
		this.anoInicio = anoInicio;
		this.mesFim = mesFim;
		this.anoFim = anoFim;
		this.tributavel = tributavel;

	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getCodigoCadastro() {
		return codigoCadastro;
	}

	public void setCodigoCadastro(String codigoCadastro) {
		this.codigoCadastro = codigoCadastro;
	}

	public String getAtividade() {
		return atividade;
	}

	public void setAtividade(String atividade) {
		this.atividade = atividade;
	}

	public String getAtividadeMunicipio() {
		return atividadeMunicipio;
	}

	public void setAtividadeMunicipio(String atividadeMunicipio) {
		this.atividadeMunicipio = atividadeMunicipio;
	}

	public String getAtividadeFederal() {
		return atividadeFederal;
	}

	public void setAtividadeFederal(String atividadeFederal) {
		this.atividadeFederal = atividadeFederal;
	}

	public Double getAliquota() {
		return aliquota;
	}

	public void setAliquota(Double aliquota) {
		this.aliquota = aliquota;
	}

	public String getDedutivel() {
		return dedutivel;
	}

	public void setDedutivel(String dedutivel) {
		this.dedutivel = dedutivel;
	}

	public String getGrupoAtividade() {
		return grupoAtividade;
	}

	public void setGrupoAtividade(String grupoAtividade) {
		this.grupoAtividade = grupoAtividade;
	}

	public String getMesInicio() {
		return mesInicio;
	}

	public void setMesInicio(String mesInicio) {
		this.mesInicio = mesInicio;
	}

	public String getAnoInicio() {
		return anoInicio;
	}

	public void setAnoInicio(String anoInicio) {
		this.anoInicio = anoInicio;
	}

	public String getMesFim() {
		return mesFim;
	}

	public void setMesFim(String mesFim) {
		this.mesFim = mesFim;
	}

	public String getAnoFim() {
		return anoFim;
	}

	public void setAnoFim(String anoFim) {
		this.anoFim = anoFim;
	}

	public String getTributavel() {
		return tributavel;
	}

	public void setTributavel(String tributavel) {
		this.tributavel = tributavel;
	}

}
