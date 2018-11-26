package com.fang.jdknewfeatures.email.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 * @program: bi-cloud
 * @Date: 2018/11/20 17:40
 * @Author: chengling.bj
 * @Description:
 *  发送邮件
 */
@Component
@PropertySource("classpath:mail.properties")
public class MailUtil {
  private MimeMessage message;

  private Session session;

  private Transport transport;
  @Value("${mail.smtp.host}")
  private String mailHost;
  @Value("${mail.sender.username}")
  private String sender_username ;
  @Value("${mail.sender.password}")
  private String sender_password ;

  public static String path;

  static {
    String os = System.getProperty("os.name");
    if (os.startsWith("win") || os.startsWith("Win")) {
      path = "E:/mail/";
    }
    else {
      path = "/www/webdev/ebcenter.light.fang.com/xxljobexecutors/";
    }
  }


  public void init (boolean debug) {
    Properties properties=new Properties();
    System.out.println(mailHost);
    System.out.println(sender_username);
    System.out.println(sender_password);
    session = Session.getInstance(properties);
    // 开启后有调试信息
    session.setDebug(debug);
    message = new MimeMessage(session);
  }

  /**
   * 发送 邮件
   * @param mailInfo
   */
  public void doSendHtmlEmail(MailInfo mailInfo) {
    //初始化
    init(false);

    try {
      // 1. 设置 发件人
      InternetAddress from = new InternetAddress(sender_username);
      message.setFrom(from);

      // 2. 设置 收件人
      message.setRecipients(Message.RecipientType.TO,  InternetAddress.parse(mailInfo.getReceiveUser()));

      //3. 设置 抄送人
      if(mailInfo.getCopyReceiveUser() != ""){
        InternetAddress cc = new InternetAddress(mailInfo.getCopyReceiveUser());
        message.setRecipient(Message.RecipientType.CC, cc);
      }

      // 4. 设置 密送
      if(mailInfo.getBccReceiveUser() != ""){
        InternetAddress bcc = new InternetAddress(mailInfo.getBccReceiveUser());
        message.setRecipient(Message.RecipientType.BCC, bcc);
      }

      // 5. 邮件主题
      if(mailInfo.getSubject() != null && mailInfo.getSubject() != "") {
        message.setSubject(mailInfo.getSubject(), "UTF-8");
      }

      // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
      Multipart multipart = new MimeMultipart();

      // 6. 添加邮件正文
      BodyPart contentPart = new MimeBodyPart();
      contentPart.setContent(mailInfo.getSendHtml(), "text/html;charset=UTF-8");
      multipart.addBodyPart(contentPart);

      // 7. 添加附件的内容
      File[] attachments = mailInfo.getAttachments();
      if (attachments != null) {
        for (File attachment : attachments) {
          BodyPart attachmentBodyPart = new MimeBodyPart();
          DataSource source = new FileDataSource(attachment);
          attachmentBodyPart.setDataHandler(new DataHandler(source));
          // MimeUtility.encodeWord可以避免文件名乱码
          attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
          multipart.addBodyPart(attachmentBodyPart);
        }
      }

      // 将multipart对象放到message中
      message.setContent(multipart);
      // 保存邮件
      message.saveChanges();
      transport = session.getTransport("smtp");
      // smtp验证，就是你用来发邮件的邮箱用户名密码
      transport.connect(mailHost, sender_username, sender_password);
      // 发送

      transport.sendMessage(message, message.getAllRecipients());
      System.out.println("send success!");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      if (transport != null) {
        try {
          transport.close();
        }
        catch (MessagingException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public <T> void sendMail(List<T> dataList, MailInfo mailInfo, String dstExcelFilename, String sheetName) throws Exception {

    dstExcelFilename = path + dstExcelFilename;

    ExcelDataFormatter edf = new ExcelDataFormatter();

    ExcelUtil.createExcel(dataList, edf, dstExcelFilename, sheetName);

    String content = Excel2Html.readExcelToHtml(dstExcelFilename, mailInfo.getSendHtml(), true);
    mailInfo.setSendHtml(content);

    File file1 = new File(dstExcelFilename);
    File[] affix = new File[]{file1};

    mailInfo.setAttachments(affix);

    //发送邮件
    doSendHtmlEmail(mailInfo);
  }

}
