package com.smart.common.utils


import net.coobird.thumbnailator.Thumbnails
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/**
 * 图片压缩个工具
 * @author zhongming
 * @since 3.0
 * 2018/7/26上午8:51
 */
object ImageUtil {

    /**
     * 压缩图片
     * @param inputStream 图片输入流
     * @param outputStream 图片输出流
     * @param scale 图片压缩的大小 0.5代表一半
     * @param quality 图片压缩质量 1最好  0最差
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun compress(inputStream: InputStream, outputStream: OutputStream, scale: Double, quality: Double) {
        Thumbnails.of(inputStream)
                .scale(scale)
                .outputQuality(quality)
                .toOutputStream(outputStream)
    }

    /**
     * 压缩图片
     * @param inputStream 图片输入流
     * @param outputStream 图片输出流
     * @param width 图片压缩后宽度
     * @param height 图片压缩后高度
     * @param quality 图片质量
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun compress(inputStream: InputStream, outputStream: OutputStream, width: Int, height: Int, quality: Double) {
        Thumbnails.of(inputStream)
                .size(width, height)
                .outputQuality(quality)
                .toOutputStream(outputStream)
    }

    /**
     * 按照宽度等比压缩
     * @param inputStream 图片输入流
     * @param outputStream 图片输出流
     * @param width 图片压缩后宽度
     */
    @Throws(IOException::class)
    @JvmStatic
    fun compress(inputStream: InputStream, outputStream: OutputStream, width: Int) {
        //获取图片原宽度
        Thumbnails.of(inputStream)
                .width(width)
                .outputQuality(1.0)
                .toOutputStream(outputStream)
    }

    /**
     * 压缩图片
     * @param inputStream 图片输入流
     * @param outputStream 图片输出流
     * @param scale 图片压缩的大小 0.5代表一半
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun compress(inputStream: InputStream, outputStream: OutputStream, scale: Double) {
        compress(inputStream, outputStream, scale, 1.0)
    }

    /**
     * 压缩图片
     * @param inputStream 图片输入流
     * @param outputStream 图片输出流
     * @param width 图片压缩后宽度
     * @param height 图片压缩后高度
     * @throws IOException
     */
    @Throws(IOException::class)
    @JvmStatic
    fun compress(inputStream: InputStream, outputStream: OutputStream, width: Int, height: Int) {
        compress(inputStream, outputStream, width, height, 1.0)
    }

    /**
     * 获取图片对象
     */
    @JvmStatic
    fun getBuffredImage(inputStream: InputStream): BufferedImage {
        return Thumbnails.of(inputStream)
                .asBufferedImage()
    }

    /**
     * 判断是否是图片
     */
    @JvmStatic
    fun isImage(contentType: String?): Boolean {
        if (contentType != null && contentType.startsWith("image")) {
            return true
        }
        return false
    }

}

