package com.fang.jdknewfeatures.jdk8.myexec.function;

import org.junit.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @program: Exercise
 * @Date: 2019/1/7 17:54
 * @Author: chengling.bj
 * @Description:
 */
public class TestFun2 {

  // Consumer
  @Test
  public void test1(){
    Consumer<String> res = a -> System.out.println(a);
    res.accept("hello world");
  }


  // Function
  @Test
  public void test3(){
    Function<Integer, Integer> res = (a) ->  a * a;
    System.out.println(res.apply(10));
  }

  // Predicate
  @Test
  public void test4(){
    Predicate<Integer> res = (a) ->  a > 10;
    System.out.println(res.test(50));
  }
}
