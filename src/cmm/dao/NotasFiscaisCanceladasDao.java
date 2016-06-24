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
	
	public NotasFiscaisCanceladasDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}
	
	public void save(NotasFiscaisCanceladas nfc) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfc);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<NotasFiscaisCanceladas> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("from NotasFiscaisCanceladas c where hash is null").setFirstResult(0).setMaxResults(850);
		List<NotasFiscaisCanceladas> lista = query.list();
		tx.commit();session.close();

		return lista;
	}

	public void saveHash(List<NotasFiscaisCanceladas> listaAtualizados, String hash){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscaisCanceladas set hash = '"+hash+"' where ");
		
		for (NotasFiscaisCanceladas c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
	}

}
