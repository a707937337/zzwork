package com.zz91.util.lang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.zz91.util.cache.MemcachedUtils;

public class SensitiveUtils {
	/**
	 * 敏感词过滤工具 需要 本地服务器有敏感词词库 "/usr/tools/sensitive"
	 * 
	 * @param filterText
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static String getSensitiveFilter(String filterText) throws Exception {
		List<String> listWord = (List<String>) MemcachedUtils.getInstance().getClient().get("zz91_sensitive");
		if (listWord == null || listWord.size() <= 0) {
			updateCache();
			listWord = (List<String>) MemcachedUtils.getInstance().getClient().get("zz91_sensitive");
		}
		Matcher m = null;

		for (int i = 0; i < listWord.size(); i++) {
			Pattern p = Pattern.compile(listWord.get(i),
					Pattern.CASE_INSENSITIVE);
			StringBuffer sb = new StringBuffer();
			m = p.matcher(filterText);
			while (m.find()) {
				m.appendReplacement(sb, "*");
			}
			m.appendTail(sb);
			filterText = sb.toString();
		}
		return filterText;
	}

	public static void updateCache() throws Exception {
		List<String> listWord = new ArrayList<String>();
		FileReader reader = new FileReader("/usr/tools/sensitive");
		BufferedReader br = new BufferedReader(reader);
		String s = null;
		while ((s = br.readLine()) != null) {
			listWord.add(s.trim());
		}
		br.close();
		reader.close();
		MemcachedUtils.getInstance().getClient().set("zz91_sensitive",3600 * 12, listWord);
	}

	public static void main(String[] args) throws Exception {
		String filterText = " 中国人民站起来了124ssssaa中国人民";
		MemcachedUtils.getInstance().init("web.properties");
		System.out.print(SensitiveUtils.getSensitiveFilter(filterText));
	}
}
