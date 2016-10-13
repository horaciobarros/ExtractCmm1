package cmm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "guias_numeracao")
public class GuiasNumeracaoCmm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;
	private String codigo;
	private String nossoNumero;
	private String numeroBaixa;
	private String pagto;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public String getNumeroBaixa() {
		return numeroBaixa;
	}
	public void setNumeroBaixa(String numeroBaixa) {
		this.numeroBaixa = numeroBaixa;
	}
	public String getPagto() {
		return pagto;
	}
	public void setPagto(String pagto) {
		this.pagto = pagto;
	}
	
}