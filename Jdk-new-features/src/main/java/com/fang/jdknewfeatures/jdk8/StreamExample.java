package com.fang.jdknewfeatures.jdk8;

/**
 * @program: Exercise
 * @Date: 2018/11/15 16:05
 * @Author: chengling.bj
 * @Description:
 */

import com.fang.jdknewfeatures.util.bean.People;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 在 Java 8 中, 集合接口有两个方法来生成流：
 *    stream() − 为集合创建串行流。
 *    parallelStream() − 为集合创建并行流。
 */
public class StreamExample {
  public static void main(String[] args) {
  List<People> peopleList = People.getPeopleList();

    Map<String, People> listAdMap =  peopleList != null ? peopleList.stream().collect(Collectors.toMap(People::getName, Function.identity(), (k1, k2) -> k2, HashMap::new)) : new HashMap<>();


    List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
    List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());

  }
}
