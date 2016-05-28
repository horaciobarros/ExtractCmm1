package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.Pessoa;
import cmm.util.HibernateUtil;

public class PessoaDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;
	
	public PessoaDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Pessoa findByCnpjCpf(String cnpjCpf) {
		Query query = sessionFactory.openSession()
				.createQuery("from Pessoa p  " + " where p.cnpjCpf like '%" + cnpjCpf.trim() + "%'");

		try {
			List<Pessoa> pessoa = query.list();

			if (pessoa.size() > 0) {
				return pessoa.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(Pessoa p) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(p);
		session.beginTransaction().commit();
		session.close();
	}
	
	public void update(Pessoa p) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(p);
		session.beginTransaction().commit();
		session.close();
	}
	
	
	public List<Pessoa> findNaoEnviados() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session
				.createQuery("from Pessoa p where hash is null").setFirstResult(0).setMaxResults(1000);;
		List<Pessoa> lista = query.list();
		tx.commit();session.close();

		return lista;
	}

	public void saveHash(List<Pessoa> listaAtualizados, String hash){
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder builder = new StringBuilder();
		builder.append("update Pessoa set hash = '"+hash+"' where ");
		
		for (Pessoa c : listaAtualizados){
			builder.append("id = "+c.getId()+" or ");
		}
		
		String sql = builder.toString();
		sql = sql.toString().substring(0,sql.length()-4);
		Query query = session.createQuery(sql);
		query.executeUpdate();
		tx.commit();session.close();
	}
}
