package cmm.entidadesOrigem;

public class DadosGuia {
	/*
	 * codigo|nosso_numero|parcela|cnpj|insc_municipal|mes|ano|imposto|
	 * correcao_monetaria|juros|multa|valor_total
	 * |data_vencimento|data_boleto|status|tipo_guia|data_via1|via1|pago|
	 * data_pagamento|valor_pago|razao_social
	 * |email_contribuinte|cnpj_contador|data_cancelamento|motivo_cancelamento|
	 * ano_processo_cancelamento
	 * |data_estorno|data_divida_ativa|valor_estimado|valor_estimativa|
	 * nome_arquivo|data_inclusao|data_alteracao
	 * |baixa_tipo|baixa_vl_acrescimos|baixa_arquivo_nome|codigo_banco|
	 * codigo_convenio|data_baixa|motivo_baixa|data_credito
	 */
	private String codigo;
	private String nossoNumero;
	private String parcela;
	private String cnpj;
	private String inscMunicipal;
	private String mes;
	private String ano;
	private Double imposto;
	private Double correcaoMonetaria;
	private Double juros;
	private Double multa;
	private Double valorTotal;
	private String dataVencimento;
	private String dataBoleto;
	private String status;
	private String tipoGuia;
	private String dataVia1;
	private String via1;
	private String pago;
	private String dataPagamento;
	private Double valorPago;
	private String razaoSocial;
	private String emailContribuinte;
	private String cnpjContador;
	private String dataCancelamento;
	private String motivoCancelamento;
	private String anoProcessoCancelamento;
	private String dataEstorno;
	private String dataDividaAtiva;
	private Double valorEstimado;
	private Double valorEstimativa;
	private String nomeArquivo;
	private String dataInclusao;
	private String dataAlteracao;
	private String baixaTipo;
	private String baixaVlAcrescimos;
	private String baixaArquivoNome;
	private String codigoBanco;
	private String codigoConvenio;
	private String dataBaixa;
	private String motivoBaixa;
	private String dataCredito;

