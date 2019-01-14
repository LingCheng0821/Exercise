package com.fang.jdknewfeatures.jdk8.myexec.function;

/**
 * @program: Exercise
 * @Date: 2019/1/7 17:12
 * @Author: chengling.bj
 * @Description:
 */
@FunctionalInterface
public interface LongFun<T, R> {
  R getResult (T t, T o);
}
