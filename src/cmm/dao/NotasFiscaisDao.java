package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.NotasFiscais;
import cmm.util.HibernateUtil;

public class NotasFiscaisDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public NotasFiscaisDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void save(NotasFiscais nf) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nf);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<NotasFiscais> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from NotasFiscais c where hash is null").setFirstResult(0).setMaxResults(500);
		List<NotasFiscais> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<NotasFiscais> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscais set hash = '"+hash+"' where ");
		
		for (NotasFiscais c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
	}

}
