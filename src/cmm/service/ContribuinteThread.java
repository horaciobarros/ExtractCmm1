package cmm.service;

import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.PessoaDao;
import cmm.dao.PrestadoresDao;
import cmm.dao.PrestadoresOptanteSimplesDao;
import cmm.entidadesOrigem.DadosCadastro;
import cmm.model.Pessoa;
import cmm.model.Prestadores;
import cmm.model.PrestadoresOptanteSimples;
import cmm.util.FileLog;
import cmm.util.Util;

public class ContribuinteThread implements Runnable {

	private PessoaDao pessoaDao = new PessoaDao();
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();
	private PrestadoresDao prestadoresDao = new PrestadoresDao();
	private PrestadoresOptanteSimplesDao prestadoresOptanteSimplesDao = new PrestadoresOptanteSimplesDao();
	private String linha;
	private Util util;
	private FileLog log;

	public ContribuinteThread(String linha, Util util, FileLog log) {
		this.linha = linha;
		this.util = util;
		this.log = log;
	}

	@Override
	public void run() {
		try {
			// linha = preparaParaSplit(linha);
			String[] arrayLinha = linha.split("@@\\|");
			DadosCadastro dc = new DadosCadastro(arrayLinha[0], arrayLinha[1], arrayLinha[2], arrayLinha[3], arrayLinha[4], arrayLinha[5], arrayLinha[6],
					arrayLinha[7], arrayLinha[8], arrayLinha[9], arrayLinha[10], arrayLinha[11], arrayLinha[12], arrayLinha[13], arrayLinha[14], arrayLinha[15],
					arrayLinha[16], arrayLinha[17], arrayLinha[18], arrayLinha[19], arrayLinha[20], arrayLinha[21], arrayLinha[22]);
			// incluindo pessoa
			String cnpjCpf = util.getCpfCnpj(dc.getCnpj());
			Pessoa pessoa = pessoaDao.findByCnpjCpf(cnpjCpf);
			try {
				if (pessoa == null || pessoa.getId() == null) {
					pessoa = new Pessoa();
					pessoa.setPessoaId(Long.valueOf(dc.getIdCodigo()));
					pessoa.setEmail(util.trataEmail(dc.getEmail()));
					pessoa.setCnpjCpf(cnpjCpf);
					pessoa.setBairro(dc.getEnderecoBairro());
					pessoa.setEndereco(dc.getEndereco());
					pessoa.setCep(dc.getEnderecoCep());
					pessoa.setComplemento(dc.getEnderecoComplemento());
					pessoa.setInscricaoMunicipal(dc.getInscricaoMunicipal());
					pessoa.setMunicipio(dc.getMunicipio());
					pessoa.setNome(dc.getRazaoSocial());
					pessoa.setNomeFantasia(dc.getNomeFantasia());
					if (dc.getNomeFantasia() == null || dc.getNomeFantasia().isEmpty()) {
						pessoa.setNomeFantasia(pessoa.getNome());
					}
					pessoa.setNumero(dc.getEnderecoNumero());
					pessoa.setOptanteSimples(util.getOptantePeloSimplesNacional(dc.getOptanteSimplesNacional()));

					pessoa.setTelefone(util.getLimpaTelefone(dc.getTelefone()));

					pessoa.setTipoPessoa(util.getTipoPessoa(cnpjCpf));
					if (dc.getInscricaoEstadual() != null && dc.getInscricaoEstadual().length() >= 15) {
						pessoa.setInscricaoEstadual(dc.getInscricaoEstadual().trim().substring(0, 14));
					} else {
						pessoa.setInscricaoEstadual(dc.getInscricaoEstadual());
					}
					pessoa = util.trataNumerosTelefones(pessoa);
					pessoa.setUf(dc.getEnderecoUf());
					pessoa.setMunicipioIbge(Long.valueOf(municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));

					if ("F".equals(pessoa.getTipoPessoa())) {
						pessoa.setSexo("M");
					}
					pessoa = util.anulaCamposVazios(pessoa);
					pessoa = pessoaDao.save(pessoa);
				} else {

					if (dc.getInscricaoEstadual() != null && dc.getInscricaoEstadual().length() >= 15) {
						pessoa.setInscricaoEstadual(dc.getInscricaoEstadual().trim().substring(0, 14));
					} else {
						pessoa.setInscricaoEstadual(dc.getInscricaoEstadual());
					}
					pessoa.setUf(dc.getEnderecoUf());
					if (pessoa.getMunicipio() == null) {
						pessoa.setMunicipio(dc.getMunicipio());
					}
					pessoa.setMunicipioIbge(Long.valueOf(municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));
					pessoa.setInscricaoMunicipal(dc.getInscricaoMunicipal());

					pessoa = util.trataNumerosTelefones(pessoa);
					pessoa = util.anulaCamposVazios(pessoa);
					pessoa = pessoaDao.update(pessoa);
				}

			} catch (Exception e) {
				e.printStackTrace();
				log.fillError(linha,"Contribuinte - Pessoa ", e);
			}

			// ajustando prestadores
			Prestadores p = prestadoresDao.findByInscricao(cnpjCpf);
			try {
				if (p == null || p.getId() == 0 || p.getId() == null) {
					try {
						p = new Prestadores();
						p.setAutorizado("N");
						dc.setTelefone(util.getLimpaTelefone(dc.getTelefone()));
						p.setEmail(util.trataEmail(dc.getEmail()));
						p.setEnquadramento("N");
						p.setInscricaoPrestador(cnpjCpf);
						p.setMotivo("Solicitar cadastro"); // Pedido do
															// Sandro de
															// enviar como
															// N�o
															// autorizadi
															// Necessita
															// deste campo
															// preenchido
						p = util.trataNumerosTelefones(p);
						p = util.anulaCamposVazios(p);
						p = prestadoresDao.save(p);
					} catch (Exception e) {
						log.fillError(linha,"Contribuinte - Prestador", e);
						e.printStackTrace();
					}
				}

				// prestadores optantes simples
				if ("S".equalsIgnoreCase(dc.getOptanteSimplesNacional().substring(0, 1))) {
					try {
						PrestadoresOptanteSimples pos = new PrestadoresOptanteSimples();
						String inicioAtividade = dc.getDtInicioAtividade();
						if (inicioAtividade == null || inicioAtividade.trim().isEmpty()) {
							inicioAtividade = dc.getDataInclusaoRegistro();

						}
						pos.setDataEfeito(util.getStringToDate(inicioAtividade));
						pos.setDataInicio(pos.getDataEfeito());
						pos.setInscricaoPrestador(cnpjCpf);
						pos.setDescricao(dc.getRegimeTributacao());
						pos.setMei("N"); // ver com cmm
						pos.setMotivo("Opção do Contribuinte");
						pos.setOptante("S");
						pos.setOrgao("M");
						pos.setPrestadores(p);
						prestadoresOptanteSimplesDao.save(pos);
					} catch (Exception e) {
						log.fillError(linha,"Contribuinte - Optante Simples ", e);
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				log.fillError(linha,"Contribuinte - Prestador ou optante simples ", e);
				e.printStackTrace();
			}

		} catch (Exception e) {
			log.fillError(linha, "ContribuinteThread ", e);
			e.printStackTrace();
		}
	}

}
