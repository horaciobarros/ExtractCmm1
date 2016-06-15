package cmm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "guias_numeracao_cmm")
public class GuiasNumeracaoCmm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="numero_guia_origem")
	private String numeroGuiaOrigem;
	
	@Column(name="sequencial_cmm")
	private String sequencialCmm;
	
	public String getNumeroGuiaOrigem() {
		return numeroGuiaOrigem;
	}

	public void setNumeroGuiaOrigem(String numeroGuiaOrigem) {
		this.numeroGuiaOrigem = numeroGuiaOrigem;
	}

	public String getSequencialCmm() {
		return sequencialCmm;
	}

	public void setSequencialCmm(String sequencialCmm) {
		this.sequencialCmm = sequencialCmm;
	}

	public String getNumeroPagamento() {
		return numeroPagamento;
	}

	public void setNumeroPagamento(String numeroPagamento) {
		this.numeroPagamento = numeroPagamento;
	}

	@Column(name="numero_pagamento")
	private String numeroPagamento;
	
	
	
	
	

}
