package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.entidadesOrigem.DadosGuia;
import cmm.util.HibernateUtil;

public class DadosGuiaDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public DadosGuiaDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public DadosGuia save(DadosGuia dg) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(dg);
			session.getTransaction().commit();
			return dg;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	
	public DadosGuia findByCodigo(Long codigo) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from DadosGuia dg  " + " where dg.codigo = " + codigo);

		try {
			List<DadosGuia> dgs = query.list();

			if (dgs.size() > 0) {
				return dgs.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}

	public boolean exists(Long id) {
		Query query = sessionFactory.openSession().createQuery("from DadosGuia dg where dg.codigo = " + id);

		try {
			List<DadosGuia> dgs = query.list();

			if (dgs.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}

