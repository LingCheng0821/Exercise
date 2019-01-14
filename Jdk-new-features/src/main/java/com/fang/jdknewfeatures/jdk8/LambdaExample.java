package com.fang.jdknewfeatures.jdk8;

/**
 * @program: Exercise
 * @Date: 2018/11/13 16:19
 * @Author: chengling.bj
 * @Description:
 */

import com.fang.jdknewfeatures.util.bean.People;
import com.fang.jdknewfeatures.util.bean.Person;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;

/**
 * lambda表达式是java匿名函数的一种表达形式
 * 对于只申明一个函数的接口，它提供了一个简单和简洁的方式让程序员编写匿名函数，
 * 同时改善了Java集合框架库（collection），使得更加容易迭代、过滤一个集合，更加容易从另一个集合中提取数据。
 *
 *
 * Lambda 允许把函数作为一个方法的参数（函数作为参数传递进方法中）
 * <p>
 * lambda 表达式的语法格式如下：
 * (parameters) -> expression
 * 或
 * (parameters) ->{ statements; }
 * <p>
 *
 * Lambda表达式的特征
 * 类型声明（可选）：可以不需要声明参数类型，编译器会识别参数值。
 * 参数圆括号（可选）：在单个参数时可以不使用括号，多个参数时必须使用。
 * 大括号和return关键字（可选）：如果只有一个表达式，则可以省略大括号和return关键字，编译器会自动的返回值；
 * 相对的，在使用大括号的情况下，则必须指明返回值。
 */
public class LambdaExample {
  /**
   * 这个小项目会涉及到三类人：
   *
   * Drivers：（司机）年龄>16
   * Draftees：（士兵）年龄在18到25之间
   * Pilots：（飞行员）年龄在23在65之间
   * 现在我们有一份名单，名单里面有着三类人的相关信息，比如姓名、年龄、手机号、邮件号码、邮件地址等等，具体的定义看一参见Person类。我们的任务是给所有的司机打电话，给所有的士兵发邮件，给所有的飞行员送邮寄。（搞笑得很:)）
   * @param args
   */
  public static void main(String[] args) {
    List<Person> list = Person.createShortList();

    Predicate<Person> allDrivers = p->p.getAge() >= 16;
    Predicate<Person> allDraftees = p -> p.getAge() >= 18 && p.getAge() <= 25 && p.getGender() == Person.Gender.MALE;
    Predicate<Person> allPilots = p -> p.getAge() >= 23 && p.getAge() <= 65;

    System.out.println("calling all drivers");
    phoneContacts(list, allDrivers);

    System.out.println("emailing all draftees");
    emailContacts(list, allDraftees);

    System.out.println("mailing all pilots");
    mailContacts(list, allPilots);

  }

  public static void phoneContacts(List<Person> personList, Predicate<Person> predicate){
    personList.stream().filter(predicate).forEach(
      p->System.out.println("Calling " + p.getGivenName() + " " + p.getSurName() + " age " + p.getAge() + " at " + p.getPhone())
    );
//    for(Person p :personList){
//      if(predicate.test(p)){
//        System.out.println("Calling " + p.getGivenName() + " " + p.getSurName() + " age " + p.getAge() + " at " + p.getPhone());
//      }
//    }
  }
  public static void emailContacts(List<Person> personList, Predicate<Person> predicate){
    personList.stream().filter(predicate).forEach(
     p-> System.out.println("EMailing  " + p.getGivenName() + " " + p.getSurName() + " age " + p.getAge() + " at " + p.getPhone())
    );

  }
  public static void mailContacts(List<Person> personList, Predicate<Person> predicate){
    personList.stream().filter(predicate).forEach(
     p-> System.out.println("Mailing  " + p.getGivenName() + " " + p.getSurName() + " age " + p.getAge() + " at " + p.getPhone())
    );

  }


  public void test(){
    Runnable rq = new Runnable() {
      @Override
      public void run() {
        System.out.println("Hello World!");
      }
    };

    Runnable r2 = () -> System.out.println("Hello Lambda!");

    TreeSet<People> ts = new TreeSet<>(new Comparator<People>() {
      @Override
      public int compare(People o1, People o2) {
        return o1.getAge() - o2.getAge();
      }
    });

    TreeSet<People> ts1 = new TreeSet<>(Comparator.comparingInt(People::getAge));
    
  }




}
