package com.svili.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;

/**
 * Csv文件导出Helper.</br>
 * 
 * @author lishiwei
 * @data 2017年6月15日
 *
 */
public class CsvExportHelper {

	/**
	 * map数据导出
	 * 
	 * @param writePath
	 *            文件目录
	 * @param fileName
	 *            文件名
	 * @param datas
	 *            数据
	 * @param mapper
	 *            key-字段编码,value-表头
	 */
	public static void exportMap(String writePath, String fileName, List<Map<String, Object>> datas,
			LinkedHashMap<String, String> mapper) throws Exception {
		File file = new File(writePath, fileName);

		exportMap(new FileOutputStream(file), datas, mapper);
	}

	/**
	 * map数据导出
	 * 
	 * @param out
	 *            IO流
	 * @param datas
	 *            数据
	 * @param mapper
	 *            key-字段编码,value-表头
	 */
	public static void exportMap(OutputStream out, List<Map<String, Object>> datas,
			LinkedHashMap<String, String> mapper) throws Exception {
		// 给CSV文件加上BOM标识,防止中文会乱码
		out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
		Writer writer = new OutputStreamWriter(out, "utf-8");
		// mapWriter
		ICsvMapWriter csvWriter = new CsvMapWriter(writer, CsvPreference.STANDARD_PREFERENCE);

		List<String> header = new ArrayList<>();
		List<String> nameMapping = new ArrayList<>();
		for (Entry<String, String> entry : mapper.entrySet()) {
			// 字段编码
			nameMapping.add(entry.getKey());
			// 表头
			header.add(entry.getValue());
		}

		csvWriter.writeHeader(header.toArray(new String[nameMapping.size()]));
		String[] nameMappingArray = nameMapping.toArray(new String[nameMapping.size()]);
		for (Map<String, Object> source : datas) {
			csvWriter.write(source, nameMappingArray);
		}

		csvWriter.close();
	}

	/**
	 * javabean数据导出
	 * 
	 * @param writePath
	 *            文件目录
	 * @param fileName
	 *            文件名
	 * @param datas
	 *            数据
	 * @param mapper
	 *            key-字段编码,value-表头
	 */
	public static void exportBean(String writePath, String fileName, List<?> datas,
			LinkedHashMap<String, String> mapper) throws Exception {
		File file = new File(writePath, fileName);

		exportBean(new FileOutputStream(file), datas, mapper);
	}

	/**
	 * javabean数据导出
	 * 
	 * @param out
	 *            IO流
	 * @param datas
	 *            数据
	 * @param mapper
	 *            key-字段编码,value-表头
	 */
	public static void exportBean(OutputStream out, List<?> datas, LinkedHashMap<String, String> mapper)
			throws Exception {
		// 给CSV文件加上BOM标识,防止中文会乱码
		out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
		Writer writer = new OutputStreamWriter(out, "utf-8");
		// beanWriter
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, CsvPreference.STANDARD_PREFERENCE);

		List<String> header = new ArrayList<>();
		List<String> nameMapping = new ArrayList<>();
		for (Entry<String, String> entry : mapper.entrySet()) {
			// 字段编码
			nameMapping.add(entry.getKey());
			// 表头
			header.add(entry.getValue());
		}

		csvWriter.writeHeader(header.toArray(new String[nameMapping.size()]));
		String[] nameMappingArray = nameMapping.toArray(new String[nameMapping.size()]);
		for (Object source : datas) {
			csvWriter.write(source, nameMappingArray);
		}

		csvWriter.close();
	}
}
