package com.interopx.fhir.auth.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "system_values")
public class SystemValues {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "system_value_seq")
  @SequenceGenerator(
      name = "system_value_seq",
      sequenceName = "system_value_id_seq",
      allocationSize = 1)
  @Column(name = "system_value_id")
  private Long systemValueId;

  @Column(name = "category")
  private String category;

  @Column(name = "key")
  private String key;

  @Column(name = "value")
  private String value;

  @Column(name = "active")
  private boolean active;

  @Column(name = "visible")
  private boolean visible;

  public Long getSystemValueId() {
    return systemValueId;
  }

  public String getCategory() {
    return category;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }

  public boolean isActive() {
    return active;
  }

  public void setSystemValueId(Long systemValueId) {
    this.systemValueId = systemValueId;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }
}
