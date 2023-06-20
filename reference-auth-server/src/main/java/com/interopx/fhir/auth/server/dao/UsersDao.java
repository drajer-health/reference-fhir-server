package com.interopx.fhir.auth.server.dao;

import com.interopx.fhir.auth.server.model.Users;
import java.util.List;

public interface UsersDao {

  public String register(Users user);

  public String updateUser(Users user);

  public Users getUserById(String id);

  public Users getUserByDetails(String userName, String password);

  public Users getUserByName(String userName);

  public String getPropertyValue(String key);

  public Users verifyToken(String token, String email);

  public Users getUserByEmail(String email);

  public List<Users> getAllUsers();
}
