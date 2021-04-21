package images_processing;

import net.imglib2.Cursor;
import net.imglib2.RandomAccess;
import net.imglib2.algorithm.gauss3.Gauss3;
import io.scif.img.SCIFIOImgPlus;
import net.imglib2.type.numeric.integer.UnsignedByteType;


import net.imglib2.view.Views;
import net.imglib2.view.IntervalView;

import net.imglib2.loops.LoopBuilder;


public class ImagesProcessing {

    private SCIFIOImgPlus<UnsignedByteType> img;
	public String format;

    public ImagesProcessing(byte[] data, String format){
		this.format = format;

		if(format.equals("image/jpeg")){
			try{
				img = ImageConverter.imageFromJPEGBytes(data);
			}catch(Exception e){
				System.out.println(e.getLocalizedMessage());
			}
		}

		else if(format.equals("image/png")){
			try{
				img = ImageConverter.imageFromAPNGBytes(data);
			}catch(Exception e){
				System.out.println(e.getLocalizedMessage());
			}
		}
    }

	private byte[] saveImg(){
		byte[] ret = {0};
		if(format.equals("image/jpeg")){
			try{
				ret = ImageConverter.imageToJPEGBytes(img);
			}catch(Exception e){
				System.out.println(e.getLocalizedMessage());
			}
		}
		else if(format.equals("image/png")){
			try{
				ret = ImageConverter.imageToAPNGBytes(img);
			}catch(Exception e){
				System.out.println(e.getLocalizedMessage());
			}
		}
		return ret;
    }

    public byte[] modifLum(int t) {
        final Cursor<UnsignedByteType> cursor = img.cursor();

        while (cursor.hasNext()) {
            cursor.fwd();
            final UnsignedByteType val = cursor.get();
            if ((val.get() + t) > 255)
                val.set(255);
            else if ((val.get() + t) < 0)
                val.set(0);
            else
                val.set(val.get() + t);
        }
		return saveImg();
    }

    private static void rgbToHsv(int r, int g, int b, float[] hsv) {
		if (hsv.length != 3)
			System.exit(1);

		int max = 0;
		int min = 255;
		int[] rgb = { r, g, b };
		for (int i = 0 ; i < rgb.length; i++) {
			if (rgb[i] > max)
				max = rgb[i];
			if (rgb[i] < min)
				min = rgb[i];
		}

		if (max == min)
			hsv[0] = 0;
		else if (max == r)
			hsv[0] = ((g - b) / (float)(max - min) * 60 + 360) % 360;
		else if (max == g)
			hsv[0] = ((b - r) / (float)(max - min) * 60) + 120;
		else
			hsv[0] = ((r - g) / (float)(max - min) * 60) + 240;

		if (max == 0)
			hsv[1] = 0;
		else
			hsv[1] = ((float)1 - (min/(float)max)) * 100;

		hsv[2] = max;
	}

	private static void hsvToRgb(float h, float s, float v, int[] rgb) {
		float ti = (h/60) % 6;
		float f = h/60 - ti;

		float l = v * (1 - s);
		float m = v * (1 - f * s);
		float n = v * (1 - (1 - f) * s);

		switch ((int)ti) {
			case 0:
				rgb[0] = (int)(v * 100);
				rgb[1] = (int)(n * 100);
				rgb[2] = (int)(l * 100);
				break;
			case 1:
				rgb[0] = (int)(m * 100);
				rgb[1] = (int)(v * 100);
				rgb[2] = (int)(l * 100);
				break;
			case 2:
				rgb[0] = (int)(l * 100);
				rgb[1] = (int)(v * 100);
				rgb[2] = (int)(n * 100);
				break;
			case 3:
				rgb[0] = (int)(l * 100);
				rgb[1] = (int)(m * 100);
				rgb[2] = (int)(v * 100);
				break;
			case 4:
				rgb[0] = (int)(n * 100);
				rgb[1] = (int)(l * 100);
				rgb[2] = (int)(v * 100);
				break;
			case 5:
				rgb[0] = (int)(v * 100);
				rgb[1] = (int)(l * 100);
				rgb[2] = (int)(m * 100);
				break;
			default:
				break;
		}
	}

