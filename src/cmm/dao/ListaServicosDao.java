package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.ListaServicos;
import cmm.util.HibernateUtil;

public class ListaServicosDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;

	public ListaServicosDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public ListaServicos findByCodigo(String lc127) {
		
		if (lc127 == null || lc127.trim().isEmpty()) {
			return null;
		}
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from ListaServicos s where s.lc127 like :lc127").setParameter("lc127", lc127);
		

		try {
			List<ListaServicos> lista = query.list();

			if (lista.size() > 0) {
				return lista.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}
		
}
