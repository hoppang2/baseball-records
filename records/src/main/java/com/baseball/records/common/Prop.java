package com.baseball.records.common;

import java.io.InputStream;
import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.FileSystem;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prop {
	protected static final Logger logger = LoggerFactory.getLogger(Prop.class);

	public static Configuration conf = null;

	public void setConf() {
		if (Prop.conf != null) {
			return;
		}

		// 순서
		// 업체-환경-추가
		String activePropertyPath = "env";
		String activePropertySubPath = null;

		String sysruntime = System.getProperty("sysruntime");
		String sysruntimeid = System.getProperty("sysruntimeid");
		String sysclient = System.getProperty("sysclient");
		String fixproperty = System.getProperty("fixproperty");

		String wasenv = System.getProperty("wasenv");
		logger.info("was env : {}", wasenv);

		if (StringUtils.isBlank(sysruntime)) {
			sysruntime = "local";
		}

		logger.info("##### ACTIVE PROFILE RUNTIME [{}] #####", sysruntime);
		logger.info("##### ACTIVE PROFILE RUNTIME_ID [{}] #####", sysruntimeid);
		logger.info("##### ACTIVE PROFILE CLIENT  [{}] #####", sysclient);
		logger.info("##### ACTIVE PROFILE FIX  [{}] #####", fixproperty);

		// 고정되는 프로퍼티 사용
		activePropertyPath = "classpath:/application.properties";

//		if (StringUtils.isNotBlank(fixproperty)) {
//			// 고정되는 프로퍼티 사용
//			activePropertyPath = "classpath:/properties/env-" + fixproperty + ".properties";
//		} else {
//
//			if (StringUtils.isNotBlank(sysclient)) {
//				activePropertyPath += "-" + sysclient;
//			}
//			activePropertyPath += "-" + sysruntime;
//
//			if (StringUtils.isNotBlank(sysruntimeid)) {
//				activePropertySubPath = activePropertyPath + "-" + sysruntimeid;
//			}
//
//			activePropertyPath = "classpath:/properties/" + activePropertyPath + ".properties";
//			activePropertySubPath = "classpath:/properties/" + activePropertySubPath + ".properties";
//		}

		logger.info("##### PROFILE PATH [{}] #####", activePropertyPath);
		logger.info("##### PROFILE ADD PATH [{}] #####", activePropertySubPath);

		try {
			FileSystem fileSystem = FileSystem.getDefaultFileSystem();

			CompositeConfiguration config = new CompositeConfiguration();

			if (hasResource(fileSystem, activePropertyPath)) {
				config.addConfiguration(new PropertiesConfiguration(activePropertyPath)); // 환경 프로퍼티
			} else {
				logger.error("activePropertyPath 없음");
			}

			if (activePropertySubPath != null) {
				if (hasResource(fileSystem, activePropertySubPath)) {
					config.addConfiguration(new PropertiesConfiguration(activePropertySubPath)); // 추가 환경 프로퍼티
				} else {
					logger.error("activePropertySubPath 없음");
				}
			}

			Prop.conf = config;

		} catch (ConfigurationException e) {
			logger.error("ConfigurationException", e);
		}
	}

	public static Configuration getConf() {
		if (Prop.conf == null) {
			Prop.getInstance().setConf();
		}
		return Prop.conf;
	}

	private Prop() {

	}

	private static class Singleton {
		private static final Prop instance = new Prop();
	}

	public static Prop getInstance() {
		return Singleton.instance;
	}

	/**
	 *
	 *
	 * @param fileSystem
	 * @param fileName
	 * @return
	 */
	private static boolean hasResource(FileSystem fileSystem, String fileName) {
		try {

			// URL url = ConfigurationUtils.locate(fileSystem, null, fileName);
			URL url = ConfigurationUtils.locate(fileName);
			if (url == null) {
				return false;
			}
			try (InputStream in = fileSystem.getInputStream(url)) {
				return true;
			} catch (Exception e) {
				return false;
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return false;
	}

}
