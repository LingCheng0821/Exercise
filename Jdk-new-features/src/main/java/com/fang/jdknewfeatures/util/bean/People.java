package com.fang.jdknewfeatures.util.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Exercise
 * @Date: 2018/11/14 15:19
 * @Author: chengling.bj
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class People {
  private String name;
  private Integer age;

  public People(Integer age) {
    this.age = age;
  }

  public static int sortByName(People A , People B ){
    return A.getName().compareTo(B.getName());
  }

  public int sortByAge(People A , People B ){
    return A.getAge().compareTo(B.getAge());
  }

  public static List<People> getPeopleList(){
    List<People> peopleList = new ArrayList<People>();
    peopleList.add(new People("a",17));
    peopleList.add(new People("b",16));
    peopleList.add(new People("c",19));
    peopleList.add(new People("d",15));
    peopleList.add(new People("f",18));
    peopleList.add(new People("g",18));
    peopleList.add(new People("e",18));
    return peopleList;
  }
}
