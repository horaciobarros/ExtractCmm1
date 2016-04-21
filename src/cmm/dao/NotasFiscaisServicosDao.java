package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.NotasFiscaisServicos;
import cmm.util.HibernateUtil;

public class NotasFiscaisServicosDao {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;
	
	public NotasFiscaisServicosDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}
	
	public void save(NotasFiscaisServicos nfs) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(nfs);
		session.beginTransaction().commit();
		session.close();
	}

	public List<NotasFiscaisServicos> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from NotasFiscaisServicos c where hash is null").setFirstResult(0).setMaxResults(1000);
		List<NotasFiscaisServicos> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<NotasFiscaisServicos> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update NotasFiscaisServicos set hash = '"+hash+"' where ");
		
		for (NotasFiscaisServicos c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
	}
}