	public byte[] equaliseHisto(int cannal) {
        final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(img, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(img, 2, 1);
        final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(img, 2, 2);
        final Cursor<UnsignedByteType> cR = inputR.cursor();
        final Cursor<UnsignedByteType> cG = inputG.cursor();
        final Cursor<UnsignedByteType> cB = inputB.cursor();

		int range;
		if(cannal == 1) range = 100;
		else range = 255;

        int hist[] = new int[range +1];
        int histCumul[] = new int[range +1];
        int N = (int) img.max(0) * (int) img.max(1) * img.numDimensions();

        while (cR.hasNext() && cG.hasNext() && cB.hasNext()) {
            cR.fwd();
            cG.fwd();
            cB.fwd();
            float[] hsv = new float[3];

            rgbToHsv(cR.get().get(), cG.get().get(), cB.get().get(), hsv);

            hist[(int) hsv[cannal]]++;
        }


        histCumul[0] = hist[0];
        for (int i = 1; i <= range; i++){
            histCumul[i] = histCumul[i - 1] + hist[i];
        }

        LoopBuilder.setImages(inputR, inputG, inputB).forEachPixel(
            (r, g, b) -> {
                int[] rgb = new int[3];
                float[] hsv = new float[3];
                rgbToHsv(r.get(), g.get(), b.get(), hsv);
                int tmp = (int) hsv[cannal];

                hsv[cannal] = (range * (float)histCumul[tmp]) / N;
                hsvToRgb(hsv[0], hsv[1]/100, hsv[2]/100, rgb);

                r.set(rgb[0]);
                g.set(rgb[1]);
                b.set(rgb[2]);
            }
        );
		return saveImg();
    }

    public byte[] modifTint(int tint) {
        final RandomAccess<UnsignedByteType> r = img.randomAccess();

        final int iw = (int) img.max(0);
        final int ih = (int) img.max(1);

        for (int x = 0; x <= iw; ++x) {
            for (int y = 0; y <= ih; ++y) {
                r.setPosition(x, 0);
                r.setPosition(y, 1);

                int[] rgb = new int[3];
                for(int i=0; i<3; i++){
                    r.setPosition(i,2);
                    rgb[i] = r.get().get();
                }

                float[] hsv = new float[3];
                rgbToHsv(rgb[0], rgb[1], rgb[2], hsv);
                hsvToRgb(tint, hsv[1]/100, hsv[2]/100, rgb);

                for(int i=0; i<3; i++){
                    r.setPosition(i,2);
                    r.get().set(rgb[i]);
                }
            }
        }
		return saveImg();
    }

	public byte[] ColorToGray() {
        final RandomAccess<UnsignedByteType> r = img.randomAccess();

        final int iw = (int) img.max(0);
        final int ih = (int) img.max(1);

        for (int x = 0; x <= iw; ++x) {
            for (int y = 0; y <= ih; ++y) {
                double gray = 0;

                r.setPosition(x, 0);
                r.setPosition(y, 1);

                r.setPosition(0, 2);
                UnsignedByteType val = r.get();
                gray += val.get() * 0.3;

                r.setPosition(1, 2);
                val = r.get();
                gray += val.get() * 0.59;

                r.setPosition(2, 2);
                val = r.get();
                gray += val.get() * 0.11;

                for(int channel = 0; channel < 3; channel++){
                    r.setPosition(channel, 2);
                    r.get().set((int) gray);
                }
            }
        }
		return saveImg();
    }

	public byte[] moyenneur(int size) {
		SCIFIOImgPlus<UnsignedByteType> imgCopy = img.copy();

		final RandomAccess<UnsignedByteType> in = imgCopy.randomAccess();
		final RandomAccess<UnsignedByteType> out = img.randomAccess();

		final int iw = (int) imgCopy.max(0);
		final int ih = (int) imgCopy.max(1);

		for (int x = 0; x <= iw; x++) {
			for (int y = 0; y <= ih; y++) {
				for(int c = 0; c < 3; c++){
					in.setPosition(c, 2);
					int moy = 0;
					int nbMoy = 0;
					for(int x2 = -size; x2 <= size; x2 ++){
						for (int y2 = -size; y2 <= size; y2 ++){
							int tmpx = x+x2;
							int tmpy = y+y2;
							if(tmpx < 0 || tmpx > iw || tmpy < 0 || tmpy > ih) continue;
							in.setPosition(tmpx, 0);
							in.setPosition(tmpy, 1);
							final UnsignedByteType val = in.get();
							moy += val.get();
							nbMoy++;
						}
					}

					moy /= nbMoy;
					out.setPosition(x, 0);
					out.setPosition(y, 1);
					out.setPosition(c, 2);
					final UnsignedByteType val = out.get();
					val.set(moy);
				}
			}
		}
		return saveImg();
	}

	public byte[] gaussien() {		
		SCIFIOImgPlus<UnsignedByteType> imgCopy = img.copy();
		Gauss3.gauss(4.0/3.0, Views.extendBorder(imgCopy), img);
		return saveImg();
	}

    public byte[] contour(){
		SCIFIOImgPlus<UnsignedByteType> imgCopy = img.copy();
		int difVal = 100;
		ColorToGray();

        final RandomAccess<UnsignedByteType> in = imgCopy.randomAccess();
		final RandomAccess<UnsignedByteType> out = img.randomAccess();

        final int iw = (int) img.max(0);
        final int ih = (int) img.max(1);

        for (int x = 1; x < iw; x++) {
            for (int y = 1; y < ih; y++) {
				out.setPosition(x, 0);
				out.setPosition(y, 1);

				//HORIZONTAL
				int left =0;
				in.setPosition(x-1, 0);

				in.setPosition(y-1, 1);
				left += in.get().get();
				in.setPosition(y, 1);
				left += 2 * in.get().get();
				in.setPosition(y+1, 1);
				left += in.get().get();
			
				int right =0;
				in.setPosition(x+1, 0);
				in.setPosition(y-1, 1);
				right += in.get().get();
				in.setPosition(y, 1);
				right += 2 * in.get().get();
				in.setPosition(y+1, 1);
				right += in.get().get();

				if(left-right > difVal || right-left > difVal){
					for(int c=0; c<3; c++){
						out.setPosition(c, 2);
						out.get().set(255);
					}
					continue;
				}

				//VERTICAL
				int up =0;
				in.setPosition(y-1, 1);

				in.setPosition(x-1, 0);
				up += in.get().get();
				in.setPosition(x, 0);
				up += 2 * in.get().get();
				in.setPosition(x+1, 0);
				up += in.get().get();

				int down =0;
				in.setPosition(y+1, 1);

				in.setPosition(x-1, 0);
				down += in.get().get();
				in.setPosition(x, 0);
				down += 2 * in.get().get();
				in.setPosition(x+1, 0);
				down += in.get().get();

				if(up-down > difVal || down-up > difVal){
					for(int c=0; c<3; c++){
						out.setPosition(c, 2);
						out.get().set(255);
					}
					continue;
				}
				
				for(int c=0; c<3; c++){
					out.setPosition(c, 2);
					out.get().set(0);
				}
			}
		}
		return saveImg();
    }

	public byte[] pixelate(int size) {
		SCIFIOImgPlus<UnsignedByteType> imgCopy = img.copy();

		final RandomAccess<UnsignedByteType> in = imgCopy.randomAccess();
		final RandomAccess<UnsignedByteType> out = img.randomAccess();

		final int iw = (int) imgCopy.max(0);
		final int ih = (int) imgCopy.max(1);

		for (int x = 0; x <= iw; x+= size) {
			for (int y = 0; y <= ih; y+=size) {
				for(int c = 0; c < 3; c++){
					in.setPosition(c, 2);
					out.setPosition(c, 2);
					int moy = 0;
					int nbMoy = 0;
					for(int x2 = 0; x2 <= size; x2 ++){
						for (int y2 = 0; y2 <= size; y2 ++){
							int tmpx = x+x2;
							int tmpy = y+y2;
							if(tmpx > iw || tmpy > ih) continue;
							in.setPosition(tmpx, 0);
							in.setPosition(tmpy, 1);
							final UnsignedByteType val = in.get();
							moy += val.get();
							nbMoy++;
						}
					}

					moy /= nbMoy;

					for(int x2 = 0; x2 <= size; x2 ++){
						for (int y2 = 0; y2 <= size; y2 ++){
							int tmpx = x+x2;
							int tmpy = y+y2;
							if(tmpx > iw || tmpy > ih) continue;
							out.setPosition(tmpx, 0);
							out.setPosition(tmpy, 1);
							final UnsignedByteType val = out.get();
							val.set(moy);
						}
					}
				}
			}
		}
		return saveImg();
	}
}
