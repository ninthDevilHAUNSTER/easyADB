package ocr_processor;

import org.apache.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jdesktop.swingx.util.OS;

import javax.imageio.ImageIO;

public class TesseractOcr implements BaseOcr {
    public static Logger logger = Logger.getLogger(TesseractOcr.class.getName());
    private final String EOL = System.getProperty("line.separator");
    private final String tessPath = "C://Program Files//Tesseract-OCR";

    public static String consumeInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            System.out.println(s);
            sb.append(s);
        }
        return sb.toString();
    }

    private String preprocessCmdCommand(BufferedImage img) throws IOException, InterruptedException {
      String cmd = "C:\\Program Files\\Tesseract-OCR\\tesseract.exe";
        Runtime run = Runtime.getRuntime();//返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            consumeInputStream(p.getInputStream());
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1)//p.exitValue()==0表示正常结束，1：非正常结束
                    System.err.println("命令执行失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public String recognizeSingleText(BufferedImage img) throws IOException, InterruptedException {
        preprocessCmdCommand(img);
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
