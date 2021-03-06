package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Bairros;
import cmm.util.HibernateUtil;

public class BairrosDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public BairrosDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Bairros findByDescricao(String descricao) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Bairros cp  " + " where cp.descricao like '%" + descricao.trim() + "%'");

		try {
			List<Bairros> entidade = query.list();

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

	public void save(Bairros entidade) {
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

	public List<Bairros> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Bairros c where hash is null").setFirstResult(0).setMaxResults(500);
		List<Bairros> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<Bairros> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Bairros set hash = '" + hash + "' where ");

		for (Bairros c : listaAtualizados) {
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
