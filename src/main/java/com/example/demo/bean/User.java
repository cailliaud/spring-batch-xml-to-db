package com.example.demo.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

  private Long id;
  private String email;
  private String firstName;
  private String lastName;

  
}
