package com.baker.learning.bigdatacommons;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Log4j2
/**
 * https://blog.csdn.net/yuanzihui/article/details/51093375
 * 为了解决Process.getInputStream()阻塞问题
 */
public class ProcessOutputThread extends Thread {
    private InputStream is;
    private List<String> outputList;

    public ProcessOutputThread(InputStream is) throws IOException {
        if (null == is) {
            throw new IOException("the provided InputStream is null");
        }
        this.is = is;
        this.outputList = new ArrayList<String>();
    }

    public String getOutResult() {
        StringBuffer sb = new StringBuffer();
        for (String message : outputList) {
            sb.append(message + "\r\n");
        }
        return sb.toString();
    }

    @Override
    public void run() {
        InputStreamReader ir = null;
        BufferedReader br = null;
        try {
            ir = new InputStreamReader(this.is);
            br = new BufferedReader(ir);
            String output = null;
            while (null != (output = br.readLine())) {
                this.outputList.add(output);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (null != br) {
                    br.close();
                }
                if (null != ir) {
                    ir.close();
                }
                if (null != this.is) {
                    this.is.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }
}
