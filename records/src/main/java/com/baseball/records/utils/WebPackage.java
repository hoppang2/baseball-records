package com.baseball.records.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.baseball.records.common.Prop;

public class WebPackage {
	static final Logger logger = LogManager.getLogger(WebPackage.class);

	public static List<String> toArrayList(String str, String regex) {
		List<String> rtn = new ArrayList<>();
		if (StringUtils.isBlank(str)) {
			return rtn;
		}
		String[] strArr = StringUtils.split(str, ",");
		if (strArr.length == 0) {
			return rtn;
		}
		for (int i = 0; i < strArr.length; i++) {
			rtn.add(strArr[i].trim());
		}

		return rtn;
	}

	/**
	 * HTTP ???????????? ?????? ?????? ?????? ??????.
	 *
	 * @param request
	 * @param headerName
	 * @param method
	 * @return
	 */
	public static String getValueFromHeader(HttpServletRequest request, String headerName, String method) {
		String rtn = "";

		String headerNameVal = request.getHeader(headerName);

		if (StringUtils.isBlank(headerNameVal)) {
			return "";
		}

		String[] headerNameVals = headerNameVal.split(";");
		for (String raw : headerNameVals) {
			String item = raw.trim();
			if (item.startsWith(method)) {
				rtn = item.replace(method, "");
			}
		}
		return rtn;
	}

	/**
	 * HTTP ???????????? ?????? ?????? ?????? ??????.
	 *
	 * @param request
	 * @param headerName
	 * @param method
	 * @return
	 */
	public static String getValueFromHeader(HttpServletRequest request, String headerName) {

		String headerNameVal = request.getHeader(headerName);

		if (StringUtils.isBlank(headerNameVal)) {
			return "";
		}
		return headerNameVal.trim();
	}

