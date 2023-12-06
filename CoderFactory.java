package com.ydnet;

import com.seeyon.ctp.common.AppContext;
import com.seeyon.ctp.common.config.SystemConfig;
import com.seeyon.ctp.common.encrypt.ICoder;
import com.seeyon.ctp.common.encrypt.TV01Coder;
import com.seeyon.ctp.common.encrypt.TV02Coder;
import com.seeyon.ctp.common.encrypt.TV03Coder;
import com.seeyon.ctp.common.security.SecurityHelper;
//import com.seeyon.ctp.util.FileUtil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.IOUtils;

public class CoderFactory {
    private final String KEY = "62C20D8F29A243D3";
    private static final CoderFactory factory = new CoderFactory();
    private Map<String, Object> fileLocks = new ConcurrentHashMap();
    private Object dLock = new Object();

    private CoderFactory() {
    }

    public static CoderFactory getInstance() {
        return factory;
    }

    public String getEncryptVersion() {
        String encryptVersion = null;
        SystemConfig systemConfig = (SystemConfig)AppContext.getBean("systemConfig");
        String configItem = systemConfig.get("attach_encrypt");
        if (configItem != null && !"no".equals(configItem)) {
            if ("middle".equals(configItem)) {
                encryptVersion = "V02";
            } else {
                encryptVersion = "V01";
            }

            if (SecurityHelper.isGmEnabled()) {
                encryptVersion = "V03";
            }
        } else {
            encryptVersion = "no";
        }

        return encryptVersion;
    }

    public void download(InputStream in, OutputStream out) throws Exception {
        byte[] temp = new byte["seeyon attachment encrypt v01".getBytes(StandardCharsets.ISO_8859_1).length];
        int count = in.read(temp);
        String head = new String(temp);
        ICoder code = this.getICoder(head);
        if (code != null) {
            code.decode(in, out);
        } else {
            if (count > 0) {
                out.write(temp);
            }

            this.directOutput(in, out);
        }

    }

    private ICoder getICoder(String head) {
        ICoder code = null;
        if ("seeyon attachment encrypt v01".equals(head)) {
             code = new TV01Coder();
            code.initKey("62C20D8F29A243D3");
            return code;
        } else if ("seeyon attachment encrypt v02".equals(head)) {
             code = new TV02Coder();
            code.initKey("62C20D8F29A243D3");
            return code;
        } else if ("seeyon attachment encrypt v03".equals(head)) {
            code = new TV03Coder();
            code.initKey("62C20D8F29A243D3");
            return code;
        } else {
            return null;
        }
    }

    public String download(byte[] buffer) throws Exception {
        byte[] temp = new byte["seeyon attachment encrypt v01".getBytes(StandardCharsets.ISO_8859_1).length];
        if (buffer.length - temp.length > 0) {
            byte[] temp1 = new byte[buffer.length - temp.length];
            System.arraycopy(buffer, 0, temp, 0, temp.length);
            System.arraycopy(buffer, temp.length, temp1, 0, temp1.length);
            String head = new String(temp);
            ICoder code = this.getICoder(head);
            if (code != null) {
                return code.decodeStr(temp1);
            }
        }

        return new String(buffer);
    }

    public void upload(InputStream in, OutputStream out, String version) throws Exception {
        Class coderClass = Class.forName("com.seeyon.ctp.common.encrypt.T" + version.toUpperCase() + "Coder");
        ICoder coder = (ICoder)coderClass.newInstance();
        coder.initKey("62C20D8F29A243D3");
        coder.encode(in, out);
    }

/*    public String getFileToString(String realPath) throws Exception {
        FileInputStream fis = new FileInputStream(realPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        this.download(fis, baos);
        fis.close();
        ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
        String encoding = FileUtil.detectEncoding(bis);
        if (encoding != null) {
            return !"KOI8-R".equalsIgnoreCase(encoding) && !"WINDOWS-1252".equals(encoding) && !"IBM855".equals(encoding) ? baos.toString(encoding) : baos.toString("GB18030");
        } else {
            return baos.toString();
        }
    }*/

    private void directOutput(InputStream in, OutputStream out) throws Exception {
        int count = 0;
        byte[] temp = new byte[8192];

        while(count >= 0) {
            count = in.read(temp);
            if (count > 0) {
                out.write(temp, 0, count);
            }
        }

        in.close();
        out.close();
    }

/*    public String decryptFileToTemp(String srcFilePath) throws Exception {
        return this.decryptFileToTemp(new File(srcFilePath)).getAbsolutePath();
    }*/

    public File decryptFileToTemp(File srcFile,String dectFilePath) throws Exception {
        if (!srcFile.exists()) {
            return srcFile;
        } else {
           // String dectFilePath = SystemEnvironment.getSystemTempFolder() + File.separator + srcFile.getName() + "D" + srcFile.lastModified();
            //String dectFilePath = "C:\\Users\\10291\\Desktop\\1";
            File dectFile = new File(dectFilePath);
            if (dectFile.exists()) {
                return dectFile;
            } else {
                Object fileLock = null;
                Object var5 = this.dLock;
                synchronized(this.dLock) {
                    fileLock = this.fileLocks.get(dectFilePath);
                    if (fileLock == null) {
                        fileLock = new Object();
                        this.fileLocks.put(dectFilePath, fileLock);
                    }
                }

                synchronized(fileLock) {
                    if (dectFile.exists()) {
                        return dectFile;
                    } else {
                        InputStream in = null;
                        FileOutputStream out = null;

                        File var11;
                        try {
                            in = new FileInputStream(srcFile);
                            byte[] temp = new byte["seeyon attachment encrypt v01".getBytes("ISO-8859-1").length];
                            in.read(temp);
                            String head = new String(temp);
                            ICoder code = this.getICoder(head);
                            if (code == null) {
                                var11 = srcFile;
                                return var11;
                            }

                            if (code instanceof TV03Coder) {
                                in.close();
                                in = new FileInputStream(srcFile);
                            }

                            out = new FileOutputStream(dectFile);
                            code.decode(in, out);
                            var11 = dectFile;
                        } catch (Exception var18) {
                            throw var18;
                        } finally {
                            this.fileLocks.remove(dectFilePath);
                            IOUtils.closeQuietly(in);
                            IOUtils.closeQuietly(out);
                        }

                        return var11;
                    }
                }
            }
        }
    }

    public void encryptFile(String srcFile, String dectFile) throws Exception {
        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(dectFile);
            String encryptVersion = this.getEncryptVersion();
            if ("no".equals(encryptVersion)) {
                IOUtils.copy(in, out);
            } else {
                this.upload(in, out, encryptVersion);
            }
        } catch (Exception var9) {
            throw var9;
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
        }

    }
}
