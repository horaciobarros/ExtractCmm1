package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Prestadores;
import cmm.model.PrestadoresAtividades;
import cmm.util.HibernateUtil;

public class PrestadoresAtividadesDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public PrestadoresAtividadesDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public void save(PrestadoresAtividades pa) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(pa);
		session.beginTransaction().commit();
		session.close();
	}

	public List<PrestadoresAtividades> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("from PrestadoresAtividades pa where hash is null").setFirstResult(0).setMaxResults(1000);
		List<PrestadoresAtividades> lista = query.list();
		tx.commit();session.close();

		return lista;
	}
	
	public void saveHash(List<PrestadoresAtividades> listaAtualizados, String hash){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update PrestadoresAtividades set hash = '"+hash+"' where ");
		
		for (PrestadoresAtividades pa : listaAtualizados){
			builder.append("id = "+pa.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
	}
	
	public PrestadoresAtividades findByInscricao(String inscricao) {
		Query query = sessionFactory.openSession()
				.createQuery("from PrestadoresAtividades pa  " + " where pa.inscricaoPrestador like '%" + inscricao.trim() + "%'");

		
		try {
			List<PrestadoresAtividades> prestadoresAtividadesList = query.list();

			if (prestadoresAtividadesList.size() > 0) {
				return prestadoresAtividadesList.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	
	}

}
