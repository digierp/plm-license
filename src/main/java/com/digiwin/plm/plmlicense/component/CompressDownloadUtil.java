package com.digiwin.plm.plmlicense.component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author liuhao
 * @date 2020/4/18
 */
public class CompressDownloadUtil {

    private CompressDownloadUtil() {}

    /**
     * 设置下载响应头
     *
     * @param response
     * @return
     * @author hongwei.lian
     * @date 2018年9月7日 下午3:01:59
     */
    public static HttpServletResponse setDownloadResponse(HttpServletResponse response, String downloadName) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;fileName*=UTF-8''"+ downloadName);
        return response;
    }

    /**
     * 将多个文件压缩到指定输出流中
     *
     * @param files 需要压缩的文件列表
     * @param outputStream  压缩到指定的输出流
     * @author hongwei.lian
     * @date 2018年9月7日 下午3:11:59
     */
    public static void compressZip(List<File> files, OutputStream outputStream) throws IOException {
        try (ZipOutputStream zipOutStream = new ZipOutputStream(new BufferedOutputStream(outputStream))){
            // -- 设置压缩方法
            zipOutStream.setMethod(ZipOutputStream.DEFLATED);
            //-- 将多文件循环写入压缩包
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);
                FileInputStream filenputStream = new FileInputStream(file);
                byte[] data = new byte[(int) file.length()];
                filenputStream.read(data);
                //-- 添加ZipEntry，并ZipEntry中写入文件流，这里，加上i是防止要下载的文件有重名的导致下载失败
                zipOutStream.putNextEntry(new ZipEntry(file.getName()));
                zipOutStream.write(data);
                filenputStream.close();
                zipOutStream.closeEntry();
            }
        }
    }

    /**
     * 下载文件
     *
     * @param outputStream 下载输出流
     * @param zipFilePath 需要下载文件的路径
     * @author hongwei.lian
     * @date 2018年9月7日 下午3:27:08
     */
    public static void downloadFile(OutputStream outputStream, String zipFilePath) throws IOException {
        File zipFile = new File(zipFilePath);
        if (!zipFile.exists()) {
            //-- 需要下载压塑包文件不存在
            return ;
        }
        try (FileInputStream inputStream =  new FileInputStream(zipFile)) {
            byte[] data = new byte[(int) zipFile.length()];
            inputStream.read(data);
            outputStream.write(data);
            outputStream.flush();
        }
    }


}