	/**
	 * ?????? ???????????? ???????????? ????????????.(V4)
	 *
	 * @return
	 */
	public static String getLocalServerIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
							&& inetAddress.isSiteLocalAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException e) {
			logger.error("????????? ?????? ??????", e);
		}
		return "";
	}

	/**
	 * ???????????? ???????????? ???????????? ????????????
	 *
	 * @param map
	 * @return
	 */
	public static String encMapToStr(Map<String, Object> map) {

		if (map == null) {
			return null;
		}

		String str = toJson(map);
		String encStr = EncUtil.AesEncode(str);
		return encStr;
	}

	/**
	 * ???????????? ???????????? map?????? ????????????.
	 *
	 * @param encStr
	 * @return
	 */
	public static CamelCaseMap<String, Object> decStrToCMap(String encStr) {
		CamelCaseMap<String, Object> rtn = new CamelCaseMap<String, Object>();

		if (StringUtils.isBlank(encStr)) {
			return rtn;
		}
		String str = EncUtil.AesDecode(encStr);

		try {
			rtn = jsonToCMap(str);
		} catch (IOException e) {
			logger.error("????????? ??????", e);
		}

		return rtn;
	}

	/**
	 * ????????? ????????????.
	 */
	public static String onlyNum(String val) {
		return val.replaceAll("[^\\d]", "");
	}

	/**
	 * ????????? ?????? ?????????.
	 */
	public static Date addMinite(Date date, int min) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, min);
		return cal.getTime();
	}

	/**
	 * ????????? ????????? ?????????.
	 */
	public static Date addHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);
		return cal.getTime();
	}

	/**
	 * ????????? ?????? ?????????.
	 */
	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

	/**
	 * ???????????? text ????????? ??????
	 */
	public static String getReourceAsString(String path, String defValue) {
//		URL url = Resources.getResource(path);
//		String text = "";
//		try {
//			text = Resources.toString(url, Charsets.UTF_8);
//		} catch (IOException e1) {
//			logger.error("????????? ?????? ??? ??????", e1);
//			return defValue;
//		}
//		return text;

		try {
			ClassPathResource cpr = new ClassPathResource(path);
			byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
			return new String(bdata, StandardCharsets.UTF_8);
		} catch (IOException e) {
			logger.error("????????? ?????? ??? ??????", e);
			return defValue;
		}
	}

	public static String getReourceAsString(String path) {
		return getReourceAsString(path, "");
	}

	public static String toAlphabetic(int i) {
		if (i < 0) {
			return "-" + toAlphabetic(-i - 1);
		}

		int quot = i / 26;
		int rem = i % 26;
		char letter = (char) ((int) 'A' + rem);
		if (quot == 0) {
			return "" + letter;
		} else {
			return toAlphabetic(quot - 1) + letter;
		}
	}

	/**
	 * ???????????? ??? ?????????
	 */
	public static String toFormatPhone(String number) {
		if (StringUtils.isBlank(number)) {
			return null;
		}

		// ????????? ?????????
		String num = number.replaceAll("[^0-9]", "");

		String rtn = "";
		if (num.length() == 8) {
			rtn = num.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
		} else if (num.length() == 12) {
			rtn = num.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
		} else {
			rtn = num.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
		}

		if (rtn.indexOf("-") == -1) {
			return null;
		} else {
			return rtn;
		}

	}

	/**
	 * ???????????? ????????? ??????
	 */
	public static Date getMonday(Date evtDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(evtDate);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return cal.getTime();
	}

	/**
	 * ????????? ??????
	 */
	public static Date getSunday(Date evtDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(evtDate);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.add(Calendar.DATE, 7);
		return cal.getTime();
	}

	/**
	 * ???????????? base64 ????????? ??????.
	 */
	public static String toBase64(String txt) {
		byte[] encodedBytes = Base64.getEncoder().encode(txt.getBytes());
		return new String(encodedBytes);
	}

	/**
	 * ???????????? base64 ????????? ??????.
	 */
	public static String decBase64(String txt) {
		byte[] decodeBytes = Base64.getDecoder().decode(txt.getBytes());
		return new String(decodeBytes);
	}

	/**
	 * HH:mm:ss ????????? ????????? ????????????.
	 */
	public static int hmsToMin(String hms) {
		if (hms == null || StringUtils.isBlank(hms)) {
			logger.warn("hsm is null");
			return 0;
		}

		String[] mins = hms.split(":");
		if (mins.length != 2 && mins.length != 3) {
			logger.warn("hsm is type error");
			return 0;
		}
		return WebPackage.toInt(mins[0]) * 60 + WebPackage.toInt(mins[1]);
	}

	/**
	 * ????????? ????????? ?????? ??? ????????? ????????? ??????
	 */
	public static boolean isBetweenTime(int startMin, int endMin, Date dt) {

		if (startMin == endMin) {
			// 24?????? ????????? ??????????????? ??????.
			return true;
		}

		if (startMin == 60 * 24) {
			// ????????? 24?????? ?????? 0??? ??????
			startMin = 0;
		}

		if (endMin == 0) {
			// ?????? 0??? ?????? 24?????? ??????
			endMin = 60 * 24;
		}

		if (startMin == endMin) {
			// 24?????? ????????? ??????????????? ??????.
			return true;
		}

		if (startMin > endMin) {
			// ??????????????? ?????????
			logger.warn("?????? ??? ??????, {}, {}", startMin, endMin);
		}

		int nowMin = WebPackage.getMin(dt);

		if (endMin > 60 * 24) {
			// ?????? 24??? ????????? ?????? -1440
			endMin = endMin - (60 * 24);

			if (startMin == endMin) {
				// 24?????? ????????? ??????????????? ??????.
				return true;
			}

			// ?????????????????? ?????????????????? true
			return (startMin < nowMin || nowMin < endMin);

		} else {
			return (startMin < nowMin && nowMin < endMin);
		}
	}

	/**
	 * ?????? 0??? ?????? ?????? ?????? ????????????.
	 */
	public static int getMin(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
	}

	/**
	 * @param sDate ?????????, ?????? ????????? 00:00:00 ??? ?????? date ????????? ?????? ?????? ?????? ????????????.
	 */
	public static int getMin(Date sDate, Date date) {
		String sDateStr = WebPackage.DateToString(sDate, "yyyy-MM-dd");
		return getMin(sDateStr, date);
	}

	public static int getMin(String ymd, Date date) {
		Date compDt = WebPackage.toDate(ymd, "yyyy-MM-dd");
		long diff = (date.getTime() - compDt.getTime()) / (60 * 1000);
		return (int) diff;
	}

	/**
	 * ??????2 ?????? ??????1??? ?????? "???"????????? ??????
	 */
	public static int getMinDiff(Date smallDate, Date largeDate) {
		long diff = (largeDate.getTime() - smallDate.getTime()) / (1000 * 60);
		return WebPackage.toInt(diff);
	}

	/**
	 * ??????2 ?????? ??????1??? ?????? "???"????????? ??????
	 */
	public static int getHourDiff(Date date1, Date date2) {
		long diff = (date2.getTime() - date1.getTime()) / (1000 * 60 * 60);
		return WebPackage.toInt(diff);
	}

	/**
	 * ??????????????? ?????? ?????????
	 */
	public static int getBetweenDayCount(Date startDate, Date endDate) {
		List<Date> rtn = getBetweenDates(startDate, endDate);
		return rtn.size();
	}

	/**
	 * ????????? ????????? ???????????? ?????? ????????? ????????????.
	 */
	public static List<Date> getBetweenDates(Date startDate, Date endDate) {
		List<Date> rtn = new ArrayList<>();

		Date date1 = null;
		String dateStr2 = "";
		if (startDate.getTime() > endDate.getTime()) {
			dateStr2 = WebPackage.DateToString(startDate, "yyyyMMdd");
			date1 = endDate;
		} else {
			dateStr2 = WebPackage.DateToString(endDate, "yyyyMMdd");
			date1 = startDate;
		}
		Calendar cal = Calendar.getInstance();
		String cDateStr = "";

		int i = 0;
		while (i < 20000) {
			cDateStr = WebPackage.DateToString(date1, "yyyyMMdd");
			rtn.add(date1);

			if (cDateStr.equals(dateStr2)) {
				break;
			} else {
				cal.setTime(date1);
				cal.add(Calendar.DATE, 1);
				date1 = cal.getTime();
				i++;
			}
		}

		return rtn;
	}

	/**
	 * ????????? ??????
	 */
	public static String zero2Pading(int val) {
		return StringUtils.leftPad(String.valueOf(val), 2, "0");
	}

	/**
	 * ??????????????? ???????????? ??????????????? ????????? ?????????????????? ??????
	 */
	public static long deviceDateToTimeStamp(String dateStr) {
		try {
			SimpleDateFormat dt = new SimpleDateFormat("yyyyy-MM-dd'T'HH:mm:ss Z");
			Date date = dt.parse(dateStr);
			Timestamp ts = new Timestamp(date.getTime());
			return ts.getTime();
		} catch (ParseException e) {
			logger.error("deviceDateToTimeStamp ParseException", e);
			return 0;
		}
	}

	/**
	 * ????????? ?????????????????? ????????????.
	 */
	public static long dateToTimeStamp(Date date) {
		Timestamp ts = new Timestamp(date.getTime());
		return ts.getTime();
	}

	/**
	 * ???????????? ??????????????? ????????????.
	 */
	public static Date toDate(String dateStr, String format) {
		SimpleDateFormat dt = new SimpleDateFormat(format, Locale.KOREA);
		try {
			return dt.parse(dateStr);
		} catch (ParseException e) {
			logger.error("toDate ParseException", e);
			return new Date();
		}
	}

	/**
	 * ???????????? ???????????????
	 */
	public static Date toDateFormat(String dateStr, String format) {
		SimpleDateFormat dt = new SimpleDateFormat(format);
		try {
			return dt.parse(dateStr);
		} catch (ParseException e) {
			logger.error("toDate ParseException", e);
			return new Date();
		}

	}

	/**
	 * ???????????? ?????? ??????
	 *
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static LocalDate toLocalDate(int year, int month, int date) {
		try {
			LocalDate rtn = LocalDate.of(year, month, date);
			return rtn;
		} catch (Exception e) {
			logger.error("?????? ?????? ??????", e);
			return null;
		}
	}

	public static Date localDateToDate(LocalDate date) {
		return localDateToDate(date, null);
	}

	public static Date localDateToDate(LocalDate date, ZoneId zoneId) {
		if (zoneId == null) {
			zoneId = ZoneId.of("Asia/Seoul");
		}
		return Date.from(date.atStartOfDay(zoneId).toInstant());
	}

	public static Date toDate(Object date) {

		try {
			return (Date) date;
		} catch (RuntimeException e) {
			logger.error("toDate ParseException", e);
			return null;
		}

	}

	/**
	 * ????????? ???????????? ????????????.
	 */
	public static String DateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String DateToString(Date date, String format, Locale locale) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, locale);
		return sdf.format(date);
	}

	/**
	 * ????????? ???????????? ????????????.
	 */
	public static String DateToStringUtc(Date date, String format) {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		sdf.setTimeZone(tz); // strip timezone
		return sdf.format(date);
	}

	/**
	 * OBJECT ??? JSON ???????????? ????????????.
	 */
	public static String toJson(Object obj) {
		return toJson(obj, false);
	}

	/**
	 * OBJECT ??? JSON ???????????? ????????????.
	 */
	public static String toJson(Object obj, boolean pretty) {
		if (obj == null) {
			return "{}";
		}
		ObjectMapper mapper = new ObjectMapper();
		mapper.setTimeZone(TimeZone.getDefault()); // TODO ?????? ????????? ??????
		try {

			if (pretty) {
				return "\r\n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
			} else {
				return mapper.writeValueAsString(obj);
			}
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

	/**
	 * JSON ???????????? ????????? ??????
	 */
	public static <T> T jsonStringtoObject(Class<T> type, String json) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		T rtn = null;

		try {
			rtn = (T) mapper.readValue(json, type);
		} catch (IOException e) {
			logger.error(e);
			return null;
		}
		return rtn;
	}

	/**
	 * JSON ???????????? ????????? ?????? (???????????? ?????? ???????????? ??????) VO??? ???????????? ????????? ?????????????????? ????????? ??? ????????? ?????? ???????????? ?????????
	 * ????????? ????????? ????????? json???????????? ???????????? ?????? ??????
	 */
	public static <T> T jsonStringtoObjectIgnoreNull(Class<T> type, String json) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		T rtn = null;

		try {
			rtn = (T) mapper.readValue(json, type);
		} catch (IOException e) {
			logger.error(e);
			return null;
		}
		return rtn;
	}

	/**
	 * null??? ???????????? ???????????? ?????? ?????? ??????(trim ??????)
	 */
	public static boolean isStrEquals(Object compare1, Object compare2) {

		String str1 = "";
		String str2 = "";

		if (compare1 != null) {
			if (StringUtils.isNotBlank(compare1.toString())) {
				str1 = compare1.toString().trim();
			}
		}

		if (compare2 != null) {
			if (StringUtils.isNotBlank(compare2.toString())) {
				str2 = compare2.toString().trim();
			}
		}

		return str1.equalsIgnoreCase(str2);

	}

	/**
	 * ???????????? MAP ????????? ??????
	 */
	public static Map<String, Object> toMap(String val) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(val, new TypeReference<Map<String, Object>>() {
		});
	}

	public static <K, V> Map<K, V> jsonToMap(String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<Map<K, V>>() {
		});
	}

	public static CamelCaseMap<String, Object> jsonToCMap(String json)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, new TypeReference<CamelCaseMap<String, Object>>() {
		});
	}

	/**
	 * ???????????? List<map> ????????? ??????
	 */
	public static List<Map<String, Object>> toListMap(String val)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(val, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * json ??????????????? List<Map> ????????? ??????
	 */
	public static List<Map<String, Object>> valueToListMap(Object val) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String rawInput = mapper.writeValueAsString(val);

		return mapper.readValue(rawInput, new TypeReference<List<Map<String, Object>>>() {
		});
	}

	/**
	 * json ??????????????? List<CamelCaseMap> ????????? ??????
	 */
	public static List<CamelCaseMap<String, Object>> valueToListCMap(Object val) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String rawInput = mapper.writeValueAsString(val);

		return mapper.readValue(rawInput, new TypeReference<List<CamelCaseMap<String, Object>>>() {
		});
	}

	/**
	 * json ??????????????? Map ????????? ??????
	 */
	public static Map<String, Object> valueToMap(Object val) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		String rawInput = mapper.writeValueAsString(val);
		return mapper.readValue(rawInput, new TypeReference<Map<String, Object>>() {
		});
	}

	/**
	 * ??????????????? ????????????.
	 */
	public static Object getInputValue(Object obj) {
		return obj;
	}

	/**
	 * ???????????? ????????????.
	 */
	public static String removeFloat(String val) {

		if (StringUtils.isBlank(val)) {
			return val;
		}

		if (val.indexOf(".") == -1) {
			return val;
		}
		return val.substring(0, val.indexOf("."));

	}

	/**
	 * ????????? ???????????? ???????????????.
	 */
	public static String exceptionStacktraceToString(Exception e) {
		if (e == null) {
			return "";
		}
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

	public static int toInteger(Object object) {
		return WebPackage.toInteger(object, null);
	}

	public static Integer toInteger(Object object, Integer DefaultVal) {
		String inv = "";
		if (null != object) {
			inv = object.toString();
			if (StringUtils.isBlank(inv)) {
				return DefaultVal;
			}

			if (!WebPackage.isNumber(inv)) {
				return DefaultVal;
			}
		} else {
			return DefaultVal;
		}

		return Integer.parseInt(inv);
	}

	public static boolean toBoolean(Object object) {
		return WebPackage.toBoolean(object, false);
	}

	public static boolean toBoolean(Object object, boolean defaultValue) {
		String inv = "";
		if (null != object) {
			inv = object.toString();
			if (StringUtils.isBlank(inv)) {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}

		String[] trueVs = new String[] { "1", "true", "y", "yes", "ok" };

		for (String tv : trueVs) {
			if (tv.equalsIgnoreCase(inv)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Map??? ?????? ????????? ????????? ????????????.
	 */
	public static String mapItemToString(Map<String, Object> map, String key) {
		if (map.containsKey(key)) {
			if (map.get(key) != null) {
				return map.get(key).toString().trim();
			}

		}
		return "";

	}

	public static long toLong(Object object) {
		return WebPackage.toLong(object, 0);
	}

	public static long toLong(Object object, long DefaultVal) {
		String inv = "";
		if (null != object) {
			inv = object.toString();
			if (StringUtils.isBlank(inv)) {
				return DefaultVal;
			}

			if (!WebPackage.isNumber(inv)) {
				return DefaultVal;
			}
		} else {
			return DefaultVal;
		}

		return Long.parseLong(inv);
	}

	public static Long toLongNullAble(Object object) {
		String inv = "";
		if (null != object) {
			inv = object.toString();
			if (StringUtils.isBlank(inv)) {
				return null;
			}

			if (!WebPackage.isNumber(inv)) {
				return null;
			}
		} else {
			return null;
		}

		return Long.parseLong(inv);
	}

	public static double toDouble(Object object, double defaultVal) {
		String inv = "";
		if (null != object) {
			inv = object.toString();
			if (StringUtils.isBlank(inv)) {
				return defaultVal;
			}

			if (!WebPackage.isNumber(inv)) {
				return defaultVal;
			}
		} else {
			return defaultVal;
		}

		return Double.parseDouble(inv);
	}

	public static double toDouble(Object object) {
		return toDouble(object, 0);
	}

	public static String decimalFormat(String formater, double val) {
		DecimalFormat form = new DecimalFormat(formater);
		return form.format(val);
	}

	public static int toInt(Object object) {
		return WebPackage.toInt(object, 0);
	}

	public static int toInt(Object object, int defaultVal) {

		if (null == object) {
			return defaultVal;
		}

		String inv = object.toString();

		try {
			return Integer.parseInt(inv);
		} catch (Exception e) {
			return defaultVal;
		}
	}

	public static String toString(Object inv) {
		return WebPackage.toString(inv, "");
	}

	public static String toString(Object inv, String DefaultVal) {

		if (inv == null) {
			return DefaultVal;
		}

		if (StringUtils.isBlank(inv.toString())) {
			return DefaultVal;
		}

		return inv.toString();
	}

	public static String toString(Object inv, String DefaultVal, String joinVal) {

		if (inv == null) {
			return DefaultVal;
		}

		if (StringUtils.isBlank(inv.toString())) {
			return DefaultVal;
		}

		// if (inv.toString().equals("0")) {
		// return DefaultVal;
		// }

		return inv.toString() + joinVal;
	}

	public static String[] SplitStr(String str, String regex, int size) {
		String[] rtn = new String[size];

		String[] tmp = str.split(regex);
		int tmpSize = tmp.length;

		for (int i = 0; i < rtn.length; i++) {
			if (i < tmpSize) {
				rtn[i] = tmp[i];
			} else {
				rtn[i] = "";
			}

		}
		return rtn;
	}

	/**
	 * ?????? ???????????? ???????????????.
	 */
	public static boolean isNumber(String str) {
		if (str == null) {
			return false;
		}
		Pattern p = Pattern.compile("([\\p{Digit}]+)(([.]?)([\\p{Digit}]+))?");
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * ??????????????? ???????????????.
	 */
	public static String ToMoney(Object val) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		return nf.format(val);
	}

	public static String CutStr(Object con, String endString, int len) {
		if (con == null) {
			return "";
		}
		String str = con.toString();

		int sl = str.length();

		if (len < sl) {
			return str.substring(0, len - endString.length()) + endString;
		} else {
			return str;
		}

	}

	/**
	 * ????????? ????????? ????????????.
	 *
	 * @param alterText 1 length??? ????????? ?????? ???????????? ?????????.
	 */
	public static String hideRightString(String value, int length, String alterText) {
		int compLength = length;
		int s = value.length() - compLength;
		if (s <= 0) {
			s = 0;
			compLength = value.length();
		}
		return StringUtils.overlay(value, StringUtils.repeat(alterText, compLength), s, value.length());
	}

	public static int getTopRowIndex(int cPage, int pageSize, int totalRowCount) {
		return totalRowCount - ((cPage - 1) * pageSize);
	}

	public static String CutDecimalPoint(Object in, int digit) {
		if (in == null) {
			return "0";
		}

		double inv = Double.parseDouble(in.toString());

		String pattern = "";

		if (digit == 0) {
			pattern = "#";
		} else {
			pattern = "#.";
			for (int i = 0; i < digit; i++) {
				pattern += "#";
			}
		}

		DecimalFormat dformat = new DecimalFormat(pattern);

		return dformat.format(inv);
	}

	/**
	 * ?????????????????? ??????????????? ??????
	 */
	public static String TimeStampToString(Timestamp ts, String fommat) {
		SimpleDateFormat sdf = new SimpleDateFormat(fommat, Locale.KOREA);
		return sdf.format(new Date(ts.getTime()));
	}

	/**
	 * -??? ????????? UUID??? ????????????.
	 *
	 * @return
	 */
	public static String getUuid() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * ????????? ?????? ????????? ??? ?????? ????????? ???????????????.
	 */
	public static String createRandomNumber(int len) {
		Random oRandom = new Random();

		String rtn = "";

		for (int i = 0; i < len; i++) {
			rtn += oRandom.nextInt(10);
		}
		return rtn;
	}

	/**
	 * ?????? ???????????? ?????? ????????? ????????????.
	 *
	 * @param len
	 * @param min
	 * @param max
	 * @return
	 * @throws Exception
	 */
	public static String createRandomNumber(int len, int min, int max) throws Exception {

		if (min > max) {
			throw new Exception("max ?????? min ?????? ??????");
		}

		Random oRandom = new Random();

		String rtn = "";

		for (int i = 0; i < len; i++) {
			rtn += (oRandom.nextInt(max - min + 1) + min);
		}
		return rtn;
	}

	/**
	 * ????????? SHA-256 ?????????(HASH)
	 */
	public static String Encrypt256(String value) {
		StringBuffer sb = new StringBuffer();
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(value.getBytes());

			byte[] msgb = md.digest();

			for (int i = 0; i < msgb.length; i++) {
				sb.append(Integer.toString((msgb[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * ????????? SHA-512 ?????????(HASH)
	 *
	 * @param value
	 * @return
	 */
	public static String Sha512(String value) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-512");
			digest.reset();
			digest.update(value.getBytes("utf8"));
			return String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * ?????? ????????? ??????
	 */
	public static String getFileExt(String fileName) {
		if (StringUtils.isBlank(fileName)) {
			return null;
		}
		String[] fileArr = fileName.split("\\.");
		if (fileArr.length == 0) {
			return null;
		}

		return fileArr[fileArr.length - 1].trim().toLowerCase();
	}

	/**
	 * ????????? ?????? ??????
	 */
	public static boolean checkImageFile(String ext) {
		if (ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ????????? ???????????? ????????? ??????????????? ???????????????.
	 */
	public static boolean isMobilePhone(String phoneNumber) {
		// 010, 011, 016, 017, 018, 019
		return phoneNumber.matches("(01[016789])-(\\d{3,4})-(\\d{4})");
	}

	/**
	 * ????????? ???????????? ????????? ??????????????? ???????????????.
	 */
	public static boolean isMobilePhone(String phone1, String phone2, String phone3) {
		return isMobilePhone(phone1 + "-" + phone2 + "-" + phone3);
	}

	/**
	 * ?????? HH:mm:ss ????????? ??????
	 */
	public static String minToHMS(int min) {
		return StringUtils.leftPad(String.valueOf(min / 60), 2, "0") + ":"
				+ StringUtils.leftPad(String.valueOf(min % 60), 2, "0") + ":00";
	}

	public static String minToHM(int min) {
		return StringUtils.leftPad(String.valueOf(min / 60), 2, "0") + ":"
				+ StringUtils.leftPad(String.valueOf(min % 60), 2, "0");
	}

	/**
	 * hex ???????????? array ??? ??????
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == 0) {
			return null;
		}
		byte[] ba = new byte[hex.length() / 2];
		for (int i = 0; i < ba.length; i++) {
			ba[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return ba;
	}

	/**
	 * ???????????? base64??? ????????????
	 */
	public static String imageToBase64(InputStream is, String imageType) {

		String rtn = "";
		try {
			ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
			int len = 0;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1) {
				byteOutStream.write(buf, 0, len);
			}

			byte[] fileArray = byteOutStream.toByteArray();
			String imageStr = new String(Base64.getEncoder().encode(fileArray));
			rtn = "data:image/" + imageType + ";base64," + imageStr;
			is.close();
			byteOutStream.close();
		} catch (IOException e) {
			logger.warn("convert image error", e);
		}
		return rtn;
	}

	public static String changeKST(String date) {

		String strDate = null;
		SimpleDateFormat recvSimpleFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

		SimpleDateFormat tranSimpleFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z", Locale.KOREA);

		try {
			Date format = recvSimpleFormat.parse(date);
			strDate = tranSimpleFormat.format(format);
		} catch (ParseException e) {
			logger.error(e);
		}

		return strDate;
	}

	/*
	 * status ????????? ???????????? ??????
	 */
	public static String getStatus(String status, List<Map<String, Object>> statusList) {
		String rtn = null;
		for (Map<String, Object> item : statusList) {
			if ((status).equals(item.get("status").toString())) {
				if (item.get("content") != null) {
					rtn = item.get("content").toString();
				}
				break;
			}
		}
		if (StringUtils.isBlank(rtn)) {
			return "";
		} else {
			return rtn;
		}
	}

	/**
	 * ???????????? ????????? ??????.
	 */
	public static void toMasking(List<Map<String, Object>> list, String name, String type) {

		for (Map<String, Object> low : list) {
			if (!low.containsKey(name)) {
				continue;
			}
			low.put(name, WebPackage.toMasking(WebPackage.toString(low.get(name)), type));
		}
	}

	public static void toMasking(List<Map<String, Object>> list, Map<String, String> maskOpt) {

		for (Map<String, Object> low : list) {
			for (String key : maskOpt.keySet()) {
				String name = key;
				String type = maskOpt.get(key);

				if (low.containsKey(name)) {
					low.put(name, WebPackage.toMasking(WebPackage.toString(low.get(name)), type));
				}
			}
		}
	}

	/**
	 * VO ????????? ????????? ??????
	 */
	public static void toMasking(List<?> list)
			throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {

		for (Object obj : list) {
			Class low = obj.getClass();

			if (ReflectionUtils.findField(low, "userNm") != null) {
				// String userNm = toString(getUserNm.invoke(obj));
				BeanUtils.setProperty(obj, "userNm",
						toMasking(toString(BeanUtils.getProperty(obj, "userNm")), "userNm"));
			}
//            if (ReflectionUtils.findField(low, "userId") != null) {
//                BeanUtils.setProperty(obj, "userId", toMasking(toString(BeanUtils.getProperty(obj, "userId")), "userId"));
//            }
			if (ReflectionUtils.findField(low, "phoneNo") != null) {
				BeanUtils.setProperty(obj, "phoneNo",
						toMasking(toString(BeanUtils.getProperty(obj, "phoneNo")), "phoneNo"));
			}
			if (ReflectionUtils.findField(low, "mobileNo") != null) {
				BeanUtils.setProperty(obj, "mobileNo",
						toMasking(toString(BeanUtils.getProperty(obj, "mobileNo")), "mobileNo"));
			}
			if (ReflectionUtils.findField(low, "email") != null) {
				BeanUtils.setProperty(obj, "email", toMasking(toString(BeanUtils.getProperty(obj, "email")), "email"));
			}
			if (ReflectionUtils.findField(low, "driverNm") != null) {
				BeanUtils.setProperty(obj, "driverNm",
						toMasking(toString(BeanUtils.getProperty(obj, "driverNm")), "driverNm"));
			}
			if (ReflectionUtils.findField(low, "driverMobileNo") != null) {
				BeanUtils.setProperty(obj, "driverMobileNo",
						toMasking(toString(BeanUtils.getProperty(obj, "driverMobileNo")), "mobileNo"));
			}
			if (ReflectionUtils.findField(low, "licenseNo4") != null) {
				BeanUtils.setProperty(obj, "licenseNo4",
						toMasking(toString(BeanUtils.getProperty(obj, "licenseNo4")), "licenseNo4"));
			}
			if (ReflectionUtils.findField(low, "driverBirth") != null) {
				BeanUtils.setProperty(obj, "driverBirth",
						toMasking(toString(BeanUtils.getProperty(obj, "driverBirth")), "birth"));
			}
			if (ReflectionUtils.findField(low, "branchPhoneNo") != null) {
				BeanUtils.setProperty(obj, "branchPhoneNo",
						toMasking(toString(BeanUtils.getProperty(obj, "branchPhoneNo")), "phoneNo"));
			}
			if (ReflectionUtils.findField(low, "ceoNm") != null) {
				BeanUtils.setProperty(obj, "ceoNm", toMasking(toString(BeanUtils.getProperty(obj, "ceoNm")), "userNm"));
			}

		}
	}

	/**
	 * ????????? ?????? ??? ??? : 3?????? ????????? ????????? ?????? ?????????(*) ??????, 4?????? ????????? ????????? ?????? ?????????(*) ?????? ????????? : ?????? 3??????
	 * ?????? ?????? ?????? ?????????(*) ?????? ????????? : ?????? 4?????? ?????? ?????? ?????? ?????????(*) ?????? ????????? : ??????/????????? ????????? ??? 4?????? ???
	 * 3?????? ?????????(*) ??????
	 *
	 * @param type ('default', 'userId', 'userName', 'phoneNo', 'mobileNo', 'email',
	 *             'licenseNo4')
	 */
	public static String toMasking(String text, String type) {
		/**
		 * type default : ?????? phone : 010-1234-**** (????????? 4???) name : ???*??? (????????? ??????)
		 */

		if (StringUtils.isBlank(text)) {
			return text;
		}

		String rtn = text;

		rtn = "";

		if (StringUtils.isBlank(type)) {
			type = "default";
		}

		if (type.equals("default")) {
			for (int i = 0; i < text.length(); i++) {
				rtn += "*";
			}
		} else if ("mobileNo".equalsIgnoreCase(type)) {

			if (text == null || "".equals(text)) {
				rtn = "";
			} else if (text.contains("-")) {
				String mask = "";
				int lio = text.lastIndexOf("-");
				String lastStr = text.substring(lio + 1, text.length());
				for (int i = 0, x = lastStr.length(); i < x; i++) {
					mask += "*";
				}
				rtn = text.substring(0, lio + 1) + mask;
			} else {
				if (text.length() >= 10) {
					rtn = text.substring(0, text.length() - 4) + "****";
				} else {
					rtn = text;
				}
			}

		} else if ("phoneNo".equalsIgnoreCase(type)) {

			if (text == null || "".equals(text)) {
				rtn = "";
			} else if (text.contains("-")) {
				String mask = "";
				int lio = text.lastIndexOf("-");
				String lastStr = text.substring(lio + 1, text.length());
				for (int i = 0, x = lastStr.length(); i < x; i++) {
					mask += "*";
				}
				rtn = text.substring(0, lio + 1) + mask;
			} else {
				if (text.length() >= 10) {
					rtn = text.substring(0, text.length() - 4) + "****";
				} else {
					rtn = text;
				}
			}
		} else if ("userNm".equalsIgnoreCase(type)) {
			String mask = "";
			if (text.length() < 2) {
				rtn = text;
			} else {
				for (int i = 0; i < text.length() - 2; i++) {
					mask += "*";
				}
				rtn = text.substring(0, 2) + mask;
			}

		} else if ("driverNm".equalsIgnoreCase(type)) {
			if (text.equals("????????????")) {
				rtn = text;

			} else {
				String mask = "";
				if (text.length() > 2) {
					rtn = text;
				} else {
					for (int i = 0; i < text.length() - 2; i++) {
						mask += "*";
					}
					rtn = text.substring(0, 2) + mask;
				}
			}

		} else if ("userId".equalsIgnoreCase(type)) {
			if (text.length() > 3) {
				String idMask = "";
				for (int i = 0; i < text.length() - 3; i++) {
					idMask += "*";
				}
				rtn = text.substring(0, 3) + idMask;
			} else {
				rtn = text;
			}
		} else if ("email".equalsIgnoreCase(type)) {
			if (text.contains("@")) {
				int atIdx = text.indexOf("@");
				String emailId = text.substring(0, atIdx);
				if (emailId.length() > 4) {
//					String emailMask = "";
					String emailCompany = text.substring(atIdx, text.length());
//					for (int i = 0; i < emailId.length() - 4; i++) {
//						emailMask += "*";
//					}
					rtn = "**" + emailId.substring(2, emailId.length()) + emailCompany;
				} else {
					rtn = text;
				}
			} else {
				rtn = text;
			}

		} else if ("licenseNo4".equalsIgnoreCase(type)) {
			String mask = "";
			for (int i = 0; i < text.length(); i++) {
				mask += "*";
			}
			rtn = mask;
		} else if ("birth".equalsIgnoreCase(type)) {
			String mask = "";
			for (int i = 0; i < text.length(); i++) {
				mask += "*";
			}
			rtn = mask;
		} else if ("sabun".equalsIgnoreCase(type)) {
			// ??????:0, ??????:1
			int idx = 0;

			if (text.length() > 2) {
				String[] textSplit = text.split("");
				for (int i = idx; i < textSplit.length + idx; i++) {
					if (i % 2 == 0) {
						rtn += "*";
					} else {
						rtn += textSplit[i - idx];
					}
				}
			} else {
				rtn = text;
			}
		}

		// ?????? ????????? ?????? ?????? ???????????? ??????
//		rtn = text;

		return rtn;
	}

	public static Date localDateTime() {
		Date in = new Date();
		LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
		Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		return date;
	}

	/**
	 * ?????? ??????????????????
	 */
	public static String getCurrentDateTime() {
		String ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

		return ldt;
	}

	/**
	 * ????????? ?????? ???????????? ??????
	 */
	public static String getCurrentDate(int type) {
		String ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

		if (type == 2) {
			ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH"));
		} else if (type == 3) {
			ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		} else if (type == 4) {
			ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} else if (type == 5) {
			ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		} else if (type == 6) {
			ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
		} else if (type == 7) {
			ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
		} else if (type == 8) {
			ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		}

		return ldt;
	}

	/**
	 * ?????? ?????????
	 */
	public static String getCurrentDate() {
		String ldt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		return ldt;
	}

	/**
	 * 1????????????
	 */
	public static String getLastYear() {
		String ldt = LocalDateTime.now().minusYears(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		return ldt;
	}

	/**
	 * ?????? ??? ????????? DATE TYPE
	 */
	public static Date getLastDate() {
		LocalDateTime ldt = LocalDateTime.now().minusDays(1);
		Date date = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		return date;
	}

	/**
	 * ?????? ??? ????????? STRING TYPE
	 */
	public static String getLastDateString() {
		String ldt = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		return ldt;
	}

	/**
	 * JSON ????????? ?????? ????????? ???????????? response??? print ??????
	 */
	public static void printJSON(Object obj, HttpServletResponse response) {
		response.setHeader("Content-Type", "text/html");
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");

		ObjectMapper mapper = new ObjectMapper();
		try {
			response.getWriter().print(mapper.writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException", e);
		} catch (IOException e) {
			logger.error("IOException", e);
		}
	}

	/**
	 * Object ??? ??? ??????
	 */
	public static boolean isEmpty(Object s) {
		if (s == null) {
			return true;
		}
		if ((s instanceof String) && (((String) s).trim().length() == 0)) {
			return true;
		}
		if (s instanceof Map) {
			return ((Map<?, ?>) s).isEmpty();
		}
		if (s instanceof List) {
			return ((List<?>) s).isEmpty();
		}
		if (s instanceof Object[]) {
			return (((Object[]) s).length == 0);
		}
		return false;
	}

	/**
	 * ?????? ????????? ??????
	 *
	 * @param checkDate
	 * @return
	 */
	public static boolean validationDate(String checkDate, String pattern) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			dateFormat.setLenient(false);
			dateFormat.parse(checkDate);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static void urlFileDownload(String downloadUrl, String path) {

		try {
			ReadableByteChannel readChannel = Channels.newChannel(new URL(downloadUrl).openStream());

			try (FileOutputStream fileOS = new FileOutputStream(path)) {
				FileChannel writeChannel = fileOS.getChannel();
				writeChannel.transferFrom(readChannel, 0, Long.MAX_VALUE);
			}

		} catch (MalformedURLException e) {
			logger.error("MalformedURLException", e);
		} catch (FileNotFoundException e) {
			logger.error("FileNotFoundException", e);
		} catch (IOException e) {
			logger.error("IOException", e);

		}
	}

	/**
	 * url ????????? ??????????????????
	 *
	 * @param downloadUrl
	 * @param response
	 */
	public static void urlFileDownload(String downloadUrl, HttpServletResponse response) {
		BufferedInputStream in = null;
		OutputStream os = null;
		URLConnection conn = null;
		URL url = null;
//		String loginPassword = "rubens" + ":" + "rubens";
		String loginPassword = "rubens:rubens";
//		String encoded = new sun.misc.BASE64Encoder().encode (loginPassword.getBytes());
		String encoding = new String(Base64.getEncoder().encode(loginPassword.getBytes(StandardCharsets.UTF_8)));

//		String encoded = "";
		byte dataBuffer[] = new byte[1024];
		int bytesRead;
		try {
			os = response.getOutputStream();
			url = new URL(downloadUrl);
			conn = url.openConnection();
			conn.setRequestProperty("Authorization", "Basic " + encoding);
			in = new BufferedInputStream(conn.getInputStream());
			response.setHeader("Content-disposition",
					"attachment; filename=" + downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1));
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				os.write(dataBuffer, 0, bytesRead);
			}
			os.flush();
		} catch (IOException e) {
			logger.error("IOException : {}", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("IOException : {}", e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("IOException : {}", e);
				}
			}
		}
	}

	/**
	 * url ????????? ??????????????????
	 *
	 * @param downloadUrl
	 * @param response
	 */
	public static void urlFileDownload(String downloadUrl, String fileName, HttpServletResponse response,
			HttpServletRequest request) throws Exception {
		BufferedInputStream in = null;
		OutputStream os = null;
		URLConnection conn = null;
		URL url = null;

		String ori_fileName = getDisposition(fileName, getBrowser(request));

//		String encoded = "";
		byte dataBuffer[] = new byte[1024];
		int bytesRead;
		try {
			os = response.getOutputStream();
			url = new URL(downloadUrl);
			conn = url.openConnection();
			in = new BufferedInputStream(conn.getInputStream());
			response.setHeader("Content-disposition", "attachment; filename=" + ori_fileName);
			while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
				os.write(dataBuffer, 0, bytesRead);
			}
			os.flush();
		} catch (IOException e) {
			logger.error("IOException : {}", e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					logger.error("IOException : {}", e);
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("IOException : {}", e);
				}
			}
		}
	}

	private static String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		} else if (header.indexOf("Trident/7.0") > -1) {
			// IE 11 ?????? //IE ?????? ??? ?????? >> Trident/6.0(IE 10) , Trident/5.0(IE 9) ,
			// Trident/4.0(IE 8)
			return "MSIE";
		}

		return "Firefox";
	}

	private static String getDisposition(String filename, String browser) throws Exception {
		String encodedFilename = null;
		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			throw new RuntimeException("Not supported browser");
		}
		return encodedFilename;
	}

//	public static String convertTimezoneToString(String timezone){
//		String dateTime = LocalDateTime.parse(timezone).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
//		return dateTime;
//	}

	public static String convertTimezoneToString(String timezone) {
		ZonedDateTime zonedDateTime = ZonedDateTime.parse(timezone.replace("Z", ""), DateTimeFormatter.ISO_DATE_TIME);
		ZonedDateTime zoneId = zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
		LocalDateTime localZone = zoneId.toLocalDateTime();

		return localZone.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
	}

	public static String makeOTPSecurityKeyByAdminRFID(String rfid) {
		if (rfid.length() == 0) {
			return "N/A";
		}

		final int MAX_RFID_LEN = 16;

		StringBuilder base = new StringBuilder();

		if (rfid.length() > MAX_RFID_LEN) {
			rfid = rfid.substring(0, MAX_RFID_LEN); // ????????? 16?????????
		}

		// ????????? RFID??? ????????? ?????????????????? ????????? ??????.

		base.append(rfid);

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(base.toString().getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}

			return sb.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "N/A";
	}

	/**
	 * @return 16?????? 8?????? ??????
	 */
	public static String createRfid() {
		String retVal = "";

		Random rnd = new Random();

		for (int i = 0; i < 8; i++) {
			retVal += Integer.toHexString(rnd.nextInt(16)).toUpperCase();
		}

		return retVal;
	}

	public static String getOtpNum(String rfid) {
		String retVal = "";
		String key = "";

		key = makeOTPSecurityKeyByAdminRFID(rfid);

		try {
			retVal = generateTOTP(key, true, 6, 60);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}

	/**
	 * ?????? ????????? ???????????? TOTP ?????? ????????????.
	 *
	 * @param secret OTP??? ???????????? ?????? ??????
	 * @param digits ????????? OTP?????? ?????????. ????????? 6(Default), 7, 8 ??? ???????????? ??????.
	 * @param period TOTP ????????????. CS ???????????? ???????????? ?????? ??????????????? ?????? 30??? ????????????. 0??? ????????? ?????????.
	 * @return ????????? TOTP ???.
	 * @throws Exception
	 */
	public static String generateTOTP(String secret, Boolean isKeyAsHexString, int digits, int period)
			throws Exception {
		if (period == 0)
			throw new Exception("Invalid Period.");

		long now_sec = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
		long mvf = now_sec / period;

		String strTemp = "";
		byte[] counter = new byte[8];
		for (int i = 0; i < 8; i++) {
			counter[i] = (byte) ((mvf >> ((8 - i - 1) * 8)) & 0xFF);
		}

		byte[] key_byte = null;
		if (isKeyAsHexString == true) {
			// SHA1 Hash????????? ????????????
			key_byte = hexStringToByteArray(secret);
		} else {
			// RAW ???????????????
			key_byte = secret.getBytes();
		}

		byte[] otp_seed = getHMACwithSHA1(key_byte, counter);
		if (otp_seed == null)
			throw new Exception("HMAC Failed.");

		final int offset = otp_seed[otp_seed.length - 1] & 0xf;
		final int binary = ((otp_seed[offset] & 0x7f) << 24) | ((otp_seed[offset + 1] & 0xff) << 16)
				| ((otp_seed[offset + 2] & 0xff) << 8) | (otp_seed[offset + 3] & 0xff);

		// To prevent digit limits
		String result = String.format("%08d", binary % 100000000);
		if (digits == 6)
			result = String.format("%06d", binary % 1000000);
		else if (digits == 7)
			result = String.format("%07d", binary % 10000000);

		String ret = convertAcceptableKeypadPassword(result);

		return ret;
	}

	/**
	 * ????????? ????????? 0~9 ?????? ?????? ?????? 1~5 ????????? ????????? ?????????.
	 *
	 * @param password 0~9????????? 6?????? ????????? ????????????
	 * @return 1~5????????? ????????? ????????????
	 */
	public static String convertAcceptableKeypadPassword(String password) {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < password.length(); i++) {
			int digit = Integer.parseInt("" + password.charAt(i));
			if (digit == 1 || digit == 6)
				sb.append(1);
			else if (digit == 2 || digit == 7)
				sb.append(2);
			else if (digit == 3 || digit == 8)
				sb.append(3);
			else if (digit == 4 || digit == 9)
				sb.append(4);
			else if (digit == 5 || digit == 0)
				sb.append(5);
		}
		return sb.toString();
	}

	private static byte[] getHMACwithSHA1(byte[] key, byte[] indata) {
		byte[] ret = null;

		try {
			Mac hmac = Mac.getInstance("HmacSHA1");
			SecretKeySpec keySpec = new SecretKeySpec(key, "RAW");
			hmac.init(keySpec);
			ret = hmac.doFinal(indata);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	/**
	 * List<Map> to List<Object>
	 *
	 * @param type
	 * @param list
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static <T> List<T> copyMapList(Class<T> type, List<Map<String, Object>> list)
			throws IllegalAccessException, InvocationTargetException, InstantiationException {
		List<T> rtn = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			T item = type.newInstance();
			BeanUtils.copyProperties(item, map);
			rtn.add(item);
		}
		return rtn;
	}

	public static void setNoCacheResponse(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
	}

	/**
	 * ??????????????? ??????
	 *
	 * @param url    ????????? URL
	 * @param object ????????????????????? ????????? ??????
	 * @return
	 */
	public static String toQueryString(String url, Object object) {

		// Object --> map
		ObjectMapper objectMapper = new ObjectMapper();

		// ???????????? ?????? ?????? ??????
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		// ????????? ?????? application.yml(??????)
		objectMapper.setTimeZone(TimeZone.getDefault());

		Map<String, Object> map = objectMapper.convertValue(object, Map.class);

		StringBuilder qs = new StringBuilder();
		qs.append(url + "?");

		for (String key : map.keySet()) {

			if (map.get(key) == null) {
				continue;
			}
			// key=value&
			qs.append(key);
			qs.append("=");
			qs.append(map.get(key));
			qs.append("&");
		}

		String result = "";

		// delete last '&'
		if (qs.length() != 0) {
			qs.deleteCharAt(qs.length() - 1);

			result = qs.toString().replaceAll(" ", "%20");
		}

		return result;
	}

	/**
	 * URL QUERY??? ????????? ????????????.
	 *
	 * @param urlQuery
	 * @return
	 */
	public static Map<String, String> urlQueryParse(String urlQuery) {
		HashMap<String, String> parameters = new HashMap<>();

		try {
			List<NameValuePair> pairs = URLEncodedUtils.parse(urlQuery, Charset.forName("UTF-8"));
			for (NameValuePair nameValuePair : pairs) {
				parameters.put(nameValuePair.getName(), nameValuePair.getValue());
			}
		} catch (Exception e) {
			logger.error("urlQueryParse ERROR", e);
			return null;
		}
		return parameters;
	}

	/*
	 * yyyymmdd ?????? ?????? parse
	 */
	public static String yyyymmddParseDate(String yyyymmdd, String format) {
		String retVal = "";
		switch (format) {
		case "yyyy-mm-dd":
			retVal = yyyymmdd.substring(0, 4);
			retVal += "-";
			retVal += yyyymmdd.substring(4, 6);
			retVal += "-";
			retVal += yyyymmdd.substring(6, 8);
			break;
		default:
			retVal = yyyymmdd;
			break;
		}
		return retVal;
	}

	/**
	 * ???????????? ?????? ??????
	 *
	 * @return 1:????????? ~ 7:?????????
	 */
	public static int getDayOfWeek(Date evtDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(evtDate);

		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * str1, str2 ????????? splitStr ????????? ?????? ?????????.
	 *
	 * @param str1
	 * @param str2
	 * @param splitStr
	 * @return
	 */
	public static String sumStr(String str1, String str2, String splitStr) {
		String rtn = "";
		if (StringUtils.isNotBlank(str1)) {
			rtn = str1 + splitStr;
		}
		if (StringUtils.isNotBlank(str2)) {
			rtn += str2;
		}
		return rtn;
	}

	/*
	 * ????????? ?????? ????????????
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new HashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	/**
	 * @param year
	 * @param month
	 * @param day
	 * @return yyyy-mm-dd
	 */
	public static String getBirthDate(int year, int month, int day) {
		return year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
	}

	/**
	 *
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static long getDayDiffrence(String startDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar sDate = Calendar.getInstance();
		Calendar eDate = Calendar.getInstance();
		long diffDays = -1;
		try {
			sDate.setTime(sdf.parse(startDate));
			eDate.setTime(sdf.parse(endDate));

			long diffSec = (eDate.getTimeInMillis() - sDate.getTimeInMillis()) / 1000;
			diffDays = diffSec / (24 * 60 * 60);

		} catch (ParseException e) {
		}
		return diffDays;
	}

	/*
	 * UTC ?????? ????????? +????????? ????????? ????????????
	 */
	public static int getUtcTimeZoneDiffMinute() {
		return ZonedDateTime.now().getOffset().getTotalSeconds() / 60;
	}

	/*
	 * ????????? ????????? ???????????? UTC+0 ?????? ?????????
	 */
	public static Date getTimeToUtcTime(Date dateTime) {
		return WebPackage.addMinite(dateTime, -WebPackage.getUtcTimeZoneDiffMinute());
	}

	/*
	 * ????????? ????????? ???????????? UTC+0 ?????? ?????????
	 */
	public static Date getServerTimeUtcZero() {
		return WebPackage.addMinite(new Date(), -WebPackage.getUtcTimeZoneDiffMinute());
	}

	/*
	 * ????????? ????????? ???????????? UTC+0 ?????? ?????????
	 */
	public static Date getCustomerTimeZone(int timeZone) {
		return WebPackage.addMinite(WebPackage.getServerTimeUtcZero(), timeZone);
	}

	/*
	 * ?????????????????? date??? ????????? format??? String ???????????? ??????
	 */
	public static String dateParseToString(String date, String dateType, String parseType) {
		DateFormat sdFormat = new SimpleDateFormat(dateType);
		try {
			return new SimpleDateFormat(parseType).format(sdFormat.parse(date));
		} catch (ParseException e) {
			logger.error("parseErr {}", e);
			return null;
		}
	}

	/*
	 * ???????????? ???????????? ???????????? UTCTIME??? ????????????
	 */
	public static String getDeviceUtcTime(String date, int timeZone) {
		DateFormat sdFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date tempDate;
		String getDate = "";
		try {
			tempDate = sdFormat.parse(date);
			tempDate = WebPackage.addMinite(tempDate, -timeZone);
			getDate = new SimpleDateFormat("yyyyMMddHHmmss").format(tempDate);
		} catch (ParseException e) {
			logger.error("parseErr {}", e);
		}
		return getDate;
	}

	/**
	 * ????????? MD5 Hash
	 *
	 * @param str
	 * @return
	 */
	public static String hashMd5(String str) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] hashInBytes = md.digest(str.getBytes(StandardCharsets.UTF_8));

			StringBuilder sb = new StringBuilder();
			for (byte b : hashInBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			logger.error("md5 hash error", e);
			return "";
		}

	}

	/*
	 * OTP ??? ?????? ?????? 6??????????????? 8????????? ??????
	 */
	public static int getKeypadPsswordWithCRC(String password) {
		// Seed : 387299, Operator Order : * + * + * +

		if (password.length() > 6)
			password = password.substring(0, 5); // ?????? 6??????????????? ????????????.

		int t = password.length();
		int[] seed = { 3, 8, 7, 2, 9, 9 };
		boolean operator = true;
		for (int i = 0; i < password.length(); i++) {
			int digit = Integer.parseInt("" + password.charAt(i));
			t += operator == true ? seed[i] * digit : seed[i] + digit;
			operator = !operator;
		}

		int crc1 = (((t % 100) / 10) % 5) + 1;
		int crc2 = (t % 5) + 1;

		return WebPackage.toInt(String.format("%s%d%d", password, crc1, crc2));
	}

}
