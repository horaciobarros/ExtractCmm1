package cmm.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.model.TabLog;
import cmm.util.HibernateUtil;

public class TabLogDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;

	public TabLogDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void save(TabLog t) {
		Session session = sessionFactory.openSession();
		try{
			session.beginTransaction();
			session.save(t);
			session.getTransaction().commit();
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		finally{
			session.close();
		}
	}
}
