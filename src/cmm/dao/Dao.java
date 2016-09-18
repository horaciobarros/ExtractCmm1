package cmm.dao;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.util.HibernateUtil;

public class Dao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public Dao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void excluiDados(String nomeEntidade) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createQuery("delete from " + nomeEntidade + " ");
		query.executeUpdate();
		session.beginTransaction().commit();
		session.close();
	}

	public void limpaHash(String nomeEntidade) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query query = session.createQuery("update " + nomeEntidade + " set hash = null");
		query.executeUpdate();
		session.getTransaction().commit();
		session.close();
	}

	public String getInfoBanco() {
		try {
			return sessionFactory.openSession().connection().getMetaData().getURL();
		} catch (HibernateException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		;
		return "";
	}

	public Object save(Session session, Object obj) {
		try {
			session.beginTransaction();
			session.save(obj);
			session.getTransaction().commit();
		} catch (Exception e) {
			session.getTransaction().rollback();
			e.printStackTrace();
			throw e;
		}

		return obj;
	}
}