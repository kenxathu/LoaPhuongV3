
package vn.mobifone.loaphuong.util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import vn.mobifone.loaphuong.lib.SystemConfig;


/**
 * @author Ha Vien Duong
 *
 */
public class EmailUtils {
    private static String smtpHost;
    private static String smtpPort;
    private static String asFrom[];
    private static String asTo[];
    
    
    public static void loadConfig(){
        smtpHost = SystemConfig.getConfig("mail_host");
        smtpHost = SystemConfig.getConfig("mail_port");
        asFrom = SystemConfig.getConfig("from_mail").split(",");
        asTo = SystemConfig.getConfig("to_mail").split(",");
    }
	// B? sun dia chi cc
    public static void sendMail(String asTo[], String userName, String password)
            throws AddressException, MessagingException {
        //
        loadConfig();
        // Create a mail session
        SMTPAuthenticator auth = new SMTPAuthenticator();

        // Create a mail session
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {                   
                    return new PasswordAuthentication(SystemConfig.getConfig("email_username"), SystemConfig.getConfig("email_password"));
                }
        });
        Transport transport = session.getTransport();

        // Construct the message
        MimeMessage msg = new MimeMessage(session);
        InternetAddress[] from = new InternetAddress[asFrom.length];
        for (int i = 0; i < asFrom.length; i++) {
            from[i] = new InternetAddress(asFrom[i]);
        }
        msg.addFrom(from);

        InternetAddress[] to = new InternetAddress[asTo.length];
        for (int i = 0; i < asTo.length; i++) {
            to[i] = new InternetAddress(asTo[i],false);
        }
        msg.addRecipients(Message.RecipientType.TO, to);
//        if (!ccMail.isEmpty())
//            msg.addRecipient(RecipientType.CC, new InternetAddress(ccMail));
//       // msg.addHeader("Mail thong bao he thong QTCP", "Mail thong bao he thong QTCP");
        msg.setSubject("Thông báo từ hệ thống loa phường - Thông tin tài khoản", "UTF-8");
        MimeBodyPart msgPart = new MimeBodyPart();
        msgPart.setContent("Bạn vừa tạo tài khoản trên hệ thống loa phường" +"</br>" + "username: "
                +userName+"</br>" + "password : "+password+"</br>"+" Truy câp vào hệ thống để thay đổi thông tin tài khoản", "text/html;charset=UTF-8");
        
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(msgPart);

        msg.setContent(mp);
        if(Message.RecipientType.TO!=null){
            transport.connect();
            transport.sendMessage(msg, msg.getRecipients(Message.RecipientType.TO));
            transport.close();
        }
    } 
}
class SMTPAuthenticator extends Authenticator {

    private PasswordAuthentication authentication;

    public SMTPAuthenticator() {
        String username = SystemConfig.getConfig("email_username");
        String password = SystemConfig.getConfig("email_password");
        authentication = new PasswordAuthentication(username, password);
    }

    public PasswordAuthentication getPasswordAuthentication() {
        return authentication;
    }
     

}
