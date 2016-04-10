package cmm.dao;

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
	

	
	
}