package com.fang.jdknewfeatures.jdk8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @program: Exercise
 * @Date: 2018/11/13 16:19
 * @Author: chengling.bj
 * @Description:
 */

/**
 * Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）
 * <p>
 * lambda 表达式的语法格式如下：
 * (parameters) -> expression
 * 或
 * (parameters) ->{ statements; }
 * <p>
 * Lambda表达式的特征
 * 类型声明（可选）：可以不需要声明参数类型，编译器会识别参数值。
 * 参数圆括号（可选）：在单个参数时可以不使用括号，多个参数时必须使用。
 * 大括号和return关键字（可选）：如果只有一个表达式，则可以省略大括号和return关键字，编译器会自动的返回值；
 * 相对的，在使用大括号的情况下，则必须指明返回值。
 */
public class LambdaExample {
  private static List<People> peopleList = new ArrayList<People>();

  static {
    peopleList.add(new People("a", 17));
    peopleList.add(new People("b", 16));
    peopleList.add(new People("c", 19));
    peopleList.add(new People("d", 15));
  }

  public static void fun1() {
    //第一种，传统匿名Compartor接口排序
    Collections.sort(peopleList, new Comparator<People>() {
      @Override
      public int compare(People o1, People o2) {
        return o1.getAge().compareTo(o2.getAge());
      }
    });

    System.out.println("" +  peopleList);

    //1.声明式,不使用大括号，只可以写单条语句
    Collections.sort(peopleList, (a, b) ->a.getAge().compareTo(b.getAge()));



    //2.不声明式，使用大括号，可以写多条语句
    Collections.sort(peopleList, (a, b) -> {
      System.out.print("——————————————");
      return a.getAge().compareTo(b.getAge());
    });

    System.out.println( peopleList);
  }

  public static void fun2() {
//      //第三种，使用Lambda表达式调用类的静态方法
//      Collections.sort(peopleList,(a,b)->People.sortByName(a,b));
//      System.out.println("Lambda表达式调用静态方法："+peopleList);
//
//      //第四种，使用Lambda表达式调用类的实例方法
//      Collections.sort(peopleList,(a,b)->new People().sortByAge(a,b));
//      System.out.println("Lambda表达式调用实例方法:"+peopleList);
  }

  public void test3(){
    List<Integer> nums = Arrays.asList(9,5,8,4,7,3,6,2,1);
    nums.forEach(t-> System.out.println(t));
  }

  public static void main(String[] args) {
    System.out.println("排序前：" + peopleList);

    fun1();

  }

  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  static class People {
    private String name;
    private Integer age;
  }


}
