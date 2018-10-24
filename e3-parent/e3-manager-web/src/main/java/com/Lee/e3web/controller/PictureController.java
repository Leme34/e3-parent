package com.Lee.e3web.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传处理Controller
 */
@Controller
public class PictureController {

    @Value("${imageServerIP}")
    private String IMAGE_SERVER_IP;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @RequestMapping(value = "/pic/upload",produces= MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")  //解决浏览器json字符串的兼容性问题
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile) {  //与前端请求参数名相同
        Gson gson = new Gson();
        try {
        //1、把图片上传的图片服务器
        //取文件扩展名
        String originalFilename = uploadFile.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        StorePath storePath = fastFileStorageClient.uploadFile(uploadFile.getInputStream(), uploadFile.getSize(), extName, null);

        //2、富文本编辑器kingeditor文档规定图片上传成功需要返回error值和图片的url
        //补充图片完整的url (http://192.168.11.101/group1/M00/00/00/xxx.jpg)
        String fileUrl = IMAGE_SERVER_IP + storePath.getGroup() + "/" +  storePath.getPath();
        System.out.println(fileUrl);
        Map result = new HashMap<>();
        result.put("error", 0);
        result.put("url", fileUrl);
        //返回String类型的json字符串解决浏览器json字符串的兼容性问题
        return gson.toJson(result);

        } catch (IOException e) {
            e.printStackTrace();
            Map result = new HashMap<>();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return gson.toJson(result);
        }



    }

}
