package edu.zyh.finalproject.dao;

import edu.zyh.finalproject.POJO.CheckUploadImage;
import edu.zyh.finalproject.POJO.Task;
import edu.zyh.finalproject.POJO.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * TaskDao 用来处理相关Task数据接口
 *
 * @author zyhsna
 */
@Mapper
public interface TaskDao {
    /**
     * Dao层 获得未完成的任务列表
     *
     * @return list (未完成的任务)
     */
    @Select("SELECT * from task where accomplish_flag = 0")
    List<Task> getUnFinishedTask();


    /**
     * Dao层 用户确认结束task， userId感觉没啥用
     *
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 1成功
     */
    @Update("update task set accomplish_flag = 1 where task_id = #{taskId}")
    int finishTask(int userId, int taskId);


    /**
     * 根据任务ID获得任务信息
     *
     * @param taskId 任务ID
     * @return Task
     */
    @Select("select * from task where task_id = #{taskId}")
    Task getTaskByTaskId(int taskId);


    /**
     * 创建新任务
     *
     * @param task Task类型的新建任务
     * @return 1 创建成功
     */
    @Insert("insert into task(publisher_id, device_id, receive_flag, accomplish_flag, " +
            "receiver_num, task_level, task_info, task_name, task_deadline, max_receiver,create_time,open,target_user_id) " +
            "VALUE (#{task.publisherId},#{task.deviceId},#{task.receiveFlag},#{task.accomplishFlag}," +
            "#{task.receiverNum},#{task.taskLevel},#{task.taskInfo},#{task.taskName}," +
            "#{task.taskDeadline},#{task.maxReceiver},#{task.createTime},#{task.open},#{task.targetUserId})")
    @Options(useGeneratedKeys = true, keyColumn = "task_id", keyProperty = "taskId")
    int insertNewTask(@Param("task") Task task);

    /**
     * 用户接受任务
     *
     * @param taskId 任务id
     * @param userId 用户id
     * @return 1接取成功， 其他失败
     */
    @Insert("insert into bill_receiver(receiver_id,task_id,revenue) value(#{userId}, #{taskId}, 0)")
    int receiveTask(int taskId, int userId);

    /**
     * 用户接取任务之后增加接取任务的人数
     *
     * @param taskId 任务ID
     */
    @Update("update task set receive_flag = 1, receiver_num = receiver_num + 1 where task_id = #{taskId}")
    void increaseTaskReceiveNum(int taskId);

    /**
     * Dao层
     *
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 1 接取过 0 未接取
     */
    @Select("select count(*) from bill_receiver where task_id = #{taskId} and receiver_id = #{userId};")
    int checkReceive(int userId, int taskId);

    /**
     * 根据用户ID来查询已发布任务
     *
     * @param userId 用户ID
     * @return List
     */
    @Select("select * from task where publisher_id = #{userId}")
    List<Task> getPublishTaskByUserId(int userId);


    /**
     * 根据用户ID查询参与的拍卖
     *
     * @param userId 用户ID
     * @return List
     */
    @Select("select *" +
            "from task where task_id in (  select task_id  from auction_detail where bidder_id = #{userId}) ")
    List<Task> getReceiveTaskByUserId(int userId);

    /**
     * 根据任务ID来获取已经接取任务用户ID
     *
     * @param taskId 任务ID
     * @return List
     */

    @Select("select user_id,user_name from user where user_id in ( select receiver_id from bill_receiver  where task_id = #{taskId});")
    List<User> getUploadUserList(int taskId);

    @Select("select distance from bill_receiver where receiver_id = #{userId} and task_id = #{taskId}")
    double getDistanceByTaskIdAndUserId(int userId,int taskId);
}
