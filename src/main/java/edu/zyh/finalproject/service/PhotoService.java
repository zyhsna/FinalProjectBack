package edu.zyh.finalproject.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zyhsna
 */
public interface PhotoService {

    /**
     * 用户上传图片
     * @param image MultipartFile类型数据图片
     * @param userId 上传的用户ID
     * @param taskId 任务ID
     * @return 1成功
     */
    int uploadImage(MultipartFile image,int userId,int taskId);

    /**
     * 用户下载图片
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return ResponseEntity<FileSystemResource>
     */
    ResponseEntity<FileSystemResource> downloadImage(int taskId, int userId);

    /**
     * 判断用户是否上传了照片
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 1 存在 其他不存在
     */
    int checkUserUpload(Integer taskId, Integer userId);
}
