package cmm.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "guias_notas_fiscais")
public class GuiasNotasFiscais implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "guias_id")
	private Guias guias;

	@Column(name = "inscricao_prestador")
	private String inscricaoPrestador;

	@Column(name = "numero_guia")
	private Long numeroGuia;

	@Column(name = "numero_nota")
	private Long numeroNota;

	@Column(name = "dh_envio")
	private Date dhEnvio;

	@Column(name = "hash")
	private String hash;
	
	@Column(name = "situacao_tributaria")
	private String situacaoTributaria;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Guias getGuias() {
		return guias;
	}

	public void setGuias(Guias guias) {
		this.guias = guias;
	}

	public String getInscricaoPrestador() {
		return inscricaoPrestador;
	}

	public void setInscricaoPrestador(String inscricaoPrestador) {
		this.inscricaoPrestador = inscricaoPrestador;
	}

	public Long getNumeroGuia() {
		return numeroGuia;
	}

	public void setNumeroGuia(Long numeroGuia) {
		this.numeroGuia = numeroGuia;
	}

	public Long getNumeroNota() {
		return numeroNota;
	}

	public void setNumeroNota(Long numeroNota) {
		this.numeroNota = numeroNota;
	}

	public Date getDhEnvio() {
		return dhEnvio;
	}

	public void setDhEnvio(Date dhEnvio) {
		this.dhEnvio = dhEnvio;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getSituacaoTributaria() {
		return situacaoTributaria;
	}

	public void setSituacaoTributaria(String situacaoTributaria) {
		this.situacaoTributaria = situacaoTributaria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dhEnvio == null) ? 0 : dhEnvio.hashCode());
		result = prime * result + ((guias == null) ? 0 : guias.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inscricaoPrestador == null) ? 0 : inscricaoPrestador.hashCode());
		result = prime * result + ((numeroGuia == null) ? 0 : numeroGuia.hashCode());
		result = prime * result + ((numeroNota == null) ? 0 : numeroNota.hashCode());
		result = prime * result + ((situacaoTributaria == null) ? 0 : situacaoTributaria.hashCode());
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
		GuiasNotasFiscais other = (GuiasNotasFiscais) obj;
		if (this.getNumeroNota() == other.getNumeroNota() && this.getInscricaoPrestador().equals(other.getInscricaoPrestador())){
			return true;
		}
		return false;
	}
	
}