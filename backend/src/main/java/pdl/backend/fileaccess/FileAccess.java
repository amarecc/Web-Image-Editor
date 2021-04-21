package pdl.backend.fileaccess;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.*;

import pdl.backend.fileaccess.exceptions.ImageDirectoryNotFound;
import pdl.backend.web.model.Image;

public class FileAccess {


    /**
     * Look for a /images folder in /backend.
     * 
     * @param path a String representing the path to images folder
     * @return the folder called /images.
     * @throws ImageDirectoryNotFound if the folder is not found in /backend
     */
    public static File findImageDirectory(String path) throws ImageDirectoryNotFound{
        final File imgDir = new File(path);
        boolean found = false;
        try{
            found = imgDir.exists() && imgDir.isDirectory();
        }catch(SecurityException e){
            System.err.println("Permission required to access " + path);
            e.printStackTrace();
        }
        if(!found){
            throw new ImageDirectoryNotFound(path + " does not exist or is not a directory");
        }
        return imgDir;
    }

    /**
     * A recursive function to scan a folder and its subfolders for jpeg and tiff images.
     * 
     * @param folder the original folder to look at.
     * @param result an empty list of images, will be updated at every call.
     */
    public static void scanForImages(final File folder, List<Image> result){

        for(final File f : folder.listFiles()) {
            if (f.isDirectory()) {
                scanForImages(f, result);
            }

            if(f.isFile()){
                if(f.getName().endsWith(".jpg") || f.getName().endsWith(".jpeg")
                || f.getName().endsWith(".tif") || f.getName().endsWith(".tiff")
                || f.getName().endsWith(".png")){
                    byte[] fileContent;
                    try {
                        fileContent = Files.readAllBytes(f.toPath());
                        Image img = new Image(f.getName(), fileContent, buildDimensions(f), getMediaType(f.getName()));
                        result.add(img);
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Builds a string [image width]*[image height]*[number of colors] with a File (image)
     * 
     * @param file
     * @return the string representing [image width]*[image height]*[number of colors].
     */
    public static String buildDimensions(File file){
        String dimensions;
        try{
            BufferedImage bImg = ImageIO.read(file);
            dimensions = bImg.getWidth() + "*" + bImg.getHeight() + "*" + bImg.getColorModel().getNumComponents();
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Could not access the file.");
            dimensions = "0*0*0";
        }
        return dimensions;
    }

    /**
     * Builds a string [image width]*[image height]*[number of colors] with a MultipartFile (image)
     * 
     * @param file Multipart file to get dimensions from
     * @return the string representing [image width]*[image height]*[number of colors].
     */
    public static String buildDimensions(MultipartFile file){
        String dimensions;
        try{
            BufferedImage bImg = ImageIO.read(file.getInputStream());
            dimensions = bImg.getWidth() + "*" + bImg.getHeight() + "*" + bImg.getColorModel().getNumComponents();
        }catch(IOException e){
            e.printStackTrace();
            System.err.println("Could not access the file.");
            dimensions = "0*0*0";
        }
        return dimensions;
    }


    public static MediaType getMediaType(String filename) {
        String res = filename.toLowerCase();
        
        if(res.endsWith(".jpg") || res.endsWith(".jpeg")){
            return MediaType.IMAGE_JPEG;
        } if(res.endsWith(".tif") || res.endsWith(".tiff")){
            return MediaType.parseMediaType("image/tiff");
        } if(res.endsWith(".png")){
            return MediaType.IMAGE_PNG;
        }else{
            return MediaType.TEXT_PLAIN;
        }
    }

}