package webserver.response.header.impl;

import static utils.HttpResponseUtils.*;

import utils.ContentType;
import webserver.response.header.HttpResponseHeader;

public class OkHeader implements HttpResponseHeader {

	private String contentType;
	private int contentLength;
	private String header;

	public OkHeader(String viewName, int contentLength) {
		this.contentType = ContentType.getByExtension(extractFileExtensionFromView(viewName));
		this.contentLength = contentLength;
		createResponse200Header();
	}

	private void createResponse200Header() {
		header = "Content-Type: " + contentType + "\r\n" +
			"Content-Length: " + contentLength + "\r\n" +
			"\r\n";
	}

	@Override
	public String toString() {
		return header;
	}
}

