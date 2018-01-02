package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Command {

	@JsonProperty("method")
	private String method;

	@JsonProperty("status")
	private String status;

	@JsonProperty("id")
	private String id;

	@JsonProperty("from")
	private String from;

	@JsonProperty("pp")
	private String pp;

	@JsonProperty("to")
	private String to;

	@JsonProperty("resource")
	private Resource resource;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPp() {
		return pp;
	}

	public void setPp(String pp) {
		this.pp = pp;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
