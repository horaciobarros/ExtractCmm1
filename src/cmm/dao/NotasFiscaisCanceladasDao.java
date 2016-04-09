package cmm.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.NotasFiscaisCanceladas;
import cmm.util.HibernateUtil;

public class NotasFiscaisCanceladasDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;
	
	public NotasFiscaisCanceladasDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void save(NotasFiscaisCanceladas nfc) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfc);
		session.beginTransaction().commit();
		session.close();
	}

}
