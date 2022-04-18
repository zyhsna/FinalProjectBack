package edu.zyh.finalproject.controller;

import edu.zyh.finalproject.POJO.CheckUploadImage;
import edu.zyh.finalproject.POJO.Task;
import edu.zyh.finalproject.service.TaskService;
import edu.zyh.finalproject.util.JSONData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 任务controller，用来处理任务分发和完成的相关逻辑
 *
 * @author zyhsna
 */
@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;


    @RequestMapping("/getUnFinishedTask")
    public JSONData getUnFinishedTaskList(int userId) {
        List<Task> unFinishedTaskList = taskService.getUnFinishedTaskList(userId);
        if (unFinishedTaskList == null) {
            return JSONData.buildError(12, "获取未完成任务列表失败");
        }
        return JSONData.buildSuccess(unFinishedTaskList);
    }


    /**
     * 用户结束任务
     *
     * @param userId 用户Id
     * @param taskId 任务ID
     * @return JSON
     */
    @RequestMapping("/finishTask")
    public JSONData finishTask(int userId, int taskId) {
        int result = taskService.finishTask(userId, taskId);

        if (result == 1) {
            return JSONData.buildSuccess();
        } else {
            return JSONData.buildError(25, "结束任务失败");
        }
    }

    @RequestMapping("/pri/insertNewTask")
    public JSONData insertNewTask(@RequestBody Task task) {
        int result = taskService.insertNewTask(task);
        if (result == 1) {
            return JSONData.buildSuccess();
        } else if (result == -1) {
            return JSONData.buildError(25, "用户积分不够");
        } else {
            return JSONData.buildError(13, "创建任务失败");
        }
    }

    @RequestMapping("/getTaskByTaskId")
    public JSONData getTaskByTaskId(int taskId) {
        Task taskByTaskId = taskService.getTaskByTaskId(taskId);
        if (taskByTaskId == null) {
            return JSONData.buildError(13, "获取任务详情失败");
        } else {
            return JSONData.buildSuccess(taskByTaskId);
        }
    }

    /**
     * 用户接取任务
     *
     * @param taskId 任务ID
     * @param userId 接取任务的用户ID
     * @return 15 接取人数上限,14 接取失败，0成功
     */
    @RequestMapping("/pri/receiveTask")
    public JSONData receiveTask(int taskId, int userId) {
        int result = taskService.receiveTask(taskId, userId);
        int RECEIVER_NUM_EXCESS = 2;
        if (result == 1) {
            return JSONData.buildSuccess();
        } else if (result == RECEIVER_NUM_EXCESS) {
            return JSONData.buildError(15, "接取人数已达上限");
        }
        return JSONData.buildError(14, "接取任务失败");
    }

    @RequestMapping("/checkTaskReceive")
    public JSONData checkReceive(int userId, int taskId) {
        int result = taskService.checkReceive(userId, taskId);
        if (result > 0) {
            return JSONData.buildSuccess();
        } else {
            return JSONData.buildError(18, "获取是否接取任务失败");
        }
    }


    @RequestMapping("/getPublishTaskByUserId")
    public JSONData getPublishTaskByUserId(int userId) {
        List<Task> taskList = taskService.getPublishTaskByUserId(userId);
        if (taskList == null) {
            return JSONData.buildError(23, "获取发布任务失败");
        } else {
            return JSONData.buildSuccess(taskList);
        }
    }

    /**
     * 获取参与的拍卖关联的任务详情
     * @param userId 用户ID
     * @return List< Task >
     */
    @RequestMapping("/getReceiveTaskByUserId")
    public JSONData getReceiveTaskByUserId(int userId) {
        List<Task> taskList = taskService.getReceiveTaskByUserId(userId);
        if (taskList == null) {
            return JSONData.buildError(24, "获取接取任务失败");
        } else {
            return JSONData.buildSuccess(taskList);
        }
    }

    @RequestMapping("/getUploadUserList")
    public JSONData getUploadUserList(int taskId) {
        List<CheckUploadImage> uploadUserList = taskService.getUploadUserList(taskId);
        if (taskService != null) {
            return JSONData.buildSuccess(uploadUserList);
        } else {
            return JSONData.buildError(24, "获取上传图片用户列表失败");
        }
    }
}
