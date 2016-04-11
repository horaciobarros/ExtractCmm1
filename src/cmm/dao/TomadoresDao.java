package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Tomadores;
import cmm.util.HibernateUtil;

public class TomadoresDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public TomadoresDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Tomadores findByInscricao(String inscricao) {
		Query query = sessionFactory.openSession()
				.createQuery("from Tomadores t  " + " where t.inscricaoTomador like '%" + inscricao.trim() + "%'");

		try {
			List<Tomadores> tomadores = query.list();

			if (tomadores.size() > 0) {
				return tomadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(Tomadores t) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.save(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.beginTransaction().commit();
		session.close();
	}
	
	public void update(Tomadores t) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		try {
			session.update(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.beginTransaction().commit();
		session.close();
	}


	public Tomadores findByInscricaoMunicipal(String inscMunicipal) {
		Query query = sessionFactory.openSession()
				.createQuery("from Tomadores t  " + " where t.inscricaoMunicipal like '%" + inscMunicipal.trim() + "%'");

		try {
			List<Tomadores> tomadores = query.list();

			if (tomadores.size() > 0) {
				return tomadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Tomadores> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from Tomadores c where hash is null");
		List<Tomadores> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<Tomadores> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Tomadores set hash = '"+hash+"' where ");
		
		for (Tomadores c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql).setFirstResult(0).setMaxResults(1000);
		query.executeUpdate();
		tx.commit();
	}
}
