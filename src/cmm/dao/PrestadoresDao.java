package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Prestadores;
import cmm.util.HibernateUtil;

public class PrestadoresDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public PrestadoresDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
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

	public Prestadores save(Prestadores p) {
		
		if (p.getId() != null) {
			try {
				throw new Exception("Erro fatal: gravando entidade com id já definido.");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Session session = sessionFactory.openSession();
		try{
			session.beginTransaction();
			session.save(p);
			session.getTransaction().commit();
		}
		catch(Exception e){
			e.printStackTrace();
			throw e;
		}
		finally{
			session.close();
		}
		return p;
	}

	public Prestadores update(Prestadores p) {

		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(p);
		session.beginTransaction().commit();
		session.close();
		return p;
	}

	public List<Prestadores> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Prestadores p where hash is null").setFirstResult(0)
				.setMaxResults(1200);
		List<Prestadores> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public List<Prestadores> findAll() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Prestadores p ");
		List<Prestadores> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<Prestadores> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Prestadores set hash = '" + hash + "' where ");

		for (Prestadores c : listaAtualizados) {
			builder.append("id = " + c.getId() + " or ");
		}

		String sql = builder.toString();
		sql = sql.toString().substring(0, sql.length() - 4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

}
