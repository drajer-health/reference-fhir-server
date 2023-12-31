package com.interopx.fhir.auth.server.model;

import java.util.Date;
import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "jwks")
public class Jwks {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "jwk", columnDefinition = "text")
  private String jwk;

  @CreationTimestamp
  @Column(name = "last_updated_datetime", columnDefinition = "TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdatedDatetime;

  public Date getLastUpdatedDatetime() {
    return lastUpdatedDatetime;
  }

  public void setLastUpdatedDatetime(Date lastUpdatedDatetime) {
    this.lastUpdatedDatetime = lastUpdatedDatetime;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getJwk() {
    return jwk;
  }

  public void setJwk(String jwk) {
    this.jwk = jwk;
  }
}
