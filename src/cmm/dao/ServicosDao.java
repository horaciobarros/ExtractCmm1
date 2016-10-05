package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.entidadesOrigem.Servicos;
import cmm.util.HibernateUtil;

public class ServicosDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public ServicosDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public Servicos findByIdOrigem(Long id) {
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Servicos s where s.idOrigem = :id").setParameter("id", id);

		try {
			List<Servicos> servicos = query.list();

			if (servicos.size() > 0) {
				return servicos.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public Servicos findByCodigo(String codigo) {
		Session session = sessionFactory.openSession();
		try {

			Query query = session.createQuery("from Servicos e where e.codigo = :codigo").setParameter("codigo", codigo);

			List<Servicos> servicos = query.list();

			if (servicos.size() > 0) {
				return servicos.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public Servicos findByCodigoServicoCodigoCnae(String codigoServico, String codigoCnae) {
		Session session = sessionFactory.openSession();
		try {
			Query query = session.createQuery("from Servicos e where e.codigo = :codigoServico and e.cnaes = :codigoCnae")
					.setParameter("codigoServico", codigoServico).setParameter("codigoCnae", codigoCnae);

			List<Servicos> servicos = query.list();

			if (servicos.size() > 0) {
				return servicos.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public Servicos save(Servicos s) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(s);
			session.getTransaction().commit();
		} catch (Exception e2) {
			e2.printStackTrace();
			throw e2;
		} finally {
			session.close();
		}
		return s;
	}
	
	public List<Servicos> findAll() {
		
		Session session = sessionFactory.openSession();
		try {
			Query query = session.createQuery("from Servicos ");

			List<Servicos> servicos = query.list();
			
			return servicos;


		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
		
	}


}
