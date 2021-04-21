package pdl.backend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.annotation.Order;
import pdl.backend.web.controller.ImageController;

@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
class BackendApplicationTests {

	@Autowired
	private ImageController imageController;

	@Test
	@Order(1)
	void contextLoads() {
		assertThat(imageController).isNotNull();
	}
}
