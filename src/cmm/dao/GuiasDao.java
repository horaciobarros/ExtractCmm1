package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Guias;
import cmm.util.HibernateUtil;

public class GuiasDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public GuiasDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public void save(Guias g) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(g);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<Guias> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from Guias c where hash is null");
		List<Guias> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<Guias> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Guias set hash = '"+hash+"' where ");
		
		for (Guias c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
	}
	
}
