package com.interopx.fhir.facade.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ResourceUtil {
	private static final Logger logger = LoggerFactory.getLogger(ResourceUtil.class);

	/**
	 * This method iterates bundle entries and returns the resource
	 * 
	 * @param bundle
	 * @param resourceType
	 * @return
	 */
	public static Resource getResourceFromBundleForRead(Bundle bundle, String resourceType) {
		
		if (bundle != null && !bundle.isEmpty()) {
			if (bundle.hasEntry()) {
				for (BundleEntryComponent component : bundle.getEntry()) {
					try {
						if (component.hasResource()) {
							if(!(component.getResource() instanceof Parameters)) {
								if(StringUtils.isNotBlank(resourceType) && StringUtils.isNotBlank(component.getResource().getClass().getSimpleName())) {
									if(resourceType.equalsIgnoreCase(component.getResource().getClass().getSimpleName())) {
										return component.getResource();
									}
									else if(component.getResource() instanceof OperationOutcome) {
										return component.getResource();
									}
								}	
							}
						}
					} catch (Exception e) {
						logger.error(
								"Exception in ProviderResourceGenerator class while iterating bundle entries in getResourceFromBundleForRead() {} ",
								e);
					}
				}
			}
		}
		return null;
	}

	/**
	 * This method iterates bundle entries and modifies the bundle based on search response
	 * @param bundle
	 * @param request
	 * @return
	 */
	public static Object getResourceFromBundleForSearch(Bundle bundle, HttpServletRequest request) {
		if (bundle != null && !bundle.isEmpty()) {
			if(bundle.hasEntry()) {
				try {
					for (BundleEntryComponent component : bundle.getEntry()) {
						try {
							if (component.getResource() instanceof Parameters) {
								component.setResource(null);
							}
							if (component.getResource() instanceof OperationOutcome) {
								return (OperationOutcome) component.getResource();
							}
						}
						catch (Exception e) {
							logger.error(
									"Exception in ProviderResourceGenerator class while iterating bundle entries in getResourceFromBundleForSearch() {} ",
									e);
						}
					}
					BundleLinkComponent blcSelf = new BundleLinkComponent();
					blcSelf.setRelation("self");
					String fullUrl = CommonUtil.getFullURL(request);
					blcSelf.setUrl(fullUrl);
					bundle.getLink().add(blcSelf);
				}
				catch (Exception e) {
					logger.error(
							"Exception in ProviderResourceGenerator class in getResourceFromBundleForSearch() {} ",
							e);
				}
			}
		}
		return bundle;
	}
}
