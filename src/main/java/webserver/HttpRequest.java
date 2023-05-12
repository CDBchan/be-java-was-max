package webserver;

import static utils.HttpRequestUtils.*;

import java.util.Map;

public class HttpRequest {

	private final String method;
	private final String URL;
	private final String queryString;
	private final Map<String, String> queryParams;

	public HttpRequest(String startLine) {
		this.method = extractMethod(startLine);
		this.URL = extractURL(startLine);
		this.queryString = extractQueryString();
		this.queryParams = parseQueryString(queryString);
	}

	private String extractMethod(String startLine) {
		String[] tokens = startLine.split(" ");
		return tokens[0];
	}

	private String extractURL(String startLine) {
		String[] tokens = startLine.split(" ");
		return decodeURL(tokens[1]);
	}

	private String extractQueryString() {
		String[] query = URL.split("\\?");
		return query.length > 1 ? query[1] : "";
	}

	public String getMethod() {
		return method;
	}

	public String getURL() {
		return URL;
	}

	public String getQueryString() {
		return queryString;
	}

	public Map<String, String> getQueryParams() {
		return queryParams;
	}
}
