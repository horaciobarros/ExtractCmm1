package cmm.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tablog")
public class TabLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	
	private String entidade;
	private String linha;
	private String erro;
	
	@Column(name="data_hora")
	private Date dataHora;

	public TabLog(String linha, String entidade, Exception e) {
		
		if (linha == null) {
			linha = "linha não informada";
		}
		this.linha = linha;
		this.entidade = entidade;
		this.erro = e.getMessage();
		if (erro == null) {
			this.erro = "erro desconhecido";
		}
		this.dataHora = new Date();
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getLinha() {
		return linha;
	}

	public void setLinha(String linha) {
		this.linha = linha;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
	}


}
