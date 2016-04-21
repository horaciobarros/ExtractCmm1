package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Prestadores;
import cmm.util.HibernateUtil;

public class PrestadoresDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public PrestadoresDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Prestadores findByInscricao(String inscricao) {
		Query query = sessionFactory.openSession()
				.createQuery("from Prestadores p  " + " where p.inscricaoPrestador like '%" + inscricao.trim() + "%'");

		try {
			List<Prestadores> prestadores = query.list();

			if (prestadores.size() > 0) {
				return prestadores.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(Prestadores p) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(p);
		session.beginTransaction().commit();
		session.close();
	}

	public Prestadores findByInscricaoMunicipal(String inscMunicipal) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Prestadores> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from Prestadores p where hash is null").setFirstResult(0).setMaxResults(1000);
		List<Prestadores> lista = query.list();
		tx.commit();

		return lista;
	}
	
	public void saveHash(List<Prestadores> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Prestadores set hash = '"+hash+"' where ");
		
		for (Prestadores c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
	}

}
