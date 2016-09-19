package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import cmm.model.Cnae;
import cmm.util.HibernateUtil;

public class CnaeDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public CnaeDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Cnae findByCodigo(String codigo) {
		Query query = sessionFactory.openSession().createQuery("from Cnae c where c.cnae like :cnae").setParameter("cnae","%"+ codigo+"%");

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
