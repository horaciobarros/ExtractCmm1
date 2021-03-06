package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Logradouros;
import cmm.util.HibernateUtil;

public class LogradourosDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public LogradourosDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Logradouros findByDescricao(String descricao) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Logradouros cp  " + " where cp.descricao like '%" + descricao.trim() + "%'");

		try {
			List<Logradouros> entidade = query.list();

			if (entidade.size() > 0) {
				return entidade.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}

	public void save(Logradouros entidade) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(entidade);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<Logradouros> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Logradouros c where hash is null").setFirstResult(0).setMaxResults(500);
		List<Logradouros> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<Logradouros> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Logradouros set hash = '" + hash + "' where ");

		for (Logradouros c : listaAtualizados) {
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
