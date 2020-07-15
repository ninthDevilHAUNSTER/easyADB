package ocr_processor;

import exception.OcrEngineNotActiveException;
import net.sourceforge.yamlbeans.YamlException;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface BaseOcr {
    public String recognizeSingleText(BufferedImage img) throws IOException, InterruptedException;

    public String recognizeMultiText(BufferedImage img);

    public int[] orientText(BufferedImage img,String text);


    public boolean initOcr() throws YamlException, FileNotFoundException;

    public boolean isOcrActive() throws OcrEngineNotActiveException;

    public void printOcrInfo();
}
