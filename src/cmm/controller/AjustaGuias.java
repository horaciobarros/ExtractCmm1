package cmm.controller;

import java.util.List;

import cmm.dao.GuiasDao;
import cmm.dao.GuiasNumeracaoCmmDao;
import cmm.dao.PagamentosDao;
import cmm.dao.GuiasNotasFiscaisDao;
import cmm.model.Guias;
import cmm.model.GuiasNotasFiscais;
import cmm.model.GuiasNumeracaoCmm;
import cmm.model.Pagamentos;
import cmm.util.FileLog;

public class AjustaGuias {
	
	private GuiasDao guiasDao = new GuiasDao();
	private GuiasNotasFiscaisDao gnfDao = new GuiasNotasFiscaisDao();
	private PagamentosDao pagamentosDao = new PagamentosDao();
	private GuiasNumeracaoCmmDao guiaNumeracaoDao = new GuiasNumeracaoCmmDao();
	private boolean salvar = false;
	
	public void init(){
		List<Guias> listaGuias = guiasDao.findAll();
		FileLog log = new FileLog("guias_numeracao");
		for (Guias g : listaGuias){
			try{
				GuiasNumeracaoCmm numeracao = guiaNumeracaoDao.findByNossoNumero(g.getNumeroGuiaOrigem());
				if (numeracao!=null){
					g.setNumeroGuiaGerada(g.getNumeroGuia());
					g.setNumeroGuia(Long.parseLong(numeracao.getNumeroBaixa()));
					
					Pagamentos pagamento = pagamentosDao.findPorIdGuia(g.getId());
					List<GuiasNotasFiscais> listaGnf = gnfDao.findPorNumeroGuia(g.getNumeroGuiaGerada());
					
					if (pagamento!=null){
						pagamento.setNumeroPagamentoOrigem(pagamento.getNumeroPagamento());
						pagamento.setNumeroGuia(g.getNumeroGuia());
						pagamento.setNumeroPagamento(Long.parseLong(numeracao.getPagto()));
						if (salvar){pagamentosDao.update(pagamento);}
					}
					if (listaGnf!=null){
						for (GuiasNotasFiscais gnf : listaGnf){
							gnf.setNumeroGuia(g.getNumeroGuia());
							if (salvar){gnfDao.update(gnf);}
						}
					}
					if (salvar){guiasDao.update(g);}
					String msg = "";
					if (pagamento!=null){
						msg = "id: "+g.getId()+" nossoNumeroOrigem: "+g.getNumeroGuiaOrigem()+" numero: "+g.getNumeroGuiaGerada()+ " novo número: "+g.getNumeroGuia()+" pagamento: "+pagamento.getNumeroPagamentoOrigem()+" novo pagamento: "+pagamento.getNumeroPagamento();
					} else{
						msg = "id: "+g.getId()+" nossoNumeroOrigem: "+g.getNumeroGuiaOrigem()+" numero: "+g.getNumeroGuiaGerada()+ " novo número: "+g.getNumeroGuia();
					}
					log.fillError("", msg);
					System.out.println(msg);
				}
			}
			catch(Exception e){
				e.printStackTrace();
				System.out.println("Guia " +g.getId());
				log.fillError(g.getId()+"", e.getMessage());
			}
		}
		log.close();
	}

	public static void main(String[] args) {
		new AjustaGuias().init();
	}
}
