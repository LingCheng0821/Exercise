package com.fang.jdknewfeatures.jdk8.myexec.methodref;

import com.fang.jdknewfeatures.util.bean.People;
import com.fang.jdknewfeatures.util.bean.Person;
import org.junit.Test;

import java.util.Comparator;
import java.util.function.*;

/**
 * @program: Exercise
 * @Date: 2019/1/8 10:07
 * @Author: chengling.bj
 * @Description:
 */
public class TestMethodRef {

  // 对象 :: 实例方法
  @Test
  public void test1(){
    Person p = new Person();
    Supplier<String> s = p::getAddress;
    Supplier<String> s2 = ()->p.getAddress();
    s.get();
  }

  @Test
  public void test2(){
    String a = "ab";
    Predicate<String> p1 = (x)->x.equals(a);
    Predicate<String> p2 = a::equals;
  }


  // 类 :: 静态方法
  @Test
  public void test3(){
    Consumer<String> con = (x)-> System.out.println(x);
    Consumer<String> con1 = System.out::println;
    con.accept("123");
    con1.accept("345");
  }


  @Test
  public void test4(){
    Integer a = 100;
    BinaryOperator<Double> f1 = (x,y) -> Math.pow(x,y);
    BinaryOperator<Double> f2 = Math::pow;
  }



  @Test
  public void test5(){
    Comparator<Integer> c1 = (x, y) -> Integer.compare(x, y);
    Comparator<Integer> c2 = Integer::compare;
    System.out.println(c2.compare(10, 20));

  }

  //类 :: 实例方法
  @Test
  public void test6(){
    BiPredicate<String, String> bp1 = (x, y) -> x.equals(y);
    BiPredicate<String, String> bp2 = String::equals;
  }

  // 构造器方法
  @Test
  public void test7(){
    Supplier<People> s1 = People::new;

    // People 需要 有 只传一个Integer类型的构造器
    Function<Integer, People> f1 = People::new;

    BiFunction<String, Integer, People> bf = People::new;

  }


  // 数组引用
  @Test
  public void test8(){
    Supplier<People> s1 = People::new;


    // People 需要 有 只传一个Integer类型的构造器
    Function<Integer, People> f1 = People::new;

    BiFunction<String, Integer, People> bf = People::new;

    Function<Integer, String[]> f2 = String[]::new;

  }
}
