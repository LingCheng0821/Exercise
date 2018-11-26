package com.fang.jdknewfeatures.email.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @program: bi-cloud
 * @Date: 2018/11/20 17:37
 * @Author: chengling.bj
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailInfo {
  //邮件主题
  private String subject = "";
  // 邮件内容
  private String sendHtml = "";
  //收件人地址
  private String receiveUser = "";
  //抄送人地址
  private String copyReceiveUser = "";
  //密送人地址
  private String bccReceiveUser = "";
  //附件
  private File[] attachments;

  public MailInfo(String subject, String sendHtml, String receiveUser, String copyReceiveUser, String bccReceiveUser) {
    this.subject = subject;
    this.sendHtml = sendHtml;
    this.receiveUser = receiveUser;
    this.copyReceiveUser = copyReceiveUser;
    this.bccReceiveUser = bccReceiveUser;
  }
}
