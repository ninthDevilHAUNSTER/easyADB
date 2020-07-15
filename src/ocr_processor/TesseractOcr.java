package ocr_processor;

import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.util.OS;

public class TesseractOcr implements BaseOcr {
    public static Logger logger = Logger.getLogger(TesseractOcr.class.getName());
    private final String LANG_OPTION = "-l"; //英文字母小写l，并非数字1

    private final String EOL = System.getProperty("line.separator");

    private final String tessPath = "C://Program Files//Tesseract-OCR";

    private String preprocessCmdCommand(BufferedImage img) {
        //        List cmd = new ArrayList();
//        if(OS.isWindowsXP()){
//            cmd.add(tessPath+"//tesseract");
//        }else if(OS.isLinux()){
//            cmd.add("tesseract");
//        }else{
//            cmd.add(tessPath+"//tesseract");
//        }
//        cmd.add("");
//        cmd.add(outputFile.getName());
//        cmd.add(LANG_OPTION);
//        //cmd.add("chi_sim");
//        cmd.add("eng");

        return null;
    }


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
