package com.example.pieEye.emailRetriever.Controller;

import com.example.pieEye.emailRetriever.Model.MailStructure;
import com.example.pieEye.emailRetriever.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import javax.mail.MessagingException;
import java.util.List;

@RestController
public class FetchEmailController {
    @Autowired
    MailService mailService;

    /**
     * Accepts Login credentials and pass it to service layer and returns the list of all the mails in the inbox
     * @param mailId
     * @param pass
     * @return
     * @throws Exception
     */
    @PostMapping("/mails")
    public List<MailStructure> login(@RequestParam("email") String mailId, @RequestParam("password") String pass) throws Exception
    {
       return mailService.fetchallMails(mailId,pass);
    }

    /**
     * Returns one mail and its details if login is successful. Else returns a string "No mails found"
     * @param id
     * @return
     */
    @GetMapping("/mails/{id}")
    public Object mail(@PathVariable("id") int id)
    {
        return mailService.FetchOneMail(id);
    }

    /**
     * Controller for logging out from the session.
     * @return
     * @throws MessagingException
     */
    @RequestMapping("/mails/logout")
    public String logout() throws MessagingException {
         return mailService.logout();
    }
}
