package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.PrestadoresOptanteSimples;
import cmm.util.HibernateUtil;

public class PrestadoresOptanteSimplesDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public PrestadoresOptanteSimplesDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void save(PrestadoresOptanteSimples pos) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(pos);
		session.beginTransaction().commit();
		session.close();
	}

	public List<PrestadoresOptanteSimples> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from PrestadoresOptanteSimples pos where hash is null").setFirstResult(0).setMaxResults(1000);
		List<PrestadoresOptanteSimples> lista = query.list();
		tx.commit();

		return lista;
	}
	
	public void saveHash(List<PrestadoresOptanteSimples> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update PrestadoresOptanteSimples set hash = '"+hash+"' where ");
		
		for (PrestadoresOptanteSimples pos : listaAtualizados){
			builder.append("id = "+pos.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
	}

}
