package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.Guias;
import cmm.util.HibernateUtil;

public class GuiasDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public GuiasDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void save(Guias g) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(g);
		session.beginTransaction().commit();
		session.close();
	}

}
