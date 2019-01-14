package com.fang.jdknewfeatures.jdk8.myexec.stream;

import com.fang.jdknewfeatures.util.bean.People;
import com.fang.jdknewfeatures.util.bean.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @program: Exercise
 * @Date: 2019/1/9 14:51
 * @Author: chengling.bj
 * @Description:
 */
public class TestStreamAPI {

  static List<Person> list = Person.createShortList();
  List<Integer> intList = Arrays.asList(1,2,3,4,4,3,2,1,4,5,6,7);
  List<String> strList = Arrays.asList("aaaa","bb","cc");


  /**
   * filter(Predicate p p) )  接收 Lambda ， 从流中排除某些元素。
   * distinct()  筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素
   * limit(long maxSize)截断流，使其元素不超过给定数量。
   * skip(long n) 跳过元素，返回一个扔掉了前 n 个元素的流。若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补
   */
  @Test
  public void testFilter(){
    intList.stream().filter((e) -> e > 2).forEach(System.out::println);
  }

  @Test
  public void testDistinct(){
    intList.stream().distinct().forEach(System.out::println);
  }

  @Test
  public void testlimit(){
    intList
     .stream()
     .filter((e) -> {
       return e > 2;
     })
     .limit(3).forEach(System.out::println);
  }

  /**
   *
   */

  List<People> peopleList = People.getPeopleList();

  @Test
  public void testmap(){
    peopleList.stream()
//     .map((p)->p.getName())
     .map(People::getName)
     .forEach(System.out::println);
  }

  @Test
  public void testflatmap(){


    Stream<Stream<Character>> sm = strList.stream()
     .map(TestStreamAPI::chHandler);
    sm.forEach(str->str.forEach(System.out::println));

    System.out.println("--------------------------------------");


    strList.stream().flatMap(TestStreamAPI::chHandler).forEach(System.out::println);
  }

  public static Stream<Character> chHandler(String str){
    List<Character> list = new ArrayList<>();

    for (Character ch : str.toCharArray()) {
      list.add(ch);
    }
    return list.stream();
  }

  /**
   * 排序
   * sorted()
   * sorted(Comparator
   */

  @Test
  public void testSort(){
    strList.stream().sorted().forEach(System.out::println);

    System.out.println("--------------------------");

    peopleList.stream().sorted((p1, p2) -> {
      if(p1.getAge() == p2.getAge()) {
        return p1.getName().compareTo(p2.getName()) ;
      }
      return p1.getAge() - p2.getAge();
    }).forEach(System.out::println);
  }



  /**
   * 查找与匹配
   * allMatch(Predicate p)
   * anyMatch( (Predicate p) )
   * noneMatch(Predicate p)
   * findFirst()
   * findAny()
   */

  @Test
  public void testMatch(){
    boolean all = list.stream().allMatch( (p) ->p.getAge() < 40 );
    boolean any = list.stream().anyMatch( (p) ->p.getAge() < 40 );
    boolean none = list.stream().noneMatch( (p) ->p.getAge() < 40 );

    Optional<Person> first =  list.stream().sorted().findFirst();
    Optional<Person> findany =  list.stream().sorted().findAny();

    System.out.println("all = " + all);
    System.out.println("any = " + any);
    System.out.println("none = " + none);
    System.out.println("first = " + first.get());
    System.out.println("findany = " + findany.get());

  }





/*
  // list 转 map
  Map<String, People> listAdMap =  peopleList != null ? peopleList.stream().collect(Collectors.toMap(People::getName, Function.identity(), (key1, key2)->key2, HashMap::new)) : new HashMap<>();

  // list 去重
  List<String> collect = peopleList.stream().map(t -> t.getName()).distinct().collect(Collectors.toList());

  // list 相同的getName 分为一组
  List<List<People>> resList = collect.stream().map(t -> peopleList.stream().filter(c -> c.getName().equals(t)).collect(Collectors.toList())).collect(Collectors.toList());

  // 里层 list 最长长度
  int len = resList.stream().mapToInt(t -> t.size()).max().getAsInt();
*/

}
