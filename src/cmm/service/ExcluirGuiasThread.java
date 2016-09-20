package cmm.service;

import java.util.List;

import cmm.dao.GuiasDao;
import cmm.dao.GuiasNotasFiscaisDao;
import cmm.dao.PagamentosDao;
import cmm.model.Guias;
import cmm.model.GuiasNotasFiscais;


public class ExcluirGuiasThread implements Runnable{
	
	private Guias guias;
	GuiasNotasFiscaisDao guiasNotasFiscaisDao = new GuiasNotasFiscaisDao();
	PagamentosDao pagamentosDao = new PagamentosDao();
	GuiasDao guiasDao = new GuiasDao();
	
	public ExcluirGuiasThread(Guias guias) {
		this.guias = guias;
	}

	@Override
	public void run() {
		try {
			List<GuiasNotasFiscais> gnfValues = guiasNotasFiscaisDao.findPorNumeroGuia(guias.getNumeroGuia());
			if (gnfValues == null || gnfValues.size() == 0) {
				pagamentosDao.deleteByGuia(guias);
				guiasDao.delete(guias);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

}
