package img_processor;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.awt.image.BufferedImage;

public class ImageColorHistogram {

    public double colorHistogramSimilarity(BufferedImage src1, BufferedImage src2) {
        int[] array1 = this.colorHistogramSimilarity(src1);
        int[] array2 = this.colorHistogramSimilarity(src2);
        double sumX = 0.0;
        double sumY = 0.0;
        double sumX_Sq = 0.0;
        double sumY_Sq = 0.0;
        double sumXY = 0.0;
        int N = 64;
        for (int i = 0; i < 64; i++) {
            int x = array1[i];
            int y = array2[i];
            sumX += x;
            sumY += y;
            sumX_Sq += Math.pow(x, 2);
            sumY_Sq += Math.pow(y, 2);
            sumXY += x * y;
        }

        double numerator = sumXY - sumX * sumY / N;
        double denominator = Math.sqrt((sumX_Sq - sumX * sumX / N)
                * (sumY_Sq - sumY * sumY / N));

        // 分母不能为0
        if (denominator == 0) {
            return 0;
        }

        return numerator / denominator;
    }

    private String getRange(int color) {
        if (color >= 0 && color <= 63) {
            return "0";
        } else if (color >= 64 && color <= 127) {
            return "1";
        } else if (color >= 128 && color <= 191) {
            return "2";
        } else if (color >= 192 && color <= 255) {
            return "3";
        }
        return null;
    }

    public int[] colorHistogramSimilarity(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        Map<String, Integer> imageHistogram = new HashMap<String, Integer>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int rgb = src.getRGB(j, i);
                int r = (rgb >> 16) & 0xff;//取出次高位（16-23）红色分量的信息
                int g = (rgb >> 8) & 0xff;//取出中位（8-15）绿色分量的信息
                int b = rgb & 0xff;//取出低位（0-7）蓝色分量的信息
                String key = this.getRange(r) + this.getRange(g) + this.getRange(b);

                Integer count = imageHistogram.get(key);
                if (count == null) {
                    imageHistogram.put(key, 1);
                } else {
                    imageHistogram.put(key, count + 1);
                }
            }
        }
        int[] result = new int[64];
        String key = "";
        int idx = 0;
        for (int r = 0; r < 4; r++) {
            for (int g = 0; g < 4; g++) {
                for (int b = 0; b < 4; b++) {
                    key = String.valueOf(r) + String.valueOf(g) + String.valueOf(b);
                    Integer count = imageHistogram.get(key);
                    if (count == null) {
                        result[idx] = 0;
                    } else {
                        result[idx] = count;
                    }
                    idx++;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {

        ImageColorHistogram p = new ImageColorHistogram();
        String imagePathA = "D:\\python_box\\DiplomatProjectDev\\attackGTSRB\\pair5\\33\\";
        String imagePathB = "D:\\python_box\\DiplomatProjectDev\\attackGTSRB\\pair5\\34\\";

        ArrayList<String> img_names = new ArrayList<String>();
        img_names.add("109.png");
        img_names.add("289.png");
        img_names.add("554.png");
        img_names.add("655.png");
        for (int i = 0; i < img_names.size(); i++) {
            System.out.println(
                    p.colorHistogramSimilarity(
                            ImageIO.read(new File(imagePathA + img_names.get(i))),
                            ImageIO.read(new File(imagePathB + img_names.get(i)))
                    )
            );
        }
    }
}