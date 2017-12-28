package service;

import java.util.Collections;

import org.limeprotocol.Envelope;
import org.limeprotocol.serialization.JacksonEnvelopeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import setting.KBTSettings;


@Service
public class HttpService {

	private RestTemplate restTemplate;
	private KBTSettings kbtSettings;
	private HttpHeaders headers;

	@Autowired
	public HttpService(RestTemplate restTemplate, KBTSettings kbtSettings) {
		this.restTemplate = restTemplate;
		this.kbtSettings = kbtSettings;
		init();
	}

	public HttpStatus post(Envelope envelope) {

		String envelopeAsString = new JacksonEnvelopeSerializer().serialize(envelope);
		HttpEntity<String> entity = new HttpEntity<String>(envelopeAsString, this.headers);

		ResponseEntity<Object> response = restTemplate.postForEntity(KBTSettings.BLIP_COMMAND_ENDPOINT, entity,Object.class);

		return response.getStatusCode();
	}
	
	private void init() {
		this.headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Key " + this.kbtSettings.getBlipAuthorizationToken());
	}
}