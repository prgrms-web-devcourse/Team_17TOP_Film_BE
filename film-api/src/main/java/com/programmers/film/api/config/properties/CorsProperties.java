package com.programmers.film.api.config.properties;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {

	private List<String> allowedOrigins;
	private List<String> allowedMethods;
	private List<String> allowedHeaders;
	private Long maxAge;

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("allowedOrigins", allowedOrigins)
			.append("allowedMethods", allowedMethods)
			.append("allowedHeaders", allowedHeaders)
			.append("maxAge", maxAge)
			.toString();
	}
}
