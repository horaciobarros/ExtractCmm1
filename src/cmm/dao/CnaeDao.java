package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import cmm.model.Cnae;
import cmm.util.HibernateUtil;
import cmm.util.Util;

public class CnaeDao {
	
	private Util util = new Util();

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public CnaeDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Cnae findByCodigo(String codigo) {
		
		if (util.isEmptyOrNull(codigo)) {
			return null;
		}
		
		Cnae c = null;
		int caracteres = codigo.length();
		while (caracteres>=4 && c == null){
			c = findByCodigo(codigo, caracteres);
			caracteres--;
		}
		return c;
	}

	private Cnae findByCodigo(String codigo, int caracteres){
		if (codigo!=null && codigo.length()>=caracteres){
			codigo = codigo.substring(0, caracteres);
		}
		Query query = sessionFactory.openSession().createQuery("from Cnae c where c.cnae like :cnae").setParameter("cnae", codigo+"%");
		

		try {
			List<Cnae> cnaes = query.list();

			if (cnaes.size() > 0) {
				return cnaes.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
