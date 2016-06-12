
package hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapMessageFactory;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class WeatherConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("hello.wsdl");
		return marshaller;
	}

	@Bean
	public String defaultActionContext(){
		return "http://ws.cdyne.com/WeatherWS";
	}

	@Bean
	public String defaultUri(){
		return "http://wsf.cdyne.com/WeatherWS/Weather.asmx";
	}

	@Bean
	public SoapMessageFactory soapMessageFactory(){
		SoapMessageFactory soapMessageFactory = new SaajSoapMessageFactory();
		soapMessageFactory.setSoapVersion(SoapVersion.SOAP_12);
		return soapMessageFactory;
	}

	@Bean
	public WeatherClient weatherClient() {
		WeatherClient client = new WeatherClient();
		return client;
	}

	@Bean
	public WebServiceTemplate webServiceTemplate(SoapMessageFactory soapMessageFactory, String defaultUri, Jaxb2Marshaller marshaller){
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate(soapMessageFactory);
		webServiceTemplate.setDefaultUri(defaultUri);
		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		return webServiceTemplate;
	}

}
