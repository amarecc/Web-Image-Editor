package pdl.backend.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.http.MediaType;

@Table(name = "image")
@Entity
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "data", columnDefinition = "MEDIUMBLOB")
  private byte[] data;

  @Column(name = "dimensions")
  private String dimensions;

  @Column(name = "type", columnDefinition = "MEDIUMBLOB")
  private MediaType type;

  @Column(name = "initialData", columnDefinition = "MEDIUMBLOB")
  private byte[] initialData;

  public Image() {

  }

  public Image(final String name, final byte[] data, String dimensions, MediaType type) {
    this.name = name;
    this.data = data;
    this.dimensions = dimensions;
    this.type = type;
    this.initialData = data;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDimensions(){
    return dimensions;
  }

  @JsonIgnore
  public MediaType getFullType(){
    return type;
  }

  public String getType(){
    return type.toString();
  }

  public void setName(final String name) {
    this.name = name;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public void setInitialData(){
    this.data = initialData;
  }

  @JsonIgnore
  public byte[] getInitialData(){
    return initialData;
  }

  public String toString(){
    return "Image #ID = " + id + " | Name = " + name + " | Dimensions : " + dimensions + " | Media type : " + type.toString();
  }

  @JsonIgnore
  public byte[] getData() {
    return data;
  }
}
