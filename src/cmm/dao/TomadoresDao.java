package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.Tomadores;
import cmm.util.HibernateUtil;

public class TomadoresDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public TomadoresDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Tomadores findByInscricao(String inscricao) {
		Query query = sessionFactory.openSession()
				.createQuery("from Tomadores t  " + " where t.inscricaoTomador like '%" + inscricao.trim() + "%'");

		try {
			List<Tomadores> tomadores = query.list();

			if (tomadores.size() > 0) {
				return tomadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(Tomadores t) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.save(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.beginTransaction().commit();
		session.close();
	}
	
	public void update(Tomadores t) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.update(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.beginTransaction().commit();
		session.close();
	}


	public Tomadores findByInscricaoMunicipal(String inscMunicipal) {
		Query query = sessionFactory.openSession()
				.createQuery("from Tomadores t  " + " where t.inscricaoMunicipal like '%" + inscMunicipal.trim() + "%'");

		try {
			List<Tomadores> tomadores = query.list();

			if (tomadores.size() > 0) {
				return tomadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
