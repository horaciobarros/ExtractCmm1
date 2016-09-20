package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import cmm.model.Cnae;
import cmm.model.ListaServicos;
import cmm.util.HibernateUtil;
import cmm.util.Util;

public class ListaServicosDao {
	
	private Util util = new Util();
	
	StringBuilder hql;
	private SessionFactory sessionFactory;

	public ListaServicosDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public ListaServicos findByCodigo(String lc127) {
		
		if (util.isEmptyOrNull(lc127)) {
			return null;
		}
		
		Query query = sessionFactory.openSession().createQuery("from ListaServicos s where s.lc127 = :lc127").setParameter("lc127", lc127);
		

		try {
			List<ListaServicos> lista = query.list();

			if (lista.size() > 0) {
				return lista.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
		
}
