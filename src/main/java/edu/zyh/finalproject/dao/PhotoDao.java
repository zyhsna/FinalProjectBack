package edu.zyh.finalproject.dao;

import edu.zyh.finalproject.util.JSONData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 用来存储用户上传的照片路径
 * @author zyhsna
 */
@Mapper
public interface PhotoDao {

    /**
     * 用户上传照片后在数据库中保存路径
     * @param taskId 任务ID
     * @param publisherId 上传的用户ID
     * @param photoName 照片名字
     * @return 1 新增正常
     */
    @Insert("insert into photo(task_id,receiver_id, photo_name) value (#{taskId}, #{publisherId}, #{photoName})")
    int addNewPhoto(int taskId, int publisherId, String photoName);

    /**
     * 根据相关信息获取用户上传图片的名字
     * @param taskId 任务ID
     * @param receiverId 上传照片的用户ID
     * @return 图片名字
     */
    @Select("select photo_name from photo where task_id = #{taskId} and receiver_id = #{receiverId}")
    String getPhotoName(int taskId, int receiverId);

    /**
     * 判断用户是否上传了照片
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 1 上传了 其他未上传
     */
    @Select(" select count(*) from photo where task_id = #{taskId} and receiver_id = #{userId}")
    int checkUserUpload(Integer taskId, Integer userId);
}
