package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Controller.UserController;

public class RequestHandler implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
	private static final String BASE_PATH = "src/main/resources/templates";
	private final UserController userController;

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
		this.userController = new UserController();
	}

	public void run() {
		logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			HttpRequest httpRequest = new HttpRequest(br.readLine());

			logger.debug("httpRequest Method : {}", httpRequest.getMethod());
			logger.debug("httpRequest URL : {}", httpRequest.getURL());
			logger.debug("httpRequest QueryString : {}", httpRequest.getQueryString());
			logger.debug("httpRequest httpVersion : {}", httpRequest.getHttpVersion());
			logger.debug("httpRequest QueryParams : {}", httpRequest.getQueryParams());

			userController.requestMapper(httpRequest);

			DataOutputStream dos = new DataOutputStream(out);
			byte[] body = Files.readAllBytes(new File(BASE_PATH + httpRequest.getURL()).toPath());
			response200Header(dos, body.length);
			responseBody(dos, body);

		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
