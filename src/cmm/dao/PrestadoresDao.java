package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import cmm.model.Prestadores;
import cmm.util.HibernateUtil;

public class PrestadoresDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public PrestadoresDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Prestadores findByInscricao(String inscricao) {
		Query query = sessionFactory.openSession()
				.createQuery("from Prestadores p  " + " where p.inscricaoPrestador like '%" + inscricao.trim() + "%'");

		try {
			List<Prestadores> prestadores = query.list();

			if (prestadores.size() > 0) {
				return prestadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(Prestadores p) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(p);
		session.beginTransaction().commit();
		session.close();
	}

	public Prestadores findByInscricaoMunicipal(String inscMunicipal) {
		// TODO Auto-generated method stub
		return null;
	}

}
