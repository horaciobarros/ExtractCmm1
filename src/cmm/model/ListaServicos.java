package cmm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lista_servicos")
public class ListaServicos {
	
	@Id
	@Column(name = "id")
	private Long id;
	
	@Column(name = "cod_atv_tributos")
	private String codAtvTributos;
	
	@Column(name = "descricao_atv_tributos")
	private String descricaoAtvTributos;
	
	@Column(name = "lc127")
	private String lc127;
	
	@Column(name = "descricao_lc127")
	private String descricaoLc127;
	
	@Column(name = "lc116")
	private String lc116;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodAtvTributos() {
		return codAtvTributos;
	}

	public void setCodAtvTributos(String codAtvTributos) {
		this.codAtvTributos = codAtvTributos;
	}

	public String getDescricaoAtvTributos() {
		return descricaoAtvTributos;
	}

	public void setDescricaoAtvTributos(String descricaoAtvTributos) {
		this.descricaoAtvTributos = descricaoAtvTributos;
	}

	public String getLc127() {
		return lc127;
	}

	public void setLc127(String lc127) {
		this.lc127 = lc127;
	}

	public String getDescricaoLc127() {
		return descricaoLc127;
	}

	public void setDescricaoLc127(String descricaoLc127) {
		this.descricaoLc127 = descricaoLc127;
	}

	public String getLc116() {
		return lc116;
	}

	public void setLc116(String lc116) {
		this.lc116 = lc116;
	}

	
	
}
