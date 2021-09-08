package ru.netology;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	private final static String IMAGE_NAME = "jclo-7-6-1";
	private final static String TAG_DEV = "dev";
	private final static String TAG_PROD = "prod";
	private final static int PORT_DEV = 8080;
	private final static int PORT_PROD = 8081;

	@Autowired
	TestRestTemplate restTemplate;

	private static GenericContainer<?> devApp = new GenericContainer<>(IMAGE_NAME + ":" + TAG_DEV)
		.withExposedPorts(PORT_DEV);
	private static GenericContainer<?> prodApp = new GenericContainer<>(IMAGE_NAME + ":" + TAG_PROD)
		.withExposedPorts(PORT_PROD);

	@BeforeAll
	public static void setUp() {
		devApp.start();
		prodApp.start();
	}

	@ParameterizedTest
	@MethodSource("buildTestDataSource")
	public void testBuild(GenericContainer<?> app, int mappedPort, String expectedResponse) {
		int port = app.getMappedPort(mappedPort);
		ResponseEntity<String> forEntity = restTemplate.getForEntity("http://localhost:" + port + "/profile", String.class);
		assertEquals(expectedResponse, forEntity.getBody());
	}

	public static Stream<Arguments> buildTestDataSource() {
		return Stream.of(
			Arguments.of(devApp, PORT_DEV, "Current profile is dev"),
			Arguments.of(prodApp, PORT_PROD, "Current profile is production")
		);
	}

}