package com.ydnet;


import java.util.Scanner;
/**
 *
 * @author zhu
 * */
public class Main {
    public static void main(String[] args) throws Exception {
        String code = "running";
        int type;
        CoderFactory coderFactory = CoderFactory.getInstance();
        while (!"exit".equals(code)){
            System.out.println("请输入需要的转换类型");
            System.out.println("1:解密文档");
            System.out.println("2:加密文档");
            Scanner scanner = new Scanner(System.in);
            type = scanner.nextInt();
            while (!(type==1||type==2)){
                System.out.println("1:解密文档");
                System.out.println("2:加密文档");
                System.out.println("请输入1或2");
                type = scanner.nextInt();
            }
            //解决nextInt 不读取/n
            scanner.nextLine();
            if(type ==1){
                Decrypt decrypt = new Decrypt();
                System.out.println("-----解密文档-------");
                System.out.println("请输入加密文件绝对路径如:C:\\Users\\admin\\Desktop\\-74545417167");
                String filePath = scanner.nextLine();
                System.out.println("请输入解密后文件的绝对路径包含名称：如C:\\Users\\admin\\Desktop\\-74545417167");
                String resultPath =scanner.nextLine();
                decrypt.decryptFile(filePath,resultPath);
                System.out.println("输入exit退出");
            }
            if(type==2){
                Encrypt encrypt = new Encrypt();
                String encryptCode = "V02";
                System.out.println("-----加密文档-------");
                System.out.println("请输入需要加密文件绝对路径如:C:\\Users\\admin\\Desktop\\-74545417167");
                String filePath = scanner.nextLine();
                System.out.println("请输入加密后文件的绝对路径包含名称：如C:\\Users\\admin\\Desktop\\-74545417167");
                String resultPath =scanner.nextLine();
                System.out.println("请输入加密规则：默认为V02,无需修改请回车");
                String inCode = scanner.nextLine();
                System.out.println(inCode);
                if(!"".equals(inCode)){
                    encryptCode = inCode;
                }
                encrypt.encryptFile(filePath,resultPath,encryptCode);
                System.out.println("输入exit退出");
            }

            code =scanner.nextLine();
        }

    }
}




