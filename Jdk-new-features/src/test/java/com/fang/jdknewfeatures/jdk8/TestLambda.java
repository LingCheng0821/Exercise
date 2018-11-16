package com.fang.jdknewfeatures.jdk8;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @program: Exercise
 * @Date: 2018/11/15 10:54
 * @Author: chengling.bj
 * @Description:
 */

public class TestLambda {
  //4-1 Runnable Lambda
  @Test
  public void test1(){
    //匿名内部类
    Runnable r1 = new Runnable() {
      @Override
      public void run() {
        System.out.println("hello Runnable 1!");
      }
    };

    //Lambda
    Runnable r2 = ()-> System.out.println("hello Runnable 2");

    r1.run();
    r2.run();
  }

  //Comparator Lambda
  @Test
  public void test2(){
    List<Integer> list = Arrays.asList(9,1,8,2,7,3,6,4,5);
    Collections.sort(list, new Comparator<Integer>() {

      @Override
      public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
      }
    });

    Collections.sort(list, (a,b)->a.compareTo(b));

  }

  // List Lambda
  @Test
  public void test3(){
    List<Integer> nums = Arrays.asList(9,5,8,4,7,3,6,2,1);
    nums.forEach(t-> System.out.println(t));
    System.out.println(nums);
  }

  @Test
  public void test4(){

  }
}
