
package hello;

import hello.wsdl.GetCityForecastByZIPResponse;

import hello.wsdl.GetWeatherInformationResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	CommandLineRunner lookup(final WeatherClient weatherClient) {
		return new CommandLineRunner() {
			@Override
			public void run(String... args) throws Exception {
				String zipCode = "94304";

				if (args.length > 0) {
					zipCode = args[0];
				}
				GetCityForecastByZIPResponse response = weatherClient.getCityForecastByZip(zipCode);
				weatherClient.printGetCityForecastByZIPResponse(response);

				GetWeatherInformationResponse response2 = weatherClient.getWeatherInformation();
				weatherClient.printGetWeatherInformationResponse(response2);

//				weatherClient.normalTestFunc();

			}
		};
	}

}
