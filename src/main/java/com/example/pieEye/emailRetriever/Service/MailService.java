package com.example.pieEye.emailRetriever.Service;
import com.example.pieEye.emailRetriever.Model.MailStructure;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MailService {
    private Session session;
    private Store store;
    private Folder folder;
    private String protocol = "imaps";
    private String file = "INBOX";
    private List<MailStructure> mails = new ArrayList<>();
    public MailService()
    {
    }
    /**
     * allMails act as a cnetralised controller.
     * Purpose of implemetation of this function is to hide all other functions in service layer from Controller layer.
     * @param mailId
     * @param password
     * @return
     * @throws Exception
     */

    public List<MailStructure> fetchallMails(String mailId, String password) throws Exception
    {
        login("outlook.office365.com",mailId,password);
        System.out.println(isLoggedIn());
        Message[] msg = getMessage();
        int i=0;
        for (Message m : msg)
        {
            MimeMultipart mimeMultipart = (MimeMultipart) m.getContent();
            String filteredBody = getTextFromMimeMultipart(mimeMultipart);
            mails.add(new MailStructure(i++,m.getFrom()[0].toString(),m.getReceivedDate(),m.getSubject().toString(),filteredBody));
        }
        return mails;
    }

    /**
     * Checks if user have loggedIn with he credentials
     * @return
     */
    private boolean isLoggedIn()
    {
        return store.isConnected();

    }

    /**
     * Accept the login credentials and establish the connection to outlook via IMAP protocal and fetch the inbox.
     * @param host
     * @param username
     * @param password
     * @throws Exception
     */
    private void login(String host, String username, String password) throws Exception {
        URLName url = new URLName(protocol, host, 993, file, username, password);
        if (session == null) {
            Properties properties = null;
            try {
                properties = System.getProperties();
            } catch (SecurityException securityException) {
                properties = new Properties();
            }
            session = Session.getInstance(properties, null);

        }
        store = session.getStore(url);
        store.connect();
        folder = store.getFolder(url);
        folder.open(Folder.READ_ONLY);
    }

    /**
     * Close the Inbox and logout he user.
     * @throws MessagingException
     */
    public String logout() throws MessagingException {
        folder.close(false);
        store.close();
        store = null;
        session = null;
        return "Logged out successfully";
    }

    /**
     * Returns all the messages inside INBOX
     * @return
     * @throws MessagingException
     */
    private Message[] getMessage() throws MessagingException {
        return folder.getMessages();
    }

    /**
     * The content of a Message is passed as an mime multipart.
     * The function returns if the content is plain, else returns the body of the mail after a filteration process.
     * @param mimeMultipart
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws IOException, MessagingException {
        int count = mimeMultipart.getCount();
        if (count == 0)
            throw new MessagingException("Multipart with no body parts not supported.");
        boolean multipartAlt = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (multipartAlt)
            // alternatives appear in an order of increasing
            // faithfulness to the original content. Customize as req'd.
            return getTextFromBodyPart(mimeMultipart.getBodyPart(count - 1));
        String result = "";
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            result += getTextFromBodyPart(bodyPart);
        }
        return result;
    }

    /**
     * Returns body content if its a plain text, Else recursively check for plain text and returns the same.
     * @param bodyPart
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private String getTextFromBodyPart(BodyPart bodyPart) throws IOException, MessagingException {
        String result = "";
        if (bodyPart.isMimeType("text/plain"))
        {
            result = (String) bodyPart.getContent();
        }
        else if (bodyPart.isMimeType("text/html"))
        {
            String html = (String) bodyPart.getContent();
            result = org.jsoup.Jsoup.parse(html).text();
        }
        else if (bodyPart.getContent() instanceof MimeMultipart)
        {
            result = getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
        }
        return result;
    }

    /**
     * Returns a single mail and its details to the controller layer with respect to the index given for the mail.
     * @param index
     * @return
     */
    public Object FetchOneMail(int index)
    {
        if (index>=0&&index< mails.size()&&isLoggedIn())
            return mails.get(index);
        return "No mails found";
    }

}
