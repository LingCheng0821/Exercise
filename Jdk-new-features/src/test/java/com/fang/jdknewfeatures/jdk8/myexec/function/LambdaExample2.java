package com.fang.jdknewfeatures.jdk8.myexec.function;

import com.fang.jdknewfeatures.util.bean.People;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @program: Exercise
 * @Date: 2019/1/7 16:30
 * @Author: chengling.bj
 * @Description:
 */
public class LambdaExample2 {
  @Test
  public void test1(){
    List<People> list = People.getPeopleList();
    Comparator<People> con1 = Comparator.comparingInt(People::getAge);
    Comparator<People> con2 = Comparator.comparing(People::getName);
    Collections.sort(list, con1.thenComparing(con2));
    list.stream().forEach(System.out::println);
  }

  @Test
  public void test2(){
    String str = testMyFun("abCDefG", (a) -> a.toUpperCase());
    System.out.println(str);
  }


  @Test
  public void test3(){
    long add = testLongFun(1000L, 2000L, (a, b) -> a + b);
    long mul = testLongFun(1000L, 2000L, (a, b) -> a * b);
    System.out.println(add);
    System.out.println(mul);
  }


  public String testMyFun(String src, MyFun myFun){
    return myFun.getValue(src);
  }

  public long testLongFun(long par1, long par2,  LongFun<Long, Long> myFun){
    return myFun.getResult(par1, par2);
  }




}
