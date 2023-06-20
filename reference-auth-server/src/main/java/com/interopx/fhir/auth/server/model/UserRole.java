package com.interopx.fhir.auth.server.model;

import com.interopx.fhir.auth.server.configuration.JSONObjectUserType;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(
    name = "sof_app_user_role",
    uniqueConstraints = @UniqueConstraint(columnNames = {"role_name"}))
@TypeDefs({
  @TypeDef(name = "list-array", typeClass = ListArrayType.class),
  @TypeDef(name = "StringJsonObject", typeClass = JSONObjectUserType.class)
})
public class UserRole {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLE_SEQ")
  @SequenceGenerator(name = "ROLE_SEQ", sequenceName = "role_id_seq", allocationSize = 1)
  @Column(name = "id")
  private int roleId;

  @Column(name = "role_name")
  private String roleName;

  @Column(name = "role_description")
  private String roleDescription;

  @Type(type = "list-array")
  @Column(name = "permission_names", columnDefinition = "text[]")
  private List<String> permissionNames;

  @Column(name = "status")
  private boolean status;

  @CreationTimestamp
  @Column(name = "created_datetime")
  private Date created;

  @UpdateTimestamp
  @Column(name = "last_updated_datetime")
  private Date lastUpdated;

  @Column(name = "is_role_predefined")
  private boolean isRolePredefined;

  @Column(name = "created_by")
  private String createdBy;

  /** @return the createdBy */
  public String getCreatedby() {
    return createdBy;
  }

  /** @param createdby the createdBy to set */
  public void setCreatedby(String createdby) {
    this.createdBy = createdby;
  }

  /** @return the roleId */
  public int getRoleId() {
    return roleId;
  }

  /** @param roleId the roleId to set */
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }

  /** @return the roleName */
  public String getRoleName() {
    return roleName;
  }

  /** @param roleName the roleName to set */
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

  /** @return the roleDescription */
  public String getRoleDescription() {
    return roleDescription;
  }

  /** @param roleDescription the roleDescription to set */
  public void setRoleDescription(String roleDescription) {
    this.roleDescription = roleDescription;
  }

  /** @return the status */
  public boolean isStatus() {
    return status;
  }

  /** @param status the status to set */
  public void setStatus(boolean status) {
    this.status = status;
  }

  /** @return the created */
  public Date getCreated() {
    return created;
  }

  /** @param created the created to set */
  public void setCreated(Date created) {
    this.created = created;
  }

  /** @return the lastUpdated */
  public Date getLastUpdated() {
    return lastUpdated;
  }

  /** @param lastUpdated the lastUpdated to set */
  public void setLastUpdated(Date lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  /** @return the isRolePredefined */
  public boolean isRolePredefined() {
    return isRolePredefined;
  }

  /** @param isRolePredefined the isRolePredefined to set */
  public void setRolePredefined(boolean isRolePredefined) {
    this.isRolePredefined = isRolePredefined;
  }

  /** @return the permissionNames */
  public List<String> getPermissionNames() {
    return permissionNames;
  }

  /** @param permissionNames the permissionNames to set */
  public void setPermissionNames(List<String> permissionNames) {
    this.permissionNames = permissionNames;
  }
}
