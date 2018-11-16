package com.fang.jdknewfeatures.jdk8;

/**
 * @program: Exercise
 * @Date: 2018/11/14 14:12
 * @Author: chengling.bj
 * @Description:
 */

import com.fang.jdknewfeatures.util.bean.People;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 不过方法引用的唯一用途是支持Lambda的简写
 * 方法引用的种类
 * •类静态方法引用:             Class::staticMethodName
 * •某个对象的方法引用:         instance::instanceMethodName
 * •特定类的任意对象的方法引用： Class::method
 * •构造方法引用：              Class::new
 */
public class MethodExample {


  private static List<People> peopleList = People.getPeopleList();

  public static void main(String[] args) {
    //第一种，引用类的静态方法

    Collections.sort(peopleList, People::sortByName);
    System.out.println("引用类的静态方法：" + peopleList);

    //第二种，引用类的实例方法
    Collections.sort(peopleList, new People()::sortByAge);
    System.out.println("引用类的实例方法：" + peopleList);

    //第三种，特定类的方法调用()
    Integer[] a = new Integer[]{3, 1, 2, 4, 6, 5};
    Arrays.sort(a, Integer::compare);
    System.out.println("特定类的方法引用：" + Arrays.toString(a));


//    //第四种，引用类的构造器
    Map<String, People> listAdMap =  peopleList != null ? peopleList.stream().collect(Collectors.toMap(People::getName, Function.identity(), (k1, k2) -> k2, HashMap::new)) : new HashMap<>();
//    System.out.println("引用类的构造器:" + car);
  }


}
