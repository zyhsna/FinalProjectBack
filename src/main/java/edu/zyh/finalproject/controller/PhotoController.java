package edu.zyh.finalproject.controller;

import edu.zyh.finalproject.service.PhotoService;
import edu.zyh.finalproject.util.JSONData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author zyhsna
 */
@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController {
    @Autowired
    private PhotoService photoService;


    @RequestMapping(value = "/pri/uploadImage")
    public JSONData uploadImage(@RequestParam(value = "file") MultipartFile image, @RequestParam("userId") int userId,
                                @RequestParam("taskId") int taskId) {
        int upload = photoService.checkUserUpload(taskId, userId);
        if (upload == 0) {
            int result = photoService.uploadImage(image, userId, taskId);
            if (result == 0) {
                return JSONData.buildSuccess();
            } else {
                return JSONData.buildError(21, "图片上传失败");
            }
        } else {
            return JSONData.buildError(28, "重复上传！");
        }
    }

    @RequestMapping(value = "/pri/downloadImage", produces = {"application/json", "application/xml"})
    public ResponseEntity<FileSystemResource> downloadImage(@RequestBody Map<String, Integer> params) {
        Integer taskId = params.get("taskId");
        Integer userId = params.get("userId");
        ResponseEntity<FileSystemResource> imageResource = photoService.downloadImage(taskId, userId);
        return imageResource;
    }

    /**
     * 判断用户是否上传了照片
     *
     * @param params 数据
     * @return 0 上传了其他没有
     */
    @RequestMapping(value = "/pri/checkUserUpload", produces = {"application/json", "application/xml"})
    public JSONData checkUserUpload(@RequestBody Map<String, Integer> params) {
        Integer taskId = params.get("taskId");
        Integer userId = params.get("userId");
        int result = photoService.checkUserUpload(taskId, userId);
        return result > 0 ? JSONData.buildSuccess() : JSONData.buildError(26, "用户未上传图片");
    }


}
