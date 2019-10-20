package com.example.demo.processor;

import java.util.HashSet;
import java.util.Set;
import org.springframework.batch.item.ItemProcessor;
import com.example.demo.bean.RtfeUser;
import com.example.demo.bean.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserProcessor implements ItemProcessor<RtfeUser, User> {

//This assumes that User.equals() identifies the duplicates
  private Set<Long> seenUsers = new HashSet<Long>();
  
  
  @Override
  public User process(final RtfeUser rtfeUser) throws Exception {
    final String firstName = rtfeUser.getFirstName().toUpperCase();
    final String lastName = rtfeUser.getLastName().toUpperCase();


    final User transformedPerson =
        new User(rtfeUser.getUID(), rtfeUser.getEmail(), firstName, lastName);

    log.info("Converting (" + rtfeUser + ") into (" + transformedPerson + ")");

    if(seenUsers.contains(transformedPerson.getId())) {
      log.error("User ID {} already used",transformedPerson.getId());
      return null;
    }
    seenUsers.add(transformedPerson.getId());
    return transformedPerson;
  }

}
