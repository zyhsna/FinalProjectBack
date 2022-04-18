package edu.zyh.finalproject.service;


import edu.zyh.finalproject.POJO.CheckUploadImage;
import edu.zyh.finalproject.POJO.Task;

import java.util.List;

/**
 * TaskService接口类
 * @author zyhsna
 */
public interface TaskService {
    /**
     * 获取未完成的任务列表
     * @return List {Task}
     * @param userId
     */
    List<Task> getUnFinishedTaskList(int userId);

    /**
     * 用户确认任务结束
     * @param userId 完成的用户ID
     * @param taskId 任务ID
     * @return 1 代表结束成功
     */
    int finishTask(int userId, int taskId);

    /**
     * 创建新的任务
     * @param task 任务
     * @return 1 创建成功 -1 积分不够  -2其他失败
     */
    int insertNewTask(Task task);

    /**
     * 根据taskId获取task信息
     * @param taskId 任务ID
     * @return task
     */
    Task getTaskByTaskId(int taskId);

    /**
     * 用户接取任务
     * @param taskId 任务ID
     * @param userId 接取任务的用户ID
     * @return 1成功 2 接取人数已达上限
     */
    int receiveTask(int taskId, int userId);

    /**
     * 用户判断是否接取该仍无
     * @param userId 用户ID
     * @param taskId 任务ID
     * @return 1 接取了，其他 未接取
     */
    int checkReceive(int userId, int taskId);


    /**
     * 更具用户ID来搜索该用户已经发布的任务
     * @param userId 用户ID
     * @return List
     */
    List<Task> getPublishTaskByUserId(int userId);

    /**
     * 根据用户ID来搜索该用户已经发布的任务
     * @param userId 用户ID
     * @return List
     */
    List<Task> getReceiveTaskByUserId(int userId);

    /**
     * 根据任务ID来获取已经接取任务用户ID
     * @param taskId 任务ID
     * @return List
     */

    List<CheckUploadImage> getUploadUserList(int taskId);
}
