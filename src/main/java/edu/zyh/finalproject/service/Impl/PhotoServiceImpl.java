package edu.zyh.finalproject.service.Impl;

import edu.zyh.finalproject.dao.PhotoDao;
import edu.zyh.finalproject.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author zyhsna
 */
@Service
public class PhotoServiceImpl implements PhotoService {
    @Autowired
    private PhotoDao photoDao;

    @Override
    public int uploadImage(MultipartFile image, int userId, int taskId) {
        String path = "src/main/image";
        File file = new File(path);
        if (!file.exists()) {
            boolean mkdir = file.mkdir();
        }
        //
        String fileName = UUID.randomUUID() + ".jpg";
        try {
            image.transferTo(new File(path, fileName));
            int result = photoDao.addNewPhoto(taskId, userId, fileName);
            if (result == 1) {
                //增加成功
                return 0;
            }
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public ResponseEntity<FileSystemResource> downloadImage(int taskId, int userId) {
        String photoName = photoDao.getPhotoName(taskId, userId);
        if (photoName == null|| " ".equals(photoName)||"".equals(photoName)){
            return null;
        }
        String path = "src/main/image";
        File file = Paths.get(path).resolve(photoName).toFile();
        if (!file.exists() || !file.canRead()){
            return null;
        }else {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new FileSystemResource(file));
        }

    }

    @Override
    public int checkUserUpload(Integer taskId, Integer userId) {
        int result = photoDao.checkUserUpload(taskId, userId);
        return result>0 ? 1 : 0;
    }

}
