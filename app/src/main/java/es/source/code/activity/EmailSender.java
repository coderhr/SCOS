package es.source.code.activity;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EmailSender {
    private Properties properties;
    private Session session;
    private Message message;
    private MimeMultipart multipart;

    public EmailSender() {
        this.properties = new Properties();
    }
    public void setProperties(String host,String post){
        this.properties.put("mail.smtp.host",host);   //设置服务器
        this.properties.put("mail.smtp.post",post);   //设置端口
        this.properties.put("mail.smtp.auth",true);   //设置是否验证
        this.session=Session.getInstance(properties);
        this.message = new MimeMessage(session);
        this.multipart = new MimeMultipart("mixed");
    }
    public void setReceiver(String[] receiver) throws MessagingException {
        Address[] address = new InternetAddress[receiver.length];
        for(int i=0;i<receiver.length;i++){
            address[i] = new InternetAddress(receiver[i]);
        }
        this.message.setRecipients(Message.RecipientType.TO, address);
    }
    public void setMessage(String from,String title,String content) throws AddressException, MessagingException{
        this.message.setFrom(new InternetAddress(from));
        this.message.setSubject(title);
        MimeBodyPart textBody = new MimeBodyPart();
        textBody.setContent(content,"text/html;charset=gbk");
        this.multipart.addBodyPart(textBody);
    }
    public void sendEmail(String host,String account,String pwd) throws MessagingException{
        //发送时间
        this.message.setSentDate(new Date());
        //发送的内容，文本和附件
        this.message.setContent(this.multipart);
        this.message.saveChanges();
        //创建邮件发送对象，并指定其使用SMTP协议发送邮件
        Transport transport=session.getTransport("smtp");
        //登录邮箱
        transport.connect(host,account,pwd);
        //发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        //关闭连接
        transport.close();
    }
}
