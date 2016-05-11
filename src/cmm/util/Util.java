package cmm.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cmm.entidadesOrigem.DadosLivroPrestador;

public class Util {

	public String CODIGO_IBGE = "3137205";

	public static Long castToLong(Object value, Long defaultValue) {
		if (value != null) {
			if (value instanceof Long) {
				return (Long) value;
			} else if (value instanceof Number) {
				return new Long(((Number) value).longValue());
			} else if (value instanceof String) {
				try {
					return value.equals("") ? defaultValue : new Long((String) value);
				} catch (NumberFormatException exn) {
					return defaultValue;
				}
			} else if (value instanceof BigInteger) {
				return ((BigInteger) value).longValue();
			} else if (value instanceof BigDecimal) {
				return ((BigDecimal) value).longValue();
			}
		}
		return defaultValue;
	}

	public static Long castToLong(Object value) {
		return castToLong(value, null);
	}

	public static boolean isBlank(String text) {
		if (text != null && text.length() > 0) {
			for (int i = 0, iSize = text.length(); i < iSize; i++) {
				if (text.charAt(i) != ' ') {
					return false;
				}
			}
		}

		return true;
	}

	public static boolean isNotBlank(String nome) {
		return !isBlank(nome);
	}

	public static Integer castToInteger(Object value, Integer defaultValue) {
		if (value != null) {
			if (value instanceof Integer) {
				return (Integer) value;
			} else if (value instanceof Number) {
				return new Integer(((Number) value).intValue());
			} else if (value instanceof BigInteger) {
				return ((BigInteger) value).intValue();
			} else if (value instanceof BigDecimal) {
				return ((BigDecimal) value).intValue();
			} else if (value instanceof String) {
				try {
					return value.equals("") ? defaultValue : new Integer((String) value);
				} catch (NumberFormatException exn) {
					return defaultValue;
				}
			}
		}
		return defaultValue;
	}

	public static Integer castToInteger(Object value) {
		return castToInteger(value, null);
	}

	public String getNomeMes(String mes) {
		String[] meses = { "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro",
				"Outubro", "Novembro", "Dezembro" };

		return meses[Integer.valueOf(mes) - 1];
	}

	public Date getStringToDateHoursMinutes(String data) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			Date date = (Date) formatter.parse(data);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Date getStringToDate(String data) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = (Date) formatter.parse(data);
			return date;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getOptantePeloSimplesNacional(String optantePeloSimplesNacional) {
		return optantePeloSimplesNacional.substring(0, 1);
	}

	public String getTipoPessoa(String inscricao) {
		return (inscricao.length() == 11 ? "F" : "J");
	}

	public BigDecimal getSumOfBigDecimal(List<BigDecimal> lista) {

		Double valorAux = Double.valueOf(0);

		for (BigDecimal valor : lista) {
			valorAux += valor.doubleValue();
		}
		return BigDecimal.valueOf(valorAux);

	}

	public BigDecimal getSubtract(BigDecimal valor1, BigDecimal valor2) {

		return BigDecimal.valueOf(valor1.doubleValue() - valor2.doubleValue());
	}

	public Date getFirstDayOfMonth(String ano, String mes) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, (Integer.valueOf(ano)));
		calendar.set(Calendar.MONTH, (Integer.valueOf(mes) - 1));
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}

	public Date getLastDayOfMonth(String ano, String mes) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, (Integer.valueOf(ano)));
		calendar.set(Calendar.MONTH, (Integer.valueOf(mes) - 1));
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendar.getTime();
	}

	public String getSituacaoTributaria(DadosLivroPrestador dlp) {
		try {
			if (dlp.getTipoRetencao().substring(0, 1).equals("R")) {
				return "R";
			} else {
				return "N";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "N";
			
		}
	}

}
