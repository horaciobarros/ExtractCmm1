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
		
		if (util.isEmptyOrNull(codigo)) {
			return null;
		}
		
		CnaeAtualizado c = null;
		int caracteres = codigo.length();
		while (caracteres>=4 && c == null){
			c = findByCodigo(codigo, caracteres);
			caracteres--;
		}
		return c;
	}

	private CnaeAtualizado findByCodigo(String codigo, int caracteres){
		if (codigo!=null && codigo.length()>=caracteres){
			codigo = codigo.substring(0, caracteres);
		}
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Cnae c where c.cnae like :cnae").setParameter("cnae", codigo+"%");
		

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
