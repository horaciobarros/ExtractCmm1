package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.NotasFiscaisPrestadores;
import cmm.util.HibernateUtil;

public class NotasFiscaisPrestadoresDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public NotasFiscaisPrestadoresDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void save(NotasFiscaisPrestadores nfp) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfp);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<NotasFiscaisPrestadores> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from NotasFiscaisPrestadores c where hash is null").setFirstResult(0).setMaxResults(1000);
		List<NotasFiscaisPrestadores> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<NotasFiscaisPrestadores> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscaisPrestadores set hash = '"+hash+"' where ");
		
		for (NotasFiscaisPrestadores c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
	}
	
}
