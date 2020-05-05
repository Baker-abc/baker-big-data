package com.baker.learning.bigdatacommons.hdfs;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @date 2020/5/5 12:48
 */
@Slf4j
@AllArgsConstructor
public class HadoopClient {
    private FileSystem fs;


    /**
     * 创建文件夹
     *
     * @param folderPath
     * @return
     */
    public boolean mkdir(String folderPath) {
        log.info("【开始创建目录】 文件夹路径名称: {}", folderPath);
        boolean flag = false;
        if (StringUtils.isEmpty(folderPath)) {
            throw new IllegalArgumentException("folder不能为空");
        }
        try {
            Path path = new Path(folderPath);
            if (!fs.exists(path)) {
                flag = fs.mkdirs(path);
            }
            if (fs.getFileStatus(path).isDirectory()) {
                flag = true;
            }
        } catch (Exception e) {
            log.error("【创建目录失败】", e);
        }
        return flag;
    }

    /**
     * 文件上传
     *
     * @param delSrc    指是否删除源文件，true为删除，默认为false
     * @param overwrite 是否覆盖
     * @param srcFile   源文件，上传文件路径
     * @param destPath  fs的目标路径
     */
    public void copyFileToHDFS(boolean delSrc, boolean overwrite, String srcFile, String destPath) throws IOException {
        log.info("【文件上传】 开始上传, 上传文件路径: {}", srcFile);
        Path srcPath = new Path(srcFile);
        // 目标路径
        Path dstPath = new Path(destPath);
        try {
            // 文件上传
            fs.copyFromLocalFile(delSrc, overwrite, srcPath, dstPath);
        } catch (IOException e) {
            log.error("【文件上传失败】", e);
            throw e;
        }
    }


    /**
     * 从hdfs下载文件
     *
     * @param path
     * @param downloadPath
     * @throws IOException
     */
    public void copyHDFSToLocal(String path, String downloadPath) throws IOException {
        log.info("【下载文件】 开始下载, 下载文件名称: {}", path);
        // 下载源文件路径
        Path clientPath = new Path(path);
        // 目标路径
        Path serverPath = new Path(downloadPath);
        // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
        fs.copyToLocalFile(false, clientPath, serverPath);
    }


    /**
     * 删除文件或者目录
     *
     * @param path
     * @return
     * @throws IOException
     */
    public boolean delete(String path) throws IOException {
        log.info("【删除文件】 开始删除, 删除文件的路径:{}", path);
        if (StringUtils.isEmpty(path)) {
            return false;
        }

        Path srcPath = new Path(path);
        return fs.deleteOnExit(srcPath);
    }

    /**
     * 获取目录下文件列表
     *
     * @param path 目录路径
     * @return {@link List}
     */
    public List<Map<String, String>> getFileList(String path) {
        log.info("【获取目录下文件列表】 开始获取, 目录路径: {}", path);
        List<Map<String, String>> list = new ArrayList<>();
        try {
            // 递归找到所有文件
            RemoteIterator<LocatedFileStatus> filesList = fs.listFiles(new Path(path), true);
            while (filesList.hasNext()) {
                LocatedFileStatus next = filesList.next();
                String fileName = next.getPath().getName();
                Path filePath = next.getPath();
                Map<String, String> map = new HashMap<>();
                map.put("fileName", fileName);
                map.put("filePath", filePath.toString());
                list.add(map);
            }
        } catch (IOException e) {
            log.info("【获取目录下文件列表】异常: {}", e);
        }
        return list;
    }

    /**
     * 读取文件内容
     *
     * @param filePath 文件路径
     */
    public String readFile(String filePath) throws IOException {
        log.info("【读取文件内容】 开始读取, 文件路径: {}", filePath);
        Path newPath = new Path(filePath);
        StringBuilder buffer = new StringBuilder();
        try (InputStream in = fs.open(newPath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {

            String line; // 用来保存每行读取的内容
            line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                buffer.append("\n");
                line = reader.readLine();
            }
        }
        return buffer.toString();
    }

    /**
     * 文件或文件夹重命名
     *
     * @param oldName 旧文件或旧文件夹名称
     * @param newName 新文件或新文件夹名称
     * @return 是否更改成功 true: 成功/false: 失败
     */
    public boolean renameFile(String oldName, String newName) {
        log.info("【文件或文件夹重命名】 开始重命名, 旧文件或旧文件夹名称: {}, 新文件或新文件夹名称: {} ", oldName, newName);
        boolean isOk = false;
        Path oldPath = new Path(oldName);
        Path newPath = new Path(newName);
        try {
            // 更改名称
            isOk = fs.rename(oldPath, newPath);
        } catch (IOException e) {
            log.info("【文件或文件夹重命名】异常", e);
        }
        return isOk;
    }

    /**
     * 复制文件
     *
     * @param sourcePath 复制路径
     * @param targetPath 目标路径
     */
    public void copyFile(String sourcePath, String targetPath) throws IOException {
        log.info("【复制文件】 开始复制, 复制路径: {}, 目标路径: {}", sourcePath, targetPath);
        // 原始文件路径
        Path oldPath = new Path(sourcePath);
        // 目标路径
        Path newPath = new Path(targetPath);
        try (FSDataInputStream inputStream = fs.open(oldPath); FSDataOutputStream outputStream = fs.create(newPath)) {
            IOUtils.copyBytes(inputStream, outputStream, 1024 * 1024 * 64, false);
        }
    }
}
