
package hello;

import hello.wsdl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.List;

public class WeatherClient{

	private static final Logger log = LoggerFactory.getLogger(WeatherClient.class);

	@Autowired
	WebServiceTemplate webServiceTemplate;

	@Autowired
	private String defaultActionContext;

	public GetCityForecastByZIPResponse getCityForecastByZip(String zipCode) {

		GetCityForecastByZIP request = new GetCityForecastByZIP();
		request.setZIP(zipCode);

		log.info("Requesting forecast for " + zipCode);

		GetCityForecastByZIPResponse response = (GetCityForecastByZIPResponse) webServiceTemplate
				.marshalSendAndReceive(
						request,
						this.setSoapActionCallback("GetCityForecastByZIP"));

		return response;
	}

	public GetWeatherInformationResponse getWeatherInformation() {
		GetWeatherInformation getWeatherInformation = new GetWeatherInformation();

		GetWeatherInformationResponse getWeatherInformationResponse = (GetWeatherInformationResponse) webServiceTemplate
				.marshalSendAndReceive(
						getWeatherInformation,
						this.setSoapActionCallback("GetWeatherInformation"));

		return getWeatherInformationResponse;
	}

	public void printGetWeatherInformationResponse(GetWeatherInformationResponse getWeatherInformationResponse){
		List<WeatherDescription> weatherDescriptions = getWeatherInformationResponse.getGetWeatherInformationResult().getWeatherDescription();
		for(WeatherDescription weatherDescription :  weatherDescriptions){
			log.info(weatherDescription.getWeatherID() + " " + weatherDescription.getDescription() + " " + weatherDescription.getPictureURL());
		}
	}

	public void printGetCityForecastByZIPResponse(GetCityForecastByZIPResponse response) {

		ForecastReturn forecastReturn = response.getGetCityForecastByZIPResult();

		if (forecastReturn.isSuccess()) {
			log.info("Forecast for " + forecastReturn.getCity() + ", " + forecastReturn.getState());

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			for (Forecast forecast : forecastReturn.getForecastResult().getForecast()) {

				Temp temperature = forecast.getTemperatures();

				log.info(String.format("%s %s %s°-%s°", format.format(forecast.getDate().toGregorianCalendar().getTime()),
						forecast.getDesciption(), temperature.getMorningLow(), temperature.getDaytimeHigh()));
				log.info("");
			}
		} else {
			log.info("No forecast received");
		}
	}

	public SoapActionCallback setSoapActionCallback(String action){
		return new SoapActionCallback(this.defaultActionContext + "/" + action);
	}

}
