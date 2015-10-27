package com.dataturn.bean;

public class Message {
	private String status;
	
	private String method;
	
	private String from;
	
	private String to;
	
	
	private String ip;
	
	private String port;

	private String toType;
	
	
	public String getToType() {
		return toType;
	}

	public void setToType(String toType) {
		this.toType = toType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "Message [status=" + status + ", method=" + method + ", from="
				+ from + ", to=" + to + ", ip=" + ip + ", port=" + port
				+ ", toType=" + toType + "]";
	}
	
}
