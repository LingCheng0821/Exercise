package com.fang.jdknewfeatures.jdk8.myexec.stream;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @program: Exercise
 * @Date: 2019/1/9 11:17
 * @Author: chengling.bj
 * @Description:
 */
public class TestStream {

  // 生成Stream
  @Test
  public void test1(){
    // 1. 通过 Collection 系列集合 提供的 stream() 和 parallelStream()
    Stream<String> sr = new ArrayList().stream();

    // 2. 通过 Arrays 中的静态方法 stream() 获取数组流
    String[] str = new String[10];
    Stream<String> sr2 = Arrays.stream(new String[10]);

    // 3. 通过 Stream 类中静态方法 of()
    Stream<String> sr3 = Stream.of("aa","bb","cc");

    // 4. 创建无限流
    Stream<Integer> stream4 = Stream.iterate(0, (x) -> x+2);
    stream4.limit(20).forEach(System.out::println);

    //生成
    Stream.generate(()->Math.random()).forEach(System.out::println);
  }

}
