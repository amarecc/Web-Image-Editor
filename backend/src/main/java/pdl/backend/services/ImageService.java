package pdl.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pdl.backend.repository.ImageRepository;
import pdl.backend.web.model.Image;


@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;

    public Optional<Image> retrieve(Long id) {
        return imageRepository.findById(id);
    }

    public void delete(Image img) {
        imageRepository.delete(img);
    }

    public List<Image> findAll() {
        return imageRepository.findAll();
    }

    public Image save(Image img) {
        return imageRepository.save(img);
    }

    public void reset(Image img){
        img.setInitialData();
        save(img);
    }

    public void resetAll(){
        List<Image> images = findAll();
        images.forEach(img -> img.setInitialData());
    }

    public long count() {
        return imageRepository.count();
    }
}
