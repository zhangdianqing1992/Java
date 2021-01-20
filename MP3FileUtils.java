package com.lanxum.bigdata.training.institution.student.util;

import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;

import java.io.*;

@Slf4j
public class MP3FileUtils {

    /**
     * 读取文件时长
     *
     * @param filePath
     * @return
     */
    public static Float getFileLength(String filePath) {
        try {
            File mp3File = new File(filePath);
            MP3File f = (MP3File) AudioFileIO.read(mp3File);
            MP3AudioHeader audioHeader = (MP3AudioHeader) f.getAudioHeader();
            return Float.parseFloat(audioHeader.getTrackLength() + "");
        } catch (Exception e) {
            e.printStackTrace();
            return 0f;
        }
    }

    public static void main(String[] args) {
        // System.out.println(getFileLength("/Users/zhangdianqing/Desktop/audios/poetry_10348.mp3"));
        //getPoetyId();

        String filePath = "/Users/zhangdianqing/poetry_audios/";
        String newFilePath = "/Users/zhangdianqing/poetry_audios_short_/";
        File file = new File(filePath);
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
            String fileName = filelist[i];
            if (fileName.startsWith("poetry_")) {
                File mp3File = new File(filePath.concat(fileName));
                try {
                    MP3File f = (MP3File) AudioFileIO.read(mp3File);
                    MP3AudioHeader mp3AudioHeader = f.getMP3AudioHeader();
                    String bitRate = mp3AudioHeader.getBitRate();
                    //System.out.println("比特率是：" + mp3AudioHeader.getBitRate());
                    cutAudios(bitRate, filePath, fileName, newFilePath, fileName.substring(0, fileName.length() - 4).concat("_short.mp3"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //文件时长
                //cutAudios(filePath, fileName, newFilePath, fileName.substring(0, fileName.length() - 4).concat("_short.mp3"));
            }

        }
    }

    public static void getPoetyId() {
        File file = new File("/Users/zhangdianqing/Desktop/images/");
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
            String fileName = filelist[i];
            if (fileName.startsWith("poetry_")) {
                //文件时长
                String id = fileName.substring(7, fileName.length() - 4);
                System.out.print(id + ",");
            }

        }
    }


    /**
     * 截取
     */
    public static void cutAudios(String bitRate, String filePath, String fileName, String newFilePath, String shortName) {
        //f1,f2分别为需要剪切的歌曲路径
        File f1 = new File(filePath + fileName);
        //File f2 = new File("E:\\CutMusicTest\\慢慢.mp3");
        //f为合并的歌曲
        File f = new File(newFilePath + shortName);
        cut(f1, f, bitRate);
    }

    public static void cut(File f1, File f, String bitRate) {
        BufferedInputStream bis1 = null;
        BufferedOutputStream bos = null;
        //第一首歌剪切部分起始字节
        int start1 = 0;//320kbps（比特率）*58s*1024/8=2375680 比特率可以查看音频属性获知
        int end1 = (Integer.valueOf(bitRate) * 1024 * 30) / 8;//320kbps*120s*1024/8=4915200

        int tatol1 = 0;
        try {
            //两个输入流
            bis1 = new BufferedInputStream(new FileInputStream(f1));
            //缓冲字节输出流（true表示可以在流的后面追加数据，而不是覆盖！！）
            bos = new BufferedOutputStream(new FileOutputStream(f, true));

            //第一首歌剪切、写入
            byte[] b1 = new byte[512];
            int len1 = 0;
            while ((len1 = bis1.read(b1)) != -1) {
                tatol1 += len1;   //累积tatol
                if (tatol1 < start1) {  //tatol小于起始值则跳出本次循环
                    continue;
                }
                bos.write(b1);   //写入的都是在我们预先指定的字节范围之内
                if (tatol1 >= end1) {  //当tatol的值超过预先设定的范围，则立刻刷新bos流对象，并结束循环
                    bos.flush();
                    break;
                }
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {//切记要关闭流！！
                if (bis1 != null) bis1.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
