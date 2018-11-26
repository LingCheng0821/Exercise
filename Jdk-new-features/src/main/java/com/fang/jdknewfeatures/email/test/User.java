package com.fang.jdknewfeatures.email.test;

import com.fang.jdknewfeatures.email.util.Excel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @program: bi-cloud
 * @Date: 2018/11/20 17:09
 * @Author: chengling.bj
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Excel(name = "姓名", width = 30)
  private String name;

  @Excel(name = "年龄", width = 60)
  private Integer age;

  @Excel(name = "出生年月", width = 60)
  private Date birth;

  @Excel(name = "钱", width = 60)
  private Double money;

}
