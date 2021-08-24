package com.baseball.records.utils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 맵의 키를 카멜표기법으로 변경
 *
 * @author jhd
 * @version 1.0 2017. 11. 30.
 */
public class CamelCaseMap<K, V> extends HashMap<Object, Object> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6928811783622655355L;

	@Override
	public Object put(Object key, Object value) {

		String nkey = WebPackage.toString(key);

		if ((nkey).contains("_")) {
			String newKey = ((nkey).matches("[a-z].*") ? nkey : (nkey).toLowerCase());
			Pattern pattern = Pattern.compile("_.{1}");
			Matcher matcher = pattern.matcher(newKey);
			while (matcher.find()) {
				String str = matcher.group();
				newKey = newKey.replace(str, str.toUpperCase());
			}
			return super.put(newKey.replaceAll("_", ""), value);
		} else {
			return super.put(nkey, value);
		}

	}

	/**
	 * 문자로 반환
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return WebPackage.toString(this.get(key));
	}

	/**
	 * 문자로 반환
	 *
	 * @param key
	 * @return
	 */
	public String getS(String key) {
		return getString(key);
	}

	/**
	 * int 반환
	 *
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		return getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		return WebPackage.toInt(this.get(key), defaultValue);
	}

	/**
	 * int 반환
	 *
	 * @param key
	 * @return
	 */
	public int getN(String key) {
		return getInt(key, 0);
	}

	public int getN(String key, int defaultValue) {
		return getInt(key, defaultValue);
	}

	/**
	 * 날짜 반환
	 *
	 * @param key
	 * @return
	 */
	public Date getDate(String key) {
		return WebPackage.toDate(this.get(key));
	}

	public List<CamelCaseMap<String, Object>> getListMap(String key) throws IOException {
		return WebPackage.valueToListCMap(this.get(key));
	}

}
