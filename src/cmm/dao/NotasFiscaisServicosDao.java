package cmm.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.NotasFiscaisServicos;
import cmm.util.HibernateUtil;

public class NotasFiscaisServicosDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;
	
	public NotasFiscaisServicosDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void save(NotasFiscaisServicos nfs) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfs);
		session.beginTransaction().commit();
		session.close();
	}

}
