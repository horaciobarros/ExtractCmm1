package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.NotasFiscaisCanceladas;
import cmm.util.HibernateUtil;

public class NotasFiscaisCanceladasDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;
	
	public NotasFiscaisCanceladasDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void save(NotasFiscaisCanceladas nfc) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfc);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<NotasFiscaisCanceladas> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from NotasFiscaisCanceladas c where hash is null");
		List<NotasFiscaisCanceladas> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<NotasFiscaisCanceladas> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscaisCanceladas set hash = '"+hash+"' where ");
		
		for (NotasFiscaisCanceladas c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql).setFirstResult(0).setMaxResults(1000);;
		query.executeUpdate();
		tx.commit();
	}

}
