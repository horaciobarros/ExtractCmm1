package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.CnaeAtualizado;
import cmm.util.HibernateUtil;
import cmm.util.Util;

public class CnaeAtualizadoDao {
	
	private Util util = new Util();

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public CnaeAtualizadoDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public CnaeAtualizado findByCodigo(String codigo) {
		
		String codigo4 = codigo.substring(0,4);
		String codigo2 = codigo.substring(4);
		
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Cnae c where c.cnae like :cnae4 and c.cnae like :cnae2 ")
				.setParameter("cnae4", codigo4 +"%").setParameter("cnae2", "%" + codigo2);
		
		try {
			List<CnaeAtualizado> cnaes = query.list();

			if (cnaes.size() > 0) {
				return cnaes.get(0);
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
