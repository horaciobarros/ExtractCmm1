package cmm.entidadesOrigem;

public class PlanoConta {
	/*
	 * id_codigo|cd_conta|cnpj|inscricao_municipal|titulo|codigo_guia|
	 * codigo_cosif|codigo_atividade|tributavel|ano_competencia
	 * |mes_competencia|vl_receita|vl_aliquota|vl_imposto|data_inclusao
	 */

	private Long idCodigo;
	private String cdConta;
	private String cnpj;
	private String inscricaoMunicipal;
	private String titulo;
	private String codigoGuia;
	private String codigoCosif;
	private String codigoAtividade;
	private String tributavel;
	private String anoCompetencia;
	private String mesCompetencia;
	private Double vlReceita;
	private Double vlAliquota;
	private Double vlImposto;
	private String dataInclusao;

	public PlanoConta(String idCodigo, String cdConta, String cnpj, String inscricaoMunicipal, String titulo, String codigoGuia, String codigoCosif,
			String codigoAtividade, String tributavel, String anoCompetencia, String mesCompetencia, String vlReceita, String vlAliquota, String vlImposto,
			String dataInclusao) {
		this.idCodigo = Long.valueOf(idCodigo);
		this.cdConta = cdConta;
		this.cnpj = cnpj;
		this.inscricaoMunicipal = inscricaoMunicipal;
		this.titulo = titulo;
		this.codigoGuia = codigoGuia;
		this.codigoCosif = codigoCosif;
		this.codigoAtividade = codigoAtividade;
		this.tributavel = tributavel;
		this.anoCompetencia = anoCompetencia;
		this.mesCompetencia = mesCompetencia;
		this.vlReceita = Double.valueOf(vlReceita);
		this.vlAliquota = Double.valueOf(vlAliquota);
		this.vlImposto = Double.valueOf(vlImposto);
		this.dataInclusao = dataInclusao;
	}

	public Long getIdCodigo() {
		return this.idCodigo;
	}

	public void setIdCodigo(Long idCodigo) {
		this.idCodigo = idCodigo;
	}

	public String getCdConta() {
		return cdConta;
	}

	public void setCdConta(String cdConta) {
		this.cdConta = cdConta;
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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCodigoGuia() {
		return codigoGuia;
	}

	public void setCodigoGuia(String codigoGuia) {
		this.codigoGuia = codigoGuia;
	}

	public String getCodigoCosif() {
		return codigoCosif;
	}

	public void setCodigoCosif(String codigoCosif) {
		this.codigoCosif = codigoCosif;
	}

	public String getCodigoAtividade() {
		return codigoAtividade;
	}

	public void setCodigoAtividade(String codigoAtividade) {
		this.codigoAtividade = codigoAtividade;
	}

	public String getTributavel() {
		return tributavel;
	}

	public void setTributavel(String tributavel) {
		this.tributavel = tributavel;
	}

	public String getAnoCompetencia() {
		return anoCompetencia;
	}

	public void setAnoCompetencia(String anoCompetencia) {
		this.anoCompetencia = anoCompetencia;
	}

	public String getMesCompetencia() {
		return mesCompetencia;
	}

	public void setMesCompetencia(String mesCompetencia) {
		this.mesCompetencia = mesCompetencia;
	}

	public Double getVlReceita() {
		return vlReceita;
	}

	public void setVlReceita(Double vlReceita) {
		this.vlReceita = vlReceita;
	}

	public Double getVlAliquota() {
		return vlAliquota;
	}

	public void setVlAliquota(Double vlAliquota) {
		this.vlAliquota = vlAliquota;
	}

	public Double getVlImposto() {
		return vlImposto;
	}

	public void setVlImposto(Double vlImposto) {
		this.vlImposto = vlImposto;
	}

	public String getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCodigo == null) ? 0 : idCodigo.hashCode());
		result = prime * result + ((anoCompetencia == null) ? 0 : anoCompetencia.hashCode());
		result = prime * result + ((cdConta == null) ? 0 : cdConta.hashCode());
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((codigoAtividade == null) ? 0 : codigoAtividade.hashCode());
		result = prime * result + ((codigoCosif == null) ? 0 : codigoCosif.hashCode());
		result = prime * result + ((codigoGuia == null) ? 0 : codigoGuia.hashCode());
		result = prime * result + ((dataInclusao == null) ? 0 : dataInclusao.hashCode());
		result = prime * result + ((inscricaoMunicipal == null) ? 0 : inscricaoMunicipal.hashCode());
		result = prime * result + ((mesCompetencia == null) ? 0 : mesCompetencia.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
		result = prime * result + ((tributavel == null) ? 0 : tributavel.hashCode());
		result = prime * result + ((vlImposto == null) ? 0 : vlImposto.hashCode());
		result = prime * result + ((vlReceita == null) ? 0 : vlReceita.hashCode());
		result = prime * result + ((vlAliquota == null) ? 0 : vlAliquota.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlanoConta other = (PlanoConta) obj;
		if (idCodigo == null) {
			if (other.idCodigo != null)
				return false;
		} else if (!idCodigo.equals(other.idCodigo))
			return false;
		if (anoCompetencia == null) {
			if (other.anoCompetencia != null)
				return false;
		} else if (!anoCompetencia.equals(other.anoCompetencia))
			return false;
		if (cdConta == null) {
			if (other.cdConta != null)
				return false;
		} else if (!cdConta.equals(other.cdConta))
			return false;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
		if (codigoAtividade == null) {
			if (other.codigoAtividade != null)
				return false;
		} else if (!codigoAtividade.equals(other.codigoAtividade))
			return false;
		if (codigoCosif == null) {
			if (other.codigoCosif != null)
				return false;
		} else if (!codigoCosif.equals(other.codigoCosif))
			return false;
		if (codigoGuia == null) {
			if (other.codigoGuia != null)
				return false;
		} else if (!codigoGuia.equals(other.codigoGuia))
			return false;
		if (dataInclusao == null) {
			if (other.dataInclusao != null)
				return false;
		} else if (!dataInclusao.equals(other.dataInclusao))
			return false;
		if (inscricaoMunicipal == null) {
			if (other.inscricaoMunicipal != null)
				return false;
		} else if (!inscricaoMunicipal.equals(other.inscricaoMunicipal))
			return false;
		if (mesCompetencia == null) {
			if (other.mesCompetencia != null)
				return false;
		} else if (!mesCompetencia.equals(other.mesCompetencia))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		if (tributavel == null) {
			if (other.tributavel != null)
				return false;
		} else if (!tributavel.equals(other.tributavel))
			return false;
		if (vlImposto == null) {
			if (other.vlImposto != null)
				return false;
		} else if (!vlImposto.equals(other.vlImposto))
			return false;
		if (vlReceita == null) {
			if (other.vlReceita != null)
				return false;
		} else if (!vlReceita.equals(other.vlReceita))
			return false;
		if (vlAliquota == null) {
			if (other.vlAliquota != null)
				return false;
		} else if (!vlAliquota.equals(other.vlAliquota))
			return false;
		return true;
	}

}
