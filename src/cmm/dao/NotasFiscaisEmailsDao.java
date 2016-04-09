package cmm.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.NotasFiscaisEmails;
import cmm.util.HibernateUtil;

public class NotasFiscaisEmailsDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;
	
	public NotasFiscaisEmailsDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void save(NotasFiscaisEmails nfe) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfe);
		session.beginTransaction().commit();
		session.close();
	}

}
