package cmm.util;
import java.io.File;

import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import cmm.model.GuiasNumeracaoCmm;


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
				configuration.addAnnotatedClass(cmm.model.Competencias.class);
				configuration
				.addAnnotatedClass(cmm.model.Guias.class);
		
				configuration
				.addAnnotatedClass(cmm.model.GuiasNotasFiscais.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscais.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisCanceladas.class);
				
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisCondPagamentos.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisEmails.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisObras.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisPrestadores.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisServicos.class);
				
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisSubst.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisTomadores.class);
		
				configuration
				.addAnnotatedClass(cmm.model.NotasFiscaisXml.class);
		
				configuration
				.addAnnotatedClass(cmm.model.Pagamentos.class);
		
				configuration
				.addAnnotatedClass(cmm.model.Prestadores.class);
		
				configuration
				.addAnnotatedClass(cmm.model.PrestadoresAtividades.class);
		
				configuration
				.addAnnotatedClass(cmm.model.PrestadoresOptanteSimples.class);
				
				configuration
				.addAnnotatedClass(cmm.model.Tomadores.class);
				
				configuration
				.addAnnotatedClass(cmm.model.Pessoa.class);
				
				configuration
				.addAnnotatedClass(cmm.model.MunicipiosIbge.class);
				
				configuration
				.addAnnotatedClass(cmm.model.Bairros.class);
				
				configuration
				.addAnnotatedClass(cmm.model.Logradouros.class);
				
				configuration
				.addAnnotatedClass(GuiasNumeracaoCmm.class);


				SessionFactory sessionFactory = configuration.configure(file)
						.buildSessionFactory();
				return sessionFactory;
			}
		} catch (Throwable ex) {
			JOptionPane.showMessageDialog(null,
					"Erro em configura��o do banco! Detalhes no log da aplica��o");
			ex.printStackTrace();
			return null;
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}

}
