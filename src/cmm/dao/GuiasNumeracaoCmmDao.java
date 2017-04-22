package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cmm.model.GuiasNumeracaoCmm;
import cmm.util.HibernateUtil;


/**
 * @author Fernando Werneck - 20/09/2016
 * Analista Desenvolvedor
 * fernandowtb@hotmail.com
 * www.jwaysistemas.com.br
 * (31) 98594-8242
 */
public class GuiasNumeracaoCmmDao {
	

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public GuiasNumeracaoCmmDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	
	public List<GuiasNumeracaoCmm> findAll() {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		Query query = session.createQuery("from GuiasNumeracaoCmm g ");
		List<GuiasNumeracaoCmm> lista = query.list();
		tx.commit();
		session.close();

		return lista;
	}
	
	public GuiasNumeracaoCmm findById(int id) {
		Session session = sessionFactory.openSession();
		Query query = session
				.createQuery("from GuiasNumeracaoCmm e where e.id = :id" 
						).setParameter("id", id);

		try {
			GuiasNumeracaoCmm g = (GuiasNumeracaoCmm) query.uniqueResult();
			return g;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}
	
	public GuiasNumeracaoCmm findByNossoNumero(String nossoNumero) {
		Session session = sessionFactory.openSession();
		Query query = session
				.createQuery("from GuiasNumeracaoCmm e where e.nossoNumero = :nossoNumero" 
						).setParameter("nossoNumero", nossoNumero);

		try {
			GuiasNumeracaoCmm g = (GuiasNumeracaoCmm) query.uniqueResult();
			return g;
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return null;
	}

}
