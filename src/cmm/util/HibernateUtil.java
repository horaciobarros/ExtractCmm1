package cmm.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {

			File file = new File("hibernate.cfg.xml");
			if (!file.exists()) {
				JOptionPane
						.showMessageDialog(null,
								"arquivo de configura��o do Banco de Dados n�o encontrado!");
				throw new ExceptionInInitializerError();
			} else {
				AnnotationConfiguration configuration = new AnnotationConfiguration();
				/*configuration
						.addAnnotatedClass(esaude.model.EsusTipoatividadecoletiva.class);*/
				SessionFactory sessionFactory = configuration.configure(file)
						.buildSessionFactory();
				return sessionFactory;
			}
		} catch (Throwable ex) {
			JOptionPane.showMessageDialog(null,
					"Erro em configura��o do banco! Detalhes no log da aplica��o");
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

}
