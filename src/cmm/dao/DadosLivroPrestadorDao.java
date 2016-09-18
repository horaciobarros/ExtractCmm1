package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import cmm.entidadesOrigem.DadosLivroPrestador;
import cmm.util.HibernateUtil;

public class DadosLivroPrestadorDao {

	StringBuilder hql;
	private SessionFactory sessionFactory;

	public DadosLivroPrestadorDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	public DadosLivroPrestador save(DadosLivroPrestador dlp) {
		Session session = sessionFactory.openSession();
		try {
			session.beginTransaction();
			session.save(dlp);
			session.getTransaction().commit();
			return dlp;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			session.close();
		}
	}

	public DadosLivroPrestador findByPrestadorAndCodigoAtividade(String inscricaoPrestador, String codigoAtividade) {
		Query query = sessionFactory.openSession().createQuery("from DadosLivroPrestador dlp  " + " where dlp.cnpjPrestador like :cnpj and dlp.codigoAtividadeMunipal like :codigoAtividade ")
				.setParameter("cnpj", inscricaoPrestador)
				.setParameter("codigoAtividade", codigoAtividade);

		try {
			List<DadosLivroPrestador> dlps = query.list();

			if (dlps.size() > 0) {
				return dlps.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public DadosLivroPrestador findById(Long id) {
		Query query = sessionFactory.openSession().createQuery("from DadosLivroPrestador dlp  " + " where dlp.idCodigo = " + id);

		try {
			List<DadosLivroPrestador> dlps = query.list();

			if (dlps.size() > 0) {
				return dlps.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean exists(Long id) {
		Query query = sessionFactory.openSession().createQuery("from DadosLivroPrestador dlp  where dlp.idCodigo = " + id);

		try {
			List<DadosLivroPrestador> dlps = query.list();

			if (dlps.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
