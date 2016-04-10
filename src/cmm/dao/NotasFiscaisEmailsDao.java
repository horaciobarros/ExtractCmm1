package cmm.dao;

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
	Session session;
	
	public NotasFiscaisEmailsDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void save(NotasFiscaisEmails nfe) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfe);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<NotasFiscaisEmails> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from NotasFiscaisEmails c where hash is null");
		List<NotasFiscaisEmails> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<NotasFiscaisEmails> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscaisEmails set hash = '"+hash+"' where ");
		
		for (NotasFiscaisEmails c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
	}

}
