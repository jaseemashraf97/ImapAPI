package com.example.pieEye.emailRetriever.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;



@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MailStructure {
    private int Id;
    private String senderName;
    private Date recivedDate;
    private String subject;
    private String content;

}
