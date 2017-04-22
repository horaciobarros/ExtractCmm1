package cmm.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.NotasFiscaisEmails;
import cmm.util.HibernateUtil;

public class NotasFiscaisEmailsDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public NotasFiscaisEmailsDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void save(NotasFiscaisEmails nfe) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(nfe);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<NotasFiscaisEmails> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscaisEmails c where hash is null").setFirstResult(0).setMaxResults(1200);
		List<NotasFiscaisEmails> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<NotasFiscaisEmails> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscaisEmails set hash = '" + hash + "' where ");

		for (NotasFiscaisEmails c : listaAtualizados) {
			builder.append("id = " + c.getId() + " or ");
		}

		String sql = builder.toString();
		sql = sql.toString().substring(0, sql.length() - 4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public List<NotasFiscaisEmails> findByPrestadorNumeroNota(String inscricao, String numero) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from NotasFiscaisEmails n where n.inscricaoPrestador like '"+inscricao+"' and n.numeroNota="+numero);
		List<NotasFiscaisEmails> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

}
