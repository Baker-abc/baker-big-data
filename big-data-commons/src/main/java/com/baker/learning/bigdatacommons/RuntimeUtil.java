package com.baker.learning.bigdatacommons;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * @description: cmd 工具类
 * @create: 2018-05-17 13:55
 * @Version: 1.0
 **/
@Slf4j
public class RuntimeUtil {


    /**
     * @param cmd cmd脚本命令
     * @return
     * @throws Exception
     */
    public static CmdResultVO executeCmd(String cmd) throws Exception {
        return executeCmd(cmd, null);
    }

    /**
     * @param cmd      cmd脚本命令
     * @param filePath 需要进入到指定的文件下下
     * @return
     * @throws Exception
     */
    public static CmdResultVO executeCmd(String cmd, String filePath) throws Exception {
        CmdResultVO cmdResultVO = new CmdResultVO();
        //TODO
        log.info(cmd);
        String result = "";
        String errorResult = "";
        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        Long currentTime = System.currentTimeMillis();
        try {
            if (filePath != null) {
                log.info(filePath);
                process = runtime.exec(cmd, null, new File(filePath));
            } else {
                process = runtime.exec(cmd);
            }

            Thread outputThread = new ProcessOutputThread(process.getInputStream());
            Thread errorOutputThread = new ProcessOutputThread(process.getErrorStream());
            outputThread.start();
            errorOutputThread.start();
            process.waitFor(60, TimeUnit.SECONDS);
            log.info("执行cmd外部程序的线程结束！");
            outputThread.join(10 * 1000);      //等待线程10s
            errorOutputThread.join(10 * 1000);
            if (outputThread.isAlive()) {
                outputThread.stop();
            }
            if (errorOutputThread.isAlive()) {
                errorOutputThread.stop();
            }
            result = ((ProcessOutputThread) outputThread).getOutResult();
            errorResult = ((ProcessOutputThread) errorOutputThread).getOutResult();
            log.info("errorResult:" + errorResult);
            process.destroy();

        } finally {
            if (process != null) {
                process.getErrorStream().close();
                process.getInputStream().close();
                process.getOutputStream().close();
            }
            log.info("执行cmd结束！");
        }
        Long taskTimeConsuming = System.currentTimeMillis() - currentTime;  //执行外部程序耗时

        cmdResultVO.setResult(result);
        cmdResultVO.setErrorResult(errorResult);
        cmdResultVO.setTaskTimeConsuming(taskTimeConsuming + "ms");

        return cmdResultVO;
    }

}
