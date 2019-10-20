package com.example.demo.bean;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString 
@XmlRootElement(name = "user")
public class RtfeUser {

  @XmlElement(name = "uID")
  private Long uID;
  @XmlElement(name = "email")
  private String email;
  @XmlElement(name = "firstName")
  private String firstName;
  @XmlElement(name = "lastName")
  private String lastName;



}
