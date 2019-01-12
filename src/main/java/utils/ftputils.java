package utils;


import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class ftputils {




    private  static String ftpip= propertyutils.getProperty("ftp.server.ip");
    private static String ftpuser =propertyutils.getProperty("ftp.user");
    private static String ftpass =propertyutils.getProperty("ftp.pass");

   public ftputils(String ip,int port,String user,String pwd){
       this.ip=ip;
       this.port=port;
       this.pwd=pwd;
       this.user=user;
   }

    private String ip;
    private  int port;
    private String user;
    private  String pwd;
    private FTPClient ftpclient;



    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return pwd;
    }

    public void setPassword(String password) {
        this.pwd = pwd;
    }

    public FTPClient getFrpclient() {
        return ftpclient;
    }

    public void setFrpclient(FTPClient ftpclient) {
        this.ftpclient = ftpclient;
    }



    private boolean uploadfile(String remotepath, List<File> fileList) throws  IOException{
        boolean upload =true;
        FileInputStream fis=null;
        if (connectserver(this.ip,this.port,this.user,this.pwd)){
            try {
                ftpclient.changeWorkingDirectory(remotepath);
                ftpclient.setBufferSize(1024);
                ftpclient.setFileType(ftpclient.BINARY_FILE_TYPE);
                ftpclient.enterLocalActiveMode();
                for (File fileitem:fileList){
                    fis=new FileInputStream(fileitem);
                    ftpclient.storeFile(fileitem.getName(),fis)
                }
            } catch (IOException e) {
              log.error("shangchuanwenjianshibai ");
                upload=false;
                e.printStackTrace();
            }finally {
                fis.close();
                ftpclient.disconnect();
            }
        }
        return upload;
    }


    private boolean uploadfile(List<File> fileList) throws IOException {
        ftputils ftputils = new ftputils(ftpip,21,ftpuser,ftpass);
        log.info("kaishixuanzeftpfuwuqi");
        boolean result = ftputils.uploadfile("img",fileList);
        log.info("kaishilianjieftpfuwuqi,jieshushangchuan,shangchaunjieguo:{}");
        return result;


    }



    private boolean connectserver (String ip, int port, String user,String pwd){
        boolean issuccess= false;
       ftpclient = new FTPClient();
        try {
            issuccess=ftpclient.login(user,pwd);
        }catch (IOException e){
            log.error("lianjieftpfuwuqiyichang",e);
        }
        return  issuccess;
    }



}
