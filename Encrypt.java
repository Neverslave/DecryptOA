package com.ydnet;

import java.io.*;

/**
 * @author zhu
 * @date 2020-11-11 9:18
 */
public class Encrypt {
    /**
     * 加密文件 生成加密文件
     * */
    public void encryptFile(String inFilePath,String outFilePath,String version)throws Exception {
        CoderFactory coderFactory =CoderFactory.getInstance();
        InputStream inputStream = new FileInputStream(inFilePath);
        OutputStream outputStream= new FileOutputStream(outFilePath);
        coderFactory.upload(inputStream,outputStream,version);
        System.out.println("-----加密完成-------");
        System.out.println("加密文件地址为 "+outFilePath);
    }
}
