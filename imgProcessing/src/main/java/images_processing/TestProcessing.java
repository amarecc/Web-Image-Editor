package images_processing;

import java.io.File;

public class TestProcessing {
    public static void main(String[] args) {

        /*StringBuffer accessFileName =  new StringBuffer() ;
        accessFileName.append("tmp").append(File.separator).append("access.log") ;
        System.out.println(accessFileName) ;
        System.out.println(System.getProperty("user.dir")) ;*/

        String in = System.getProperty("user.dir");
        in += File.separator + "online-image-editor" + File.separator + "backend" + File.separator + "images_backup";
        System.out.println(in) ;

        /*ImagesProcessing imgP = new ImagesProcessing(in);
        imgP.contour();*/
    }

}
