package pdl.backend.web.controller;

import static pdl.backend.fileaccess.FileAccess.*;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pdl.backend.services.ImageService;
import pdl.backend.web.exceptions.MyImageException;
import pdl.backend.web.model.Image;
import images_processing.ImagesProcessing;

@RestController
public class ImageController {

  @Autowired
  private ObjectMapper mapper;

  @Autowired
  private final ImageService imageService;

  @Autowired
  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/tiff" })
  public ResponseEntity<byte[]> getImage(@PathVariable("id") long id) {
    Image img = imageService.retrieve(id)
                  .orElseThrow(() -> new MyImageException("Image not found, id : " + id));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(img.getFullType());

    return new ResponseEntity<>(img.getData(), headers, HttpStatus.OK);
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<Void> deleteImage(@PathVariable("id") long id) {
    Image img = imageService.retrieve(id)
                  .orElseThrow(() -> new MyImageException("Image not found, id : " + id));

    imageService.delete(img);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @RequestMapping(value = "/images", method = RequestMethod.POST)
  public ResponseEntity<Void> addImage(@RequestParam("file") MultipartFile file,
      RedirectAttributes redirectAttributes) {
        if (! file.getContentType().equals(MediaType.IMAGE_JPEG_VALUE) && ! file.getContentType().equals(MediaType.IMAGE_PNG_VALUE) && ! file.getContentType().equals("image/tiff"))
          return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        try {
          Image img = new Image(file.getOriginalFilename(), file.getBytes(), buildDimensions(file), getMediaType(file.getOriginalFilename()));

          imageService.save(img);

          return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
          throw new MyImageException("Error in file posting");
        }
  }

  @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
  @ResponseBody
  public ResponseEntity<ArrayNode> getImageList() {
    ArrayNode nodes = mapper.createArrayNode();

    for (Image img: imageService.findAll()) {
      JsonNode node = mapper.valueToTree(img);
      nodes.add(node);
    }

    return new ResponseEntity<>(nodes, HttpStatus.OK);
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.POST, produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/tiff" }, params = {"algorithm"})
  @ResponseBody
  public ResponseEntity<byte[]> postImageProcessing(@PathVariable("id") long id, @RequestParam(required = true) String algorithm
    , @RequestParam(required = false) String gain
    , @RequestParam(required = false) String channel
    , @RequestParam(required = false) String tint
    , @RequestParam(required = false) String filter
    , @RequestParam(required = false) String lvl ) {

    Image img = imageService.retrieve(id)
                  .orElseThrow(() -> new MyImageException("Image not found, id : " + id));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(img.getFullType());

    ImagesProcessing imgP = new ImagesProcessing(img.getData(), img.getType());

    switch(algorithm){

      case("addLumen"):
        try{
          Integer.parseInt(gain);
        }catch(Exception e){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        img.setData(imgP.modifLum(Integer.parseInt(gain)));
        break;

      case("equaliseHist"):
        if(channel.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(channel.equals("S")){
          img.setData(imgP.equaliseHisto(1));
          break;
        }
        if(channel.equals("V")){
          img.setData(imgP.equaliseHisto(2));
          break;
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

      case("color"):
        try{
          Integer.parseInt(tint);
        }catch(Exception e){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        img.setData(imgP.modifTint(Integer.parseInt(tint)));
        break;

      case("blur"):
        if(filter.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if(filter.equals("gaussian")){
          img.setData(imgP.gaussien());
          break;
        }

        try{
          Integer.parseInt(lvl);
        }catch(Exception e){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(filter.equals("average")){
          img.setData(imgP.moyenneur(Integer.parseInt(lvl)));
          break;
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

      case("outline"):
        img.setData(imgP.contour());
        break;

      case("colorToGray"):
        img.setData(imgP.ColorToGray());
        break;

      case("pixelate"):
        try{
          Integer.parseInt(lvl);
        }catch(Exception e){
          return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        img.setData(imgP.pixelate(Integer.parseInt(lvl)));
        break;
      
      default:
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    imageService.save(img);

    return new ResponseEntity<>(img.getData(), headers, HttpStatus.OK);
  }


  @RequestMapping(value = "/images/{id}/reset", method = RequestMethod.POST, produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE, "image/tiff" })
  public ResponseEntity<byte[]> postReset(@PathVariable("id") long id) {
    Image img = imageService.retrieve(id)
                  .orElseThrow(() -> new MyImageException("Image not found, id : " + id));

    imageService.reset(img);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(img.getFullType());

    return new ResponseEntity<>(img.getData(), headers, HttpStatus.OK);
  }


}
