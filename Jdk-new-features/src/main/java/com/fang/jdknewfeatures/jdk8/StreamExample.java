package com.fang.jdknewfeatures.jdk8;

/**
 * @program: Exercise
 * @Date: 2018/11/15 16:05
 * @Author: chengling.bj
 * @Description:
 */

import com.fang.jdknewfeatures.util.bean.People;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * 在 Java 8 中, 集合接口有两个方法来生成流：
 *    stream() − 为集合创建串行流。
 *    parallelStream() − 为集合创建并行流。
 */
public class StreamExample {
  private static final List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
  private static final  List<Integer> list = Arrays.asList(9,8,7,6,5,4,3,2,1);
  public static void test1(){
    // 去掉 空字符串
    List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(toList());

    List<Integer> collect = list.stream()
     .filter(t -> t % 2 ==0 )  //过滤
     .sorted()                //排序
     .map( n -> n * n)        //平方
     .collect(toList());

    filtered.forEach(System.out::println);
  }

  public static void test2(){
    List<Integer> collect = list.stream()
     .sorted((a,b) ->  a-b)
     .map( n -> n * n)
     .collect(toList());
    collect.forEach(System.out::println);
  }

  public static void test(){
    List<People> peopleList = People.getPeopleList();

    // 重复key的情况下 简单的使用后者覆盖前者的

    // list 转 map
    Map<String, People> listAdMap =  peopleList != null ? peopleList.stream().collect(Collectors.toMap(People::getName, Function.identity(), (key1,key2)->key2, HashMap::new)) : new HashMap<>();

    // list 去重
    List<String> collect = peopleList.stream().map(t -> t.getName()).distinct().collect(Collectors.toList());

    // list 相同的getName 分为一组
    List<List<People>> resList = collect.stream().map(t -> peopleList.stream().filter(c -> c.getName().equals(t)).collect(Collectors.toList())).collect(Collectors.toList());

    // 里层 list 最长长度
    int len = resList.stream().mapToInt(t -> t.size()).max().getAsInt();
  }

  public static void main(String[] args) {
    test2();
  }
}
