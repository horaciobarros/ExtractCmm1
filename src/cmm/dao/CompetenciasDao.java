package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Competencias;
import cmm.util.HibernateUtil;

public class CompetenciasDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public CompetenciasDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Competencias findByDescricao(String descricao) {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory.openSession()
				.createQuery("from Competencias cp  " + " where cp.descricao = '" + descricao + "'");
		List<Competencias> competencias = query.list();
		tx.commit();

		return competencias.get(0);
	}

	public void save(Competencias cp) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(cp);
		session.beginTransaction().commit();
		session.close();
	}

}
