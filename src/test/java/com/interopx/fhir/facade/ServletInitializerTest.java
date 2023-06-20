package com.interopx.fhir.facade;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ServletInitializerTest {
	@Mock
	  private SpringApplicationBuilder springApplicationBuilder;

	  @Test
	  public void testIt() {
	    ServletInitializer servletInitializer = new ServletInitializer();
	    when(springApplicationBuilder.sources(IxFhirFacadeApplication.class)).thenReturn(springApplicationBuilder);

	    SpringApplicationBuilder result = servletInitializer.configure(springApplicationBuilder);

	    verify(springApplicationBuilder).sources(IxFhirFacadeApplication.class);
	    assertEquals(springApplicationBuilder,result);
	  }

}
