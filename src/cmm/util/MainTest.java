package cmm.util;

import java.text.SimpleDateFormat;

import cmm.dao.MunicipiosIbgeDao;
import cmm.dao.PessoaDao;
import cmm.dao.TomadoresDao;
import cmm.service.ExtractorService;
import cmm.model.Pessoa;
import cmm.model.Tomadores;

import java.util.*;

public class MainTest {

	private PessoaDao pessoaDao = new PessoaDao();
	private TomadoresDao tomadoresDao = new TomadoresDao();
	private ExtractorService extractorService = new ExtractorService();
	private Util util = new Util();
	private MunicipiosIbgeDao municipiosIbgeDao = new MunicipiosIbgeDao();

	public static void main(String args[]) {

		MainTest m = new MainTest();
		List<Tomadores> lista = m.tomadoresDao.findAll();
		m.incluiPessoaByTomadores(lista);

	}

	private void incluiPessoaByTomadores(List<Tomadores> lista) {
		// incluindo pessoa

		Long proximoPessoaIdGravado = null;
		Pessoa ultimaPessoa = null;
		try {
			ultimaPessoa = pessoaDao.ultimoPessoaIdGravado();
			proximoPessoaIdGravado = ultimaPessoa.getPessoaId();
			proximoPessoaIdGravado++;
		} catch (Exception e) {
			try {
				throw new Exception("Erro fatal: ultimo pessoa-id gravado não recuperado!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

		int lidos = 0;
		int gravados = 0;

		for (Tomadores t : lista) {
			lidos++;
			String cnpjCpf = t.getInscricaoTomador().trim();
			Pessoa pessoa = pessoaDao.findByCnpjCpf(cnpjCpf);
			try {
				if (pessoa == null || pessoa.getId() == null) {
					proximoPessoaIdGravado++;
					pessoa = new Pessoa();
					pessoa.setPessoaId(proximoPessoaIdGravado);
					pessoa.setCelular(t.getTelefone());
					pessoa.setEmail(t.getEmail() != null && t.getEmail().length() >= 80 ? t.getEmail().substring(0, 80)
							: t.getEmail());
					pessoa.setCnpjCpf(cnpjCpf);
					pessoa.setBairro(t.getBairro());
					pessoa.setEndereco(t.getEndereco());
					pessoa.setCep(t.getCep());
					pessoa.setComplemento(t.getComplemento());
					pessoa.setInscricaoMunicipal(t.getInscricaoMunicipal());
					pessoa.setMunicipio(t.getMunicipio());
					pessoa.setNome(t.getNome());
					pessoa.setNomeFantasia(t.getNomeFantasia());
					if (t.getNomeFantasia() == null || t.getNomeFantasia().isEmpty()) {
						pessoa.setNomeFantasia(pessoa.getNome());
					}
					pessoa.setNumero(t.getNumero());
					pessoa.setOptanteSimples(util.getOptantePeloSimplesNacional(t.getOptanteSimples()));
					if (t.getTelefone() != null && t.getTelefone().trim().length() > 11) {
						pessoa.setTelefone(t.getTelefone().trim().substring(0, 11));
						pessoa.setCelular(t.getTelefone().trim().substring(0, 11));
					} else {
						if (t.getTelefone() != null) {
							pessoa.setTelefone(t.getTelefone().trim());
							pessoa.setCelular(t.getTelefone().trim());
						}
					}
					pessoa.setTipoPessoa(util.getTipoPessoa(pessoa.getCnpjCpf().trim()));
					if (t.getInscricaoEstadual() != null && t.getInscricaoEstadual().length() >= 15) {
						pessoa.setInscricaoEstadual(t.getInscricaoEstadual().trim().substring(0, 14));
					} else {
						pessoa.setInscricaoEstadual(t.getInscricaoEstadual());
					}
					pessoa = extractorService.trataNumerosTelefones(pessoa);
					pessoa.setUf(municipiosIbgeDao.findUfByCodigoIbge(t.getMunicipioIbge()));
					try {
					pessoa.setMunicipioIbge(
							Long.valueOf(municipiosIbgeDao.getCodigoIbge(pessoa.getMunicipio(), pessoa.getUf())));
					} catch (Exception e) {
						e.printStackTrace();
					}
					pessoa = extractorService.anulaCamposVazios(pessoa);
					pessoaDao.save(pessoa);
					gravados++;

				}

			} catch (Exception e) {
				e.printStackTrace();

			}
			System.out.println("Regs lidos: " + lidos + "  Gravados:" + gravados);
		}
	}
	
	
	
	

}
