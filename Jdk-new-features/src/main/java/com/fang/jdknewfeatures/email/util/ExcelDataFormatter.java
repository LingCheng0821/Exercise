package com.fang.jdknewfeatures.email.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: bi-cloud
 * @Date: 2018/11/20 17:39
 * @Author: chengling.bj
 * @Description: 显示转换
 */
public class ExcelDataFormatter {
  private Map<String, Map<String, String>> formatter = new HashMap<String, Map<String, String>>();

  public void set(String key, Map<String, String> map) {
    formatter.put(key, map);
  }

  public Map<String, String> get(String key) {
    return formatter.get(key);
  }
}
