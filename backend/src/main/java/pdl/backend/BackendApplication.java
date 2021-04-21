package pdl.backend;

import static pdl.backend.fileaccess.FileAccess.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import pdl.backend.fileaccess.exceptions.ImageDirectoryNotFound;
import pdl.backend.services.ImageService;
import pdl.backend.web.model.Image;

@SpringBootApplication
public class BackendApplication {

	@Autowired
	ImageService imageService;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void initDb(){
		if(imageService.count() > 0) return;
		File imgDir;
		try{
			imgDir = findImageDirectory(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "images"); 
			List<Image> imgList = new ArrayList<Image>();
			scanForImages(imgDir, imgList);
			imgList.forEach(img -> imageService.save(img));

		}catch(ImageDirectoryNotFound e){
			e.printStackTrace();
			System.err.println("Could not find /images folder");
		}
	}
}

