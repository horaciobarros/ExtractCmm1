package cmm.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Competencias;
import cmm.model.Guias;
import cmm.util.HibernateUtil;

public class GuiasDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public GuiasDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void save(Guias g) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(g);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}
	
	public void update(Guias g) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.update(g);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public List<Guias> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Guias c where hash is null").setFirstResult(0).setMaxResults(1000);
		List<Guias> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void saveHash(List<Guias> listaAtualizados, String hash) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Guias set hash = '" + hash + "' where ");

		for (Guias c : listaAtualizados) {
			builder.append("id = " + c.getId() + " or ");
		}

		String sql = builder.toString();
		sql = sql.toString().substring(0, sql.length() - 4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();
		session.close();
	}

	public Guias findByNumeroGuia(String nossoNumero) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Guias g where g.numeroGuiaOrigem=" + Long.valueOf(nossoNumero.trim()));
		List<Guias> lista = query.list();
		tx.commit();
		session.close();

		if (lista.size() > 0) {
			return lista.get(0);
		} else {
			return null;
		}
	}
	

	public List<Guias> findAll() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Guias g ");
		List<Guias> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}

	public void delete(Guias guias) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.delete(guias);
		session.beginTransaction().commit();
		session.close();
		
	}

	public Guias findByPrestadorCompetencia(String inscricaoPrestador, Competencias competencias) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from Guias g join fetch g.competencias c where g.inscricaoPrestador='"+inscricaoPrestador+"' and c.id="+competencias.getId());
		List<Guias> lista = query.list();
		tx.commit();
		session.close();
		if (lista!=null && lista.size()>0){
			return lista.get(0);	
		}
		else{
			return null;
		}
		
	}


}
