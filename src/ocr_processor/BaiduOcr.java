package ocr_processor;

import base.HelpFunction;
import com.baidu.aip.ocr.AipOcr;
import exception.OcrEngineNotActiveException;
import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class BaiduOcr implements BaseOcr {
    public static Logger logger = Logger.getLogger(BaiduOcr.class.getName());
    private AipOcr client;
    private static String app_id;
    private static String app_key;
    private static String app_secret;

    public BaiduOcr() throws YamlException, FileNotFoundException {
        initOcr();
    }

    /**
     * 参考代码
     * https://ai.baidu.com/ai-doc/OCR/nk3h7yc12#通用文字识别
     *
     * @param img BufferedImage
     * @return
     */
    @Override
    public String recognizeSingleText(BufferedImage img) {
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "false");
        options.put("detect_language", "false");
        options.put("probability", "false");

        JSONObject res;

        // 参数为本地图片二进制数组
        byte[] file = HelpFunction.getImageBinary(img, "png");
        res = client.basicGeneral(file, options);
        JSONArray words_result = res.getJSONArray("words_result");
        int num = (Integer) res.get("words_result_num");
        if (num >=2)
            logger.warn("获取多个返回结果，取最前面一个");
        return (String) words_result.getJSONObject(0).get("words");
    }

    /**
     * 参考代码
     * https://ai.baidu.com/ai-doc/OCR/nk3h7yc12#通用文字识别（含位置信息版）
     *
     * @param img
     */
    @Override
    public void recognizeMultiText(BufferedImage img) {
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("recognize_granularity", "big");
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("vertexes_location", "false"); // 取消单字识别
        options.put("probability", "false"); // 取消置信度获取
        JSONObject res;
        // 参数为本地图片二进制数组
        byte[] file = HelpFunction.getImageBinary(img, "png");
        res = this.client.general(file, options);
        System.out.println(res.toString(2));
    }

    @Override
    public boolean initOcr() throws YamlException, FileNotFoundException {
        try {
            YamlReader reader = new YamlReader(new FileReader("config/config.yaml"));
            // 总感觉我YAML的读法怪怪的
            Map ocr_config = reader.read(Map.class);
            Map ocr_info = (Map) ocr_config.get("baidu_api");
            BaiduOcr.app_id = (String) ocr_info.get("app_id");
            BaiduOcr.app_key = (String) ocr_info.get("app_key");
            BaiduOcr.app_secret = (String) ocr_info.get("app_secret");
            this.client = new AipOcr(BaiduOcr.app_id, BaiduOcr.app_key, BaiduOcr.app_secret);
            return true;
        } catch (YamlException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * @return true if active
     * @throws OcrEngineNotActiveException Ocr引擎未激活，程序将强制结束
     */
    @Override
    public boolean isOcrActive() throws OcrEngineNotActiveException {
        // 调用接口
        String path = "storage/TEST.png";
        JSONObject res = client.basicGeneral(path, new HashMap<String, String>());
//        System.out.println(res.toString(2));
        JSONArray words_result = res.getJSONArray("words_result");
        if ((Integer) res.get("words_result_num") == 1 &&
                ((String) words_result.getJSONObject(0).get("words")).equals("82/130")) {
            return true;
        } else {
            throw new OcrEngineNotActiveException("Baidu Ocr not Active");
        }
    }

    @Override
    public void printOcrInfo() {
        logger.info("Baidu OCR");
    }


}
