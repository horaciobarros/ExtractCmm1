package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Pagamentos;
import cmm.util.HibernateUtil;

public class PagamentosDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public PagamentosDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void save(Pagamentos p) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(p);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<Pagamentos> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from Pagamentos c where hash is null");
		List<Pagamentos> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<Pagamentos> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Pagamentos set hash = '"+hash+"' where ");
		
		for (Pagamentos c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql).setFirstResult(0).setMaxResults(1000);
		query.executeUpdate();
		tx.commit();
	}

}