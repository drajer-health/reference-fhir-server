package com.interopx.fhir.auth.server.jwtservice;

import ch.qos.logback.classic.Logger;
import com.interopx.fhir.auth.server.dao.UsersDao;
import com.interopx.fhir.auth.server.model.Users;
import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
  private static final Logger logger =
      (Logger) LoggerFactory.getLogger(JwtUserDetailsService.class);
  @Autowired private UsersDao userDao;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    logger.debug("Start in loadUserByUsername() of JwtUserDetailsService class ");
    Users user = userDao.getUserByName(username);
    if (user == null) {
      logger.error("User not found with username: " + username);
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    logger.debug("End in loadUserByUsername() of JwtUserDetailsService class ");
    return new org.springframework.security.core.userdetails.User(
        user.getUser_name(), user.getUser_password(), new ArrayList<>());
  }
}
