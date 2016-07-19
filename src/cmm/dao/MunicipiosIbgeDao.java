package cmm.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import cmm.model.MunicipiosIbge;
import cmm.util.HibernateUtil;

public class MunicipiosIbgeDao  {
	
	StringBuilder hql;
	private SessionFactory sessionFactory;

	public MunicipiosIbgeDao() {
		sessionFactory = HibernateUtil.getSessionFactory();
	}

	
	public String getCodigoIbge(String nomeMunicipio, String uf) {
		Query query = sessionFactory.openSession()
				.createQuery("from MunicipiosIbge m  where upper(m.municipio) like :nomemun"
						+ " and upper(m.uf) like :uf")
		.setParameter("nomemun", "%" + nomeMunicipio.trim().toUpperCase()+ "%")
		.setParameter("uf", "%" + uf.trim().toUpperCase() + "%");
		

		try {
			List<MunicipiosIbge> municipios = query.list();

			if (municipios.size() > 0) {
				return municipios.get(0).getCodigo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public String findUfByCodigoIbge(Long codigo) {
		Query query = sessionFactory.openSession()
				.createQuery("from MunicipiosIbge m where m.codigo = :codigo" 
						).setParameter("codigo", codigo);

		try {
			List<MunicipiosIbge> municipios = query.list();

			if (municipios.size() > 0) {
				return municipios.get(0).getUf();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
