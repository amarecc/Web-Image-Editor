package pdl.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pdl.backend.web.model.Image;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {

  public Optional<Image> findById(final long id);

  public List<Image> findAll();

  public Image save(Image img);

  public long count();

  public void deleteById(Long id);


}
