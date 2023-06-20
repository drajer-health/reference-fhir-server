package com.interopx.fhir.auth.server.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "sof_app_user_permission",
    uniqueConstraints = @UniqueConstraint(columnNames = {"permission_name"}))
public class UserPermission {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSION_NEW_SEQ")
  @SequenceGenerator(
      name = "PERMISSION_NEW_SEQ",
      sequenceName = "permission_new_id_seq",
      allocationSize = 1)
  @Column(name = "id")
  private Long permissionId;

  @Column(name = "permission_name")
  private String permissionName;

  @Column(name = "status")
  private boolean status;

  @Column(name = "permission_description")
  private String permissionDescription;

  @Column(name = "last_updated_datetime")
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdated;

  public Long getPermissionId() {
    return permissionId;
  }

  public void setPermissionId(Long permissionId) {
    this.permissionId = permissionId;
  }

  public String getPermissionName() {
    return permissionName;
  }

  public void setPermissionName(String permissionName) {
    this.permissionName = permissionName;
  }

  public boolean isStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public Date getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  /** @return the permissionDescription */
  public String getPermissionDescription() {
    return permissionDescription;
  }

  /** @param permissionDescription the permissionDescription to set */
  public void setPermissionDescription(String permissionDescription) {
    this.permissionDescription = permissionDescription;
  }
}
