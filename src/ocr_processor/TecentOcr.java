package ocr_processor;

import com.baidu.aip.util.Base64Util;
import exception.OcrEngineNotActiveException;
import net.sourceforge.yamlbeans.YamlException;
import util.Base64ImgUtil;

import javax.servlet.http.HttpUtils;
import java.awt.image.BufferedImage;
import java.io.*;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;

class TencentAISDK {

    public static String genSignString(Map<String, String> parser) {
        String uri_str = "";

        Map<String, String> map = new TreeMap<String, String>();
        map.putAll(parser);

        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + map.get(key));
            if (key.equalsIgnoreCase("app_key"))
                continue;

            String encode = "";
            try {
                String value = parser.get(key);
                encode = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            uri_str = uri_str + key + "=" + encode + "&";
        }
        String sign_str = uri_str + "app_key=" + parser.get("app_key");
        System.out.println("-----urlencode_str:" + sign_str);
        sign_str = conVertTextToMD5(sign_str).toUpperCase();
        System.out.println("-----sign_str:" + sign_str);
        return sign_str;

    }

    // 计算字符串的MD5
    public static String conVertTextToMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            // 32位加密
            return buf.toString();
            // 16位的加密
            // return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }


    public static Integer DateToTimestamp(Date time) {
        Timestamp ts = new Timestamp(time.getTime());
        return (int) ((ts.getTime()) / 1000);
    }
}


public class TecentOcr implements BaseOcr {
    private static String app_id;
    private static String app_key;

    @Override
    public String recognizeSingleText(BufferedImage img) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public String recognizeMultiText(BufferedImage img) {
        String result=null;
        TencentAISDK tsdk = new TencentAISDK();

        Map params = new HashMap<String, String>();

        params.put("app_id", app_id);
        params.put("app_key", app_key);
        String time_string = String.valueOf(TencentAISDK.DateToTimestamp(new Date()));
        params.put("time_stamp", time_string);
        params.put("nonce_str", time_string);
        Base64ImgUtil imgutl = new Base64ImgUtil();
        String base64 = Base64ImgUtil.GetImageStr("C:\\b84.jpg");

        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream("C:\\b84.jpg");
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Base64Util b64 = new Base64Util();
        String b64str = b64.encode(data);
        params.put("image", b64str);
        params.put("sign", TencentAISDK.genSignString(params));

        // 打开和URL之间的连接
        try {
            HttpUtils util = new HttpUtils();
            result = HttpUtils.sendPostToOtherServer("https://api.ai.qq.com/fcgi-bin/ocr/ocr_generalocr", params);
            System.out.println("result:"+ result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int[] orientText(BufferedImage img, String text) {
        return new int[0];
    }

    @Override
    public boolean initOcr() throws YamlException, FileNotFoundException {
        return false;
    }

    @Override
    public boolean isOcrActive() throws OcrEngineNotActiveException {
        return false;
    }

    @Override
    public void printOcrInfo() {

    }
}
