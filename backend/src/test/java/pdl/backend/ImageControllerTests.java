package pdl.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Order(1)
	public void getImageListShouldReturnSuccess() throws Exception {
		mockMvc.perform(get("/images"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
			
	}

	@Test
	@Order(2)
	public void getImageShouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/images/9999"))
			.andExpect(status().isNotFound());
	}

	/*@Test
	@Order(3)
	public void deleteImageShouldReturnSuccess() throws Exception {
		mockMvc.perform(delete("/images/0"))
			.andExpect(status().isOk());
	}*/

	@Test
	@Order(4)
	public void deleteImageShouldReturnBadRequest() throws Exception {
		mockMvc.perform(delete("/images/logo.png"))
			.andExpect(status().isBadRequest());
	}



	@Test
	@Order(5)
	public void createImageShouldReturnUnsupportedMediaType() throws Exception {
		MockMultipartFile someText = new MockMultipartFile("file", "", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());

		mockMvc.perform(MockMvcRequestBuilders.multipart("/images").file(someText).contentType(MediaType.TEXT_PLAIN))
			.andExpect(status().isUnsupportedMediaType());
	}
}
