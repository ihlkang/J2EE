package com.ii.springboot.controller;

import com.ii.springboot.service.PictureUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/upload")
public class PictureUploadController {
    private static Logger log = LoggerFactory.getLogger(PictureUploadController.class);
    @Autowired
    PictureUploadService pictureUploadService;
    @RequestMapping("/to_upload")
    public String upload(){
        return "picture_upload";
    }
    @RequestMapping("/do_upload")
    @ResponseBody
    public String doUpload(@RequestParam("file") MultipartFile file){
        String s = pictureUploadService.resolve(file);
        log.debug(s);
        return s;
    }
}
