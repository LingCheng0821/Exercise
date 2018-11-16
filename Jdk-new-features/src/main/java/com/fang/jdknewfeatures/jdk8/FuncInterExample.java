package com.fang.jdknewfeatures.jdk8;

import java.util.function.*;

/**
 * @program: Exercise
 * @Date: 2018/11/16 10:56
 * @Author: chengling.bj
 * @Description:
 */
public class FuncInterExample {
  public static void testFunction() {
    Function<Integer, Integer> incrl = x -> x + 1;
    Function<Integer, Integer> mul = x -> x * 2;
    int x = 2;
    // incrl.apply(x) = 3
    System.out.println("f(x) = x+1, when x = " + x + ", f(x) = " + incrl.apply(x));
    //incrl.compose(mul).apply(x) = 5
    System.out.println("f(x) = x+1, g(x) = 2x,  when x = " + x + ", f(g(x)) = " + incrl.compose(mul).apply(x));
    //incrl.andThen(mul).apply(x) = mul.compose(incrl).apply(x) = 6
    System.out.println("f(x) = x+1, g(x) = 2x,  when x = " + x + ", g(f(x)) = " + incrl.andThen(mul).apply(x));
    System.out.println("f(x) = x+1, g(x) = 2x,  when x = " + x + ", g(f(x)) = " + mul.compose(incrl).apply(x));
    /*---------------------------------------------------------------*/

    // 123456
    Function<Integer, Integer> fun1 = Function.identity();
    System.out.println(fun1.apply(123456));
  }

  public static void testFunction2() {
    Function<Integer, Function<Integer, Integer>> incrl = x -> y -> x * y;
    int x = 3;
    int y =5;
    System.out.println("f(x) = x * y, when x = " + x + ", y = " + y + ", f(x) = " + incrl.apply(y).apply(x));

    //BiFunction ：R apply(T t, U u);接受两个参数，返回一个值，代表一个二元函数；
    BiFunction<Integer, Integer, Integer> biFunction = (a, b) -> a * b;
    System.out.println(biFunction.apply(4,9));

  }

  public static void testOperator() {
    UnaryOperator<Integer> fun1 = x -> x + 1;
    System.out.println(fun1.apply(1234));

    BinaryOperator<Integer> min = BinaryOperator.minBy(((o1, o2) -> o1 - o2));
    System.out.println(min.apply(100, 200));

    BinaryOperator<Integer> max = BinaryOperator.maxBy(((o1, o2) -> o1 - o2));
    System.out.println(max.apply(100, 200));
  }

  public static void testPredicate(){
    Predicate<Integer> predicate1 = a -> true;
    Predicate<Integer> predicate2 = b -> b > 2;
    //true
    System.out.println(" predicate1 = " + predicate1.test(1));
    //false
    System.out.println(" predicate2 = " + predicate2.test(1));
    //false
    System.out.println(predicate1.and(predicate2).test(1));
    //true
    System.out.println(predicate1.or(predicate2).test(1));
    //true
    System.out.println(predicate2.negate().test(1));
  }

  public static void testConsumer(){

  }

  public static void main(String[] args) {
    FuncInterExample.testPredicate();
  }
}
