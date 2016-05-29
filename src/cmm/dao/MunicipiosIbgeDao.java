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

	
	public String getCodigo(String nomeMunicipio) {
		Query query = sessionFactory.openSession()
				.createQuery("from MunicipiosIbge m  " + " where upper(m.municipio) like '%" + nomeMunicipio.trim().toUpperCase() + "%'");

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


}
