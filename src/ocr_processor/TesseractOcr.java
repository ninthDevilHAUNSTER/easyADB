package ocr_processor;

import exception.OcrEngineNotActiveException;
import exception.OcrEngineNotSupportException;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.util.OS;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.HelpFunction;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;


public class TesseractOcr implements BaseOcr {
    public static Logger logger = Logger.getLogger(TesseractOcr.class.getName());

    private final String EOL = System.getProperty("line.separator");
    private final String LANG = "chi_sim";
    private String version = "UK";


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

    public HashMap<String, int[]> runTesseractWinWithHocr(BufferedImage img, String language, int psm) {
        // 执行命令
        runTesseractWin(img, "hocr", language, psm, true);
        HashMap<String, int[]> result = new HashMap<String, int[]>();

        // 按行读取 文件（为啥JAVA读个文件这么烦）
        try {
            int position = 0;
            String[] bufstring = new String[1024];
            BufferedReader br = new BufferedReader(new FileReader("hocr.hocr"));
            String line = null;
            while ((line = br.readLine()) != null) {
                bufstring[position] = line;
                position++;
            }
            Document doc = Jsoup.parse(Arrays.toString(bufstring));

            Elements ocr_lines = doc.getElementsByClass("ocr_line");
            for (Element ocr_line : ocr_lines) {

                String[] _buf = ocr_line.attr("title").split("[ ;]");
                if (_buf != null && _buf.length > 5) {
                    int[] pos = {Integer.parseInt(_buf[1]), Integer.parseInt(_buf[2]), Integer.parseInt(_buf[3]), Integer.parseInt(_buf[4]),};
                    String res = ocr_line.text().replaceAll("\\pP|\\s*", "");
                    result.put(res, pos);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String runTesseractWin(BufferedImage img, String output, String language, int psm, boolean hocr) {
        assert OS.isWindows();
        String result = null;
        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(String.format("cmd /c tesseract stdin %s --psm %d -l %s %s", output, psm, language, hocr ? "hocr" : ""));
            OutputStream stdin = process.getOutputStream();
            stdin.write(util.HelpFunction.getBinaryImage(img));
            stdin.close();
            InputStream stdout = process.getInputStream();
            result = consumeInputStream(stdout);
            int exitVal = process.exitValue();
            if (hocr) return result;
            if (exitVal == 0) {
                result = result.substring(0, result.length() - 1); // remove the last character \x0F
                logger.info(result);
            } else {
                String msg = switch (exitVal) {
                    case 1 -> "Errors accessing files.There may be spaces in your image's filename.";
                    case 29 -> "Cannot recongnize the image or its selected region.";
                    case 31 -> "Unsupported image format.";
                    default -> "Errors occurred.";
                };
                logger.fatal(msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public String recognizeSingleText(BufferedImage img) {
        return runTesseractWin(img, "stdout", "chi_sim", 12, false);
    }

    @Override
    public String recognizeMultiText(BufferedImage img) {
        if (OS.isWindows()) {
            HashMap<String, int[]> resMap = runTesseractWinWithHocr(img, "chi_sim", 12);
            Set<String> s = resMap.keySet();
            StringBuilder result = new StringBuilder();
            result.append(s);
            return s.toString();
        }
        return null;
    }

    @Override
    public int[] orientText(BufferedImage img, String text) {
        int[] result = new int[]{0, 0, 0, 0};
        HashMap<String, int[]> resMap = runTesseractWinWithHocr(img, "chi_sim", 12);
        text = text.replaceAll("\\pP|\\s*", "");
        if (resMap.containsKey(text)) {
            result = resMap.get(text);
        }
        return result;
    }

    private boolean getVersion() {
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("cmd /c tesseract --version");
            int exitVal = process.exitValue();
            if (exitVal == 0) {
                version = consumeInputStream(process.getInputStream());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean initOcr() throws OcrEngineNotSupportException {
        if (!getVersion()) throw new OcrEngineNotSupportException("Tesseract Ocr not support");
        return true;
    }

    @Override
    public boolean isOcrActive() throws OcrEngineNotActiveException {
        boolean result = false;
        try {
            result = "82/130".equals(recognizeSingleText(HelpFunction.getBufferedImage(
                    Paths.get("storage", "TEST.png").toString()
            )));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (OS.isWindows() && result) {
            return true;
        } else {
            throw new OcrEngineNotActiveException("Tesseract Ocr not active");
        }
    }

    @Override
    public void printOcrInfo() {
        logger.info("Tesseract Ocr");
    }
}
