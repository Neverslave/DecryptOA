package com.ydnet;

import org.apache.commons.logging.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * @author zhu
 * @date 2020-11-11 9:26
 */
public class Decrypt {

    public void decryptFile(String filePath,String resultPath) throws Exception{
        File file = new File(filePath);
        CoderFactory coderFactory = CoderFactory.getInstance();
        File result =coderFactory.decryptFileToTemp(file,resultPath);
        System.out.println("-----------转换完成------");
        System.out.println("目的文件为  "+result.getAbsolutePath());
    }
}
