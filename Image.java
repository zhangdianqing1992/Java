import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {
    public static List<BufferedImage> SHARE_IMAGE_BG_BUFFERED_IMAGE = new ArrayList<>();

    private static BufferedImage getShareImageBgBufferedImage() {
        return SHARE_IMAGE_BG_BUFFERED_IMAGE.get(new Random().nextInt(SHARE_IMAGE_BG_BUFFERED_IMAGE.size()));
    }


    //在图片中 添加
    public static void main(String[] args) throws IOException {
        String sourceImg = "/Users/zhangdianqing/Downloads/akImages/111111.jpeg"; //源图片地址
        String targetImg = "/Users/zhangdianqing/Downloads/akImages/1.jpeg";
       /* String imageCode = "";
        String[] codes = imageCode.split(",");
        for (String c : codes) {
            String targetImg = "/Users/zhangdianqing/Downloads/akImages/" + c + ".jpg"; //新存储的地址
            markImgMark(sourceImg, targetImg);
        }*/
        markImgMark(sourceImg, targetImg);
    }

    /**
     * 为图片添加图片文字
     *
     * @param imageLocalUrl 原图
     * @param imgName       制作完成的图片
     * @return
     * @throws IOException
     */
    public static String markImgMark(String imageLocalUrl, String imgName) throws IOException {
        Font font = new Font("SimHei", Font.PLAIN, 46);// 添加字体的属性设置

        try {
            // 加载本地图片
            String newUserName = "MbLgCVFLCJ";
            File file = new File(imageLocalUrl);
            Image img = ImageIO.read(file);
            BufferedImage imageLocal = ImageIO.read(file);
            // 加载用户的二维码
            // 以本地图片为模板
            Graphics2D g = imageLocal.createGraphics();
            // 在模板上添加用户二维码(地址,左边距,上边距,图片宽度,图片高度,未知)
            // 设置文本样式
            g.setFont(font);
            GradientPaint gt = new GradientPaint(0, 0, new Color(255, 241, 164), 30, 30, new
                    Color(255, 169, 19), true);
            int width = img.getWidth(null);//水印宽度
            int height = img.getHeight(null);//水印高
            FontRenderContext context = g.getFontRenderContext();
            Rectangle2D bounds = font.getStringBounds(newUserName, context);
            double x = (width - bounds.getWidth()) / 2;
            double y = (height - bounds.getHeight()) / 2;
            double ascent = -bounds.getY();
            double baseY = y + ascent;
            System.out.println(x);
            System.out.println(y);
            g.drawImage(img.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);

            g.setPaint(gt);
//            g.setColor(Color.BLACK);
            // 截取用户名称的最后一个字符
            // 拼接新的用户名称

            // 添加用户名称
//            g.drawString(newUserName, 620, imageLocal.getHeight() - 530);
            g.drawString(newUserName, 550, 445);
            // 完成模板修改
            g.dispose();
            // 获取新文件的地址
            File outputfile = new File(imgName);
            // 生成新的合成过的用户二维码并写入新图片
            ImageIO.write(imageLocal, "png", outputfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回给页面的图片地址(因为绝对路径无法访问)

        return imgName;
    }
}
