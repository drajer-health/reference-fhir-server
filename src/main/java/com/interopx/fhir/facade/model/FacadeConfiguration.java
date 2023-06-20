package com.interopx.fhir.facade.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="facade_config")
public class FacadeConfiguration {
	@Id	
	@Column(name="facade_config_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer facadeConfigId;
	
	@Column(name="facade_config_key")
	private String facadeConfigKey;
	
	@Column(name="facade_config_value")
	private String facadeConfigValue;

	public Integer getFacadeConfigId() {
		return facadeConfigId;
	}

	public void setFacadeConfigId(Integer facadeConfigId) {
		this.facadeConfigId = facadeConfigId;
	}

	public String getFacadeConfigKey() {
		return facadeConfigKey;
	}

	public void setFacadeConfigKey(String facadeConfigKey) {
		this.facadeConfigKey = facadeConfigKey;
	}

	public String getFacadeConfigValue() {
		return facadeConfigValue;
	}

	public void setFacadeConfigValue(String facadeConfigValue) {
		this.facadeConfigValue = facadeConfigValue;
	}
}
