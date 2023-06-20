package com.interopx.fhir.auth.server.model;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "REFRESH_TOKEN")
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REFRESH_TOKEN_SEQ")
  @SequenceGenerator(
      name = "REFRESH_TOKEN_SEQ",
      sequenceName = "refresh_token_id_seq",
      allocationSize = 1)
  @Column(name = "id")
  private Long id;

  @OneToOne
  @JoinColumn(name = "user_id", referencedColumnName = "user_id")
  private Users user;

  @Column(nullable = false, unique = true)
  private String token;

  @Column(nullable = false)
  private Instant expiryDate;

  public RefreshToken() {}

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Users getUser() {
    return user;
  }

  public void setUser(Users user) {
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Instant getExpiryDate() {
    return expiryDate;
  }

  public void setExpiryDate(Instant expiryDate) {
    this.expiryDate = expiryDate;
  }
}
