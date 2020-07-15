package ocr_processor;

import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;

public class TesseractOcr implements BaseOcr {
    public static Logger logger = Logger.getLogger(TesseractOcr.class.getName());



    @Override
    public String recognizeSingleText(BufferedImage img) {
        return null;
    }

    @Override
    public String recognizeMultiText(BufferedImage img) {
        return null;
    }

    @Override
    public int[] orientText(BufferedImage img, String text) {
        int[] result = new int[]{0, 0, 0, 0};
        return result;
    }

    @Override
    public boolean initOcr() {
        return false;
    }

    @Override
    public boolean isOcrActive() {
        return false;
    }

    @Override
    public void printOcrInfo() {
        logger.info("Tesseract Ocr");
    }

}
