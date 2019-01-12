package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utils.ftputils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import utils.ftputils;

@Slf4j
@Service("fileservice")
public class fileserviceimp implements fileservice {

private String upload(MultipartFile file,String path){
    String filename= file.getOriginalFilename();
    String fileextionname =filename.substring(filename.lastIndexOf(".")+1);
    String uploadfilename = UUID.randomUUID().toString()+"."+fileextionname;
   log.info("开始上传文件，上传文件的文件名：{},上传的路经：{}，新文件名：{}",filename,path,uploadfilename);


    File filedir =new File(path);
    if (!filedir.exists()){
        filedir.setWritable(true);
        //mkdir是当前这个级别的  mkdirs是
        filedir.mkdirs();

    }

    File targetfile = new File(path,uploadfilename);

        try {
            file.transferTo(targetfile);
            ftputils.;
            //文件上传成功
            //todo  将targetfile 上传到ftp服务器
            //上传之后删除upload 下面的文件夹
            ftputils
        } catch (IOException e){

logger.info("文件上传异常"，e);

}

}
