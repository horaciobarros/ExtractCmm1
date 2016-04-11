package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Competencias;
import cmm.util.HibernateUtil;

public class CompetenciasDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	Session session;

	public CompetenciasDao() {

		sessionFactory = HibernateUtil.getSessionFactory();
		session = sessionFactory.openSession();
	}

	public Competencias findByDescricao(String descricao) {
		Query query = sessionFactory.openSession()
				.createQuery("from Competencias cp  " + " where cp.descricao like '%" + descricao.trim() + "%'");

		try {
			List<Competencias> competencias = query.list();

			if (competencias.size() > 0) {
				return competencias.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(Competencias cp) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(cp);
		session.beginTransaction().commit();
		session.close();
	}
	
	public List<Competencias> findNaoEnviados() {
		Transaction tx = session.beginTransaction();
		Query query = sessionFactory
				.openSession()
				.createQuery("from Competencias c where hash is null");
		List<Competencias> lista = query.list();
		tx.commit();

		return lista;
	}

	public void saveHash(List<Competencias> listaAtualizados, String hash){
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Competencias set hash = '"+hash+"' where ");
		
		for (Competencias c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql).setFirstResult(0).setMaxResults(1000);;
		query.executeUpdate();
		tx.commit();
	}
}
