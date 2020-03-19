/**
 *
 */
package com.baker.learning.bigdatacommons;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * @description: 文件操作工具类
 * @create: 2018-05-14 17:55
 * @Version: 1.0
 **/
@Slf4j
public class FileUtil {
    /**
     * 根据路径 递归获取路径下全部文件路径（包含子文件夹下）
     *
     * @param path
     * @return
     * @throws Exception
     */
    public static ArrayList<String> getFileNameList(String path) throws Exception {
        //目标集合fileList
        ArrayList<File> fileListStatic = new ArrayList<File>();
        getFiles(fileListStatic, path);
        ArrayList<String> iconNameList = new ArrayList<String>();//返回文件名数组
        for (int i = 0; i < fileListStatic.size(); i++) {
            String curpath = fileListStatic.get(i).getPath();//获取文件路径
            curpath = curpath.replace("\\", "/");
            iconNameList.add(curpath);
        }
        return iconNameList;

    }

    /**
     * 根据路径 递归获取路径下全部文件file（包含子文件夹下）
     *
     * @param fileListStatic
     * @param path
     * @throws Exception
     */
    private static void getFiles(ArrayList<File> fileListStatic, String path) throws Exception {

        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File fileIndex : files) {
                //如果这个文件是目录，则进行递归搜索
                if (fileIndex.isDirectory()) {
                    getFiles(fileListStatic, fileIndex.getPath());
                } else {
                    //如果文件是普通文件，则将文件句柄放入集合中
                    fileListStatic.add(fileIndex);
                }
            }
        }
    }

    /**
     * 读取file内容
     *
     * @param file
     * @return
     */
    public static String getString(File file) throws IOException {
        return FileUtils.readFileToString(file, "utf-8");
    }


    /**
     * 删除文件或者文件夹
     *
     * @param fileName 文件名/文件夹名字
     * @return
     */
    public static void deleteTarget(String fileName) throws Exception {
        if (!FileUtils.deleteQuietly(new File(fileName))) {
            throw new RuntimeException("删除文件/文件夹异常：" + fileName);
        }
    }

    /**
     * 清空文件夹
     *
     * @param path 文件夹路径
     * @return
     */
    public static void cleanFolder(String path) throws Exception {
        if (!new File(path).exists()) {
            log.info("清空文件夹不存在");
        } else {
            FileUtils.cleanDirectory(new File(path));
        }
    }

    /**
     * 将整个文件夹包含里面的文件拷贝到目标文件夹里面
     *
     * @param srcDir
     * @param destDir
     * @throws Exception
     */
    public static void copyDirectoryToDirectory(File srcDir, File destDir) throws Exception {
        FileUtils.copyDirectoryToDirectory(srcDir, destDir);


    }

    /**
     * 将文件夹里面的内容拷贝到目标文件夹里面
     *
     * @param srcDir
     * @param destDir
     * @throws Exception
     */
    public static void copyDirectoryContentsToDir(File srcDir, File destDir) throws Exception {
        FileUtils.copyDirectory(srcDir, destDir);
    }

    /**
     * 复制文件到指定文件夹
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFileToDirectory(File srcFile, File destFile) throws IOException {
        FileUtils.copyFileToDirectory(srcFile, destFile);
    }

    /**
     * 拷贝文件
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void copyFileToFile(File srcFile, File destFile) throws IOException {
        FileUtils.copyFile(srcFile, destFile);
    }

    /**
     * 写入文件
     *
     * @param str      内容
     * @param filePath 文件
     * @return 保存路径
     * @throws Exception 异常
     */
    public static String writeStringToFile(String str, String filePath) throws Exception {
        FileUtils.writeStringToFile(new File(filePath), str, "utf-8");
        return filePath;
    }

    /**
     * 删除文件的空白行
     *
     * @param filePath    旧文件
     * @param newFilePath 新文件
     * @return 新文件
     * @throws Exception
     */
    public static String deleteSpace(String filePath, String newFilePath) throws Exception {
        File file = new File(filePath);
        InputStream is = null;
        BufferedReader br = null;
        String tmp;
        FileWriter writer = null;
        is = new BufferedInputStream(new FileInputStream(file));
        br = new BufferedReader(new InputStreamReader(is, "utf-8"));
        writer = new FileWriter(newFilePath, true);
        while ((tmp = br.readLine()) != null) {
            if (tmp.equals("")) ;
            else {
                writer.write(tmp + "\n");
            }
        }
        writer.close();
        is.close();
        return newFilePath;
    }

}