	public DadosGuia(String codigo, String nossoNumero, String parcela, String cnpj, String inscMunicipal, String mes,
			String ano, Double imposto, Double correcaoMonetaria, Double juros, Double multa, Double valorTotal,
			String dataVencimento, String dataBoleto, String status, String tipoGuia, String dataVia1, String via1,
			String pago, String dataPagamento, String valorPago, String razaoSocial, String emailContribuinte,
			String cnpjContador, String dataCancelamento, String motivoCancelamento, String anoProcessoCancelamento,
			String dataEstorno, String dataDividaAtiva, Double valorEstimado, Double valorEstimativa,
			String nomeArquivo, String dataInclusao, String dataAlteracao, String baixaTipo, String baixaVlAcrescimos,
			String baixaArquivoNome, String codigoBanco, String codigoConvenio, String dataBaixa, String motivoBaixa,
			String dataCredito) {
		this.codigo = codigo;
		this.nossoNumero = nossoNumero;
		this.parcela = parcela;
		this.cnpj = cnpj;
		this.inscMunicipal = inscMunicipal;
		this.mes = mes;
		this.ano = ano;
		this.imposto = imposto;
		this.correcaoMonetaria = correcaoMonetaria;
		this.juros = juros;
		this.multa = multa;
		this.valorTotal = valorTotal;
		this.dataVencimento = dataVencimento;
		this.dataBoleto = dataBoleto;
		this.status = status;
		this.tipoGuia = tipoGuia;
		this.dataVia1 = dataVia1;
		this.via1 = via1;
		try {
			this.pago = pago;
		} catch (NumberFormatException e) {

		}
		this.dataPagamento = dataPagamento;
		try {
			this.valorPago = Double.valueOf(valorPago);
		} catch (NumberFormatException e) {

		}
		this.razaoSocial = razaoSocial;
		this.emailContribuinte = emailContribuinte;
		this.cnpjContador = cnpjContador;
		this.dataCancelamento = dataCancelamento;
		this.motivoCancelamento = motivoCancelamento;
		this.anoProcessoCancelamento = anoProcessoCancelamento;
		this.dataEstorno = dataEstorno;
		this.dataDividaAtiva = dataDividaAtiva;
		try {
			this.valorEstimado = valorEstimado;
		} catch (NumberFormatException e) {

		}

		try {
			this.valorEstimativa = valorEstimativa;
		} catch (NumberFormatException e) {

		}
		this.nomeArquivo = nomeArquivo;
		this.dataInclusao = dataInclusao;
		this.dataAlteracao = dataAlteracao;
		this.baixaTipo = baixaTipo;
		this.baixaVlAcrescimos = baixaVlAcrescimos;
		this.baixaArquivoNome = baixaArquivoNome;
		this.codigoBanco = codigoBanco;
		this.codigoConvenio = codigoConvenio;
		this.dataBaixa = dataBaixa;
		this.motivoBaixa = motivoBaixa;
		this.dataCredito = dataCredito;
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

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscMunicipal() {
		return inscMunicipal;
	}

	public void setInscMunicipal(String inscMunicipal) {
		this.inscMunicipal = inscMunicipal;
	}

	public String getMes() {
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Double getImposto() {
		return imposto;
	}

	public void setImposto(Double imposto) {
		this.imposto = imposto;
	}

	public Double getCorrecaoMonetaria() {
		return correcaoMonetaria;
	}

	public void setCorrecaoMonetaria(Double correcaoMonetaria) {
		this.correcaoMonetaria = correcaoMonetaria;
	}

	public Double getJuros() {
		return juros;
	}

	public void setJuros(Double juros) {
		this.juros = juros;
	}

	public Double getMulta() {
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(String dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getDataBoleto() {
		return dataBoleto;
	}

	public void setDataBoleto(String dataBoleto) {
		this.dataBoleto = dataBoleto;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTipoGuia() {
		return tipoGuia;
	}

	public void setTipoGuia(String tipoGuia) {
		this.tipoGuia = tipoGuia;
	}

	public String getDataVia1() {
		return dataVia1;
	}

	public void setDataVia1(String dataVia1) {
		this.dataVia1 = dataVia1;
	}

	public String getVia1() {
		return via1;
	}

	public void setVia1(String via1) {
		this.via1 = via1;
	}

	public String getPago() {
		return pago;
	}

	public void setPago(String pago) {
		this.pago = pago;
	}

	public String getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(String dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getValorPago() {
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getEmailContribuinte() {
		return emailContribuinte;
	}

	public void setEmailContribuinte(String emailContribuinte) {
		this.emailContribuinte = emailContribuinte;
	}

	public String getCnpjContador() {
		return cnpjContador;
	}

	public void setCnpjContador(String cnpjContador) {
		this.cnpjContador = cnpjContador;
	}

	public String getDataCancelamento() {
		return dataCancelamento;
	}

	public void setDataCancelamento(String dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public String getMotivoCancelamento() {
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public String getAnoProcessoCancelamento() {
		return anoProcessoCancelamento;
	}

	public void setAnoProcessoCancelamento(String anoProcessoCancelamento) {
		this.anoProcessoCancelamento = anoProcessoCancelamento;
	}

	public String getDataEstorno() {
		return dataEstorno;
	}

	public void setDataEstorno(String dataEstorno) {
		this.dataEstorno = dataEstorno;
	}

	public String getDataDividaAtiva() {
		return dataDividaAtiva;
	}

	public void setDataDividaAtiva(String dataDividaAtiva) {
		this.dataDividaAtiva = dataDividaAtiva;
	}

	public Double getValorEstimado() {
		return valorEstimado;
	}

	public void setValorEstimado(Double valorEstimado) {
		this.valorEstimado = valorEstimado;
	}

	public Double getValorEstimativa() {
		return valorEstimativa;
	}

	public void setValorEstimativa(Double valorEstimativa) {
		this.valorEstimativa = valorEstimativa;
	}

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	public String getDataInclusao() {
		return dataInclusao;
	}

	public void setDataInclusao(String dataInclusao) {
		this.dataInclusao = dataInclusao;
	}

	public String getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(String dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getBaixaTipo() {
		return baixaTipo;
	}

	public void setBaixaTipo(String baixaTipo) {
		this.baixaTipo = baixaTipo;
	}

	public String getBaixaVlAcrescimos() {
		return baixaVlAcrescimos;
	}

	public void setBaixaVlAcrescimos(String baixaVlAcrescimos) {
		this.baixaVlAcrescimos = baixaVlAcrescimos;
	}

	public String getBaixaArquivoNome() {
		return baixaArquivoNome;
	}

	public void setBaixaArquivoNome(String baixaArquivoNome) {
		this.baixaArquivoNome = baixaArquivoNome;
	}

	public String getCodigoBanco() {
		return codigoBanco;
	}

	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}

	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public String getDataBaixa() {
		return dataBaixa;
	}

	public void setDataBaixa(String dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public String getMotivoBaixa() {
		return motivoBaixa;
	}

	public void setMotivoBaixa(String motivoBaixa) {
		this.motivoBaixa = motivoBaixa;
	}

	public String getDataCredito() {
		return dataCredito;
	}

	public void setDataCredito(String dataCredito) {
		this.dataCredito = dataCredito;
	}

}
