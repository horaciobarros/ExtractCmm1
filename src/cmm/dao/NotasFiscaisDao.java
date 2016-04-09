package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.NotasFiscais;
import cmm.util.HibernateUtil;

public class NotasFiscaisDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public NotasFiscaisDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void save(NotasFiscais nf) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nf);
		session.beginTransaction().commit();
		session.close();
	}

}
