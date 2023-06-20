package com.interopx.fhir.auth.server.service;

import com.interopx.fhir.auth.server.model.Users;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface UsersService {

  public String registerUser(Users user);

  public String updateUser(Users user);

  public Users getUserById(String id);

  public Users getUserByDetails(String userName, String password, HttpServletRequest request);

  public Users getUserByName(String userName, HttpServletRequest request);

  public String getPropertyValue(String key);

  public Users verifyToken(String token, String email);

  public Users getUserByEmail(String email, HttpServletRequest request);

  public List<Users> getAllUsers();
}
