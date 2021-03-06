package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Guias;
import cmm.model.Pagamentos;
import cmm.util.HibernateUtil;

public class PagamentosDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public PagamentosDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void save(Pagamentos p) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(p);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public void update(Pagamentos p) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.update(p);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<Pagamentos> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Pagamentos c where hash is null").setFirstResult(0).setMaxResults(1000);
		List<Pagamentos> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<Pagamentos> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Pagamentos set hash = '" + hash + "' where ");

		for (Pagamentos c : listaAtualizados) {
			builder.append("id = " + c.getId() + " or ");
		}

		String sql = builder.toString();
		sql = sql.toString().substring(0, sql.length() - 4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}
	
	public void deleteByGuia(Guias guia) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("delete from pagamentos where guia_id = " + guia.getId());
		String sql = builder.toString();
		Query query = session.createSQLQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
	}
	
	public Pagamentos findPorIdGuia(Long idGuia) {
		Session session = sessionFactory.openSession();
		Query query = session
				.createQuery("from Pagamentos c left join fetch c.guias g where g.id = :id").setParameter("id", idGuia);
		Pagamentos pagamento = (Pagamentos) query.uniqueResult();
		session.close();
		return pagamento;
	}

}
