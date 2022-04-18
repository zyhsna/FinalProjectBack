package edu.zyh.finalproject.service.Impl;

import edu.zyh.finalproject.POJO.Auction;
import edu.zyh.finalproject.POJO.CheckUploadImage;
import edu.zyh.finalproject.POJO.Task;
import edu.zyh.finalproject.POJO.User;
import edu.zyh.finalproject.dao.*;
import edu.zyh.finalproject.service.TaskService;
import edu.zyh.finalproject.util.GetDistance;
import edu.zyh.finalproject.util.JsonUtil;
import edu.zyh.finalproject.util.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.*;

/**
 * TaskService实现类
 *
 * @author zyhsna
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private DeviceDao deviceDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PointDao pointDao;

    @Autowired
    private ProgressDao progressDao;

    @Autowired
    private AuctionDao auctionDao;

    @Override
    public List<Task> getUnFinishedTaskList(int userId) {
        BoundHashOperations<String, Object, Object> unFinishedTaskOps = getUnFinishedTaskOps();
        List<Object> unFinishTaskList = unFinishedTaskOps.values();
        List<Task> tasks = new ArrayList<>();
        if (unFinishTaskList != null && unFinishTaskList.size() != 0) {
            //redis中有数据
            for (Object o : unFinishTaskList) {
                Task task = JsonUtil.jsonToPojo(JsonUtil.objectToJson(o), Task.class);
                if (task.getOpen() == 0) {
                    if (task.getTargetUserId() == userId) {
                        tasks.add(task);
                    }
                } else {
                    tasks.add(task);
                }
            }
            return tasks;
        }
        tasks = taskDao.getUnFinishedTask();
        //更新到redis中
        for (Task task : tasks) {
            updateUnfinishedTaskToRedis(unFinishedTaskOps, task);
        }
        return tasks;
    }

    @Override
    public int finishTask(int userId, int taskId) {
        int result = taskDao.finishTask(userId, taskId);

        Task taskByTaskId = taskDao.getTaskByTaskId(taskId);
        int taskLevel = taskByTaskId.getTaskLevel();
        long createTime = Long.parseLong(taskByTaskId.getCreateTime());
        Date date = new Date();
        long completeTime = (date.getTime() - createTime) / 1000 / 60;
        double distance = taskDao.getDistanceByTaskIdAndUserId(userId, taskId);

        int accomplishmentLevel = SendMessage.getAccomplishmentLevel(taskLevel, completeTime, distance);

//        accomplishmentLevel = 2;

        double percent = 0;
        switch (accomplishmentLevel) {
            case 3:
                percent = 0.9;
                break;
            case 2:
                percent = 0.85;
                break;
            case 1:
                percent = 0.8;
                break;
            default:
                percent = 0.75;
                break;
        }

        User personInfo = userDao.getPersonInfo(userId);
        int userLevel1 = personInfo.getUserLevel();
        for (int i = 0; i < 7; i++) {
            if (i == userLevel1) {
                percent *= 1 - (6 - i) * 0.05;
                break;
            }
        }

        double revenue = taskLevel * 25 * percent;
        int progress = taskLevel * 20;
        pointDao.increasePointByUserId(userId, revenue);
        progressDao.increaseProgressByUserId(userId, progress);
        double userProgress = progressDao.getProgressByUserId(userId);
        int userLevel = 1;
        for (; userLevel < 7; userLevel++) {
            userProgress = userProgress - 100 * userLevel;
            if (userProgress < 0) {
                break;
            }
        }
        int updateUserLevelResult = progressDao.setUserLevel(userId, userLevel);


        if (result == 1) {

            //从redis中删除
            deleteUnfinishedTaskFromRedis(taskId);
            //更新到redis中，已完成的项目
            updateFinishedTaskToRedis(getFinishedTaskOps(), taskDao.getTaskByTaskId(taskId));
            //找回更新设备状态
            deviceDao.findDeviceBack(taskByTaskId.getDeviceId());
            auctionDao.closeAuctionByTaskId(taskId);
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int insertNewTask(Task task) {

        //设置创建时间
        Date date = new Date();
        task.setCreateTime(String.valueOf(date.getTime()));
        int deviceId = task.getDeviceId();
        int price = deviceDao.getDevicePrice(deviceId);
        String currentDeviceHardwareAddress = task.getCurrentDeviceHardwareAddress();
        int deviceIdByHardwareAddress = deviceDao.getDeviceIdByHardwareAddress(currentDeviceHardwareAddress);


        double currentDeviceLastLatitude = deviceDao.getDeviceLastLatitude(deviceIdByHardwareAddress);
        double currentDeviceLastLongitude = deviceDao.getDeviceLastLongitude(deviceIdByHardwareAddress);

        double findDeviceLastLatitude = deviceDao.getDeviceLastLatitude(deviceId);
        double findDeviceLastLongitude = deviceDao.getDeviceLastLongitude(deviceId);
        //计算设备间距离
        double distance = GetDistance.getDistance(currentDeviceLastLatitude, currentDeviceLastLongitude,
                findDeviceLastLatitude, findDeviceLastLongitude);

        if (price == 0) {
            price = 2500;
        }
        int taskLevel;
        try {
            //python分配任务等级
            taskLevel = SendMessage.getTaskLevel(price, distance);
        } catch (Exception e) {
            taskLevel = 2;
        }
        double point = taskLevel * 30 * task.getMaxReceiver();
        double userOwnedPoint = pointDao.getPointByUserId(task.getPublisherId());
        if ((userOwnedPoint - point) > 0) {
            //积分足够扣除，新增任务
            int i = taskDao.insertNewTask(task);
            //对用户的积分进行扣除
            pointDao.decreasePointByUserId(task.getPublisherId(), point);

            if (i == 1) {
                //更新到未完成的redis中
                updateUnfinishedTaskToRedis(getUnFinishedTaskOps(), task);
                Auction auction = new Auction();
                auction.setAuctioneerId(task.getPublisherId());
                auction.setTaskId(task.getTaskId());
                auction.setBidderNum(0);
                auctionDao.createAuction(auction);
                return 1;
            }
            //出现意外错误
            return -2;
        } else {
            //积分不够扣
            return -1;
        }

    }

    @Override
    public Task getTaskByTaskId(int taskId) {
        BoundHashOperations<String, Object, Object> unFinishedTaskOps = getUnFinishedTaskOps();
        Object o = unFinishedTaskOps.get(String.valueOf(taskId));
        if (o != null) {
            return JsonUtil.jsonToPojo(JsonUtil.objectToJson(o), Task.class);
        }
        BoundHashOperations<String, Object, Object> finishedTaskOps = getFinishedTaskOps();
        Object o1 = finishedTaskOps.get(String.valueOf(taskId));
        if (o1 != null) {
            return JsonUtil.jsonToPojo((String) o1, Task.class);
        }
        Task taskByTaskId = taskDao.getTaskByTaskId(taskId);
        //根据是否完成更新到redis当中
        if (taskByTaskId.getAccomplishFlag() == 0) {
            updateUnfinishedTaskToRedis(unFinishedTaskOps, taskByTaskId);
        } else {
            updateFinishedTaskToRedis(getFinishedTaskOps(), taskByTaskId);

        }
        return taskByTaskId;
    }

    @Override
    public int receiveTask(int taskId, int userId) {
        Task taskByTaskId = taskDao.getTaskByTaskId(taskId);
        if (taskByTaskId.getMaxReceiver() <= taskByTaskId.getReceiverNum()) {
            //接取人数已满
            return 2;
        }
        int result = taskDao.receiveTask(taskId, userId);
        taskDao.increaseTaskReceiveNum(taskId);
        if (result == 1) {
            //redis中内容更新
            BoundHashOperations<String, Object, Object> unFinishedTaskOps = getUnFinishedTaskOps();
            taskByTaskId = taskDao.getTaskByTaskId(taskId);
            updateUnfinishedTaskToRedis(unFinishedTaskOps, taskByTaskId);
            return 1;
        }
        return 0;
    }

    @Override
    public int checkReceive(int userId, int taskId) {
        int result = taskDao.checkReceive(userId, taskId);
        return result;
    }

    @Override
    public List<Task> getPublishTaskByUserId(int userId) {
        List<Task> taskList = taskDao.getPublishTaskByUserId(userId);
        return taskList;
    }

    @Override
    public List<Task> getReceiveTaskByUserId(int userId) {
        List<Task> receiveTaskByUserId = taskDao.getReceiveTaskByUserId(userId);
        return receiveTaskByUserId;
    }

    @Override
    public List<CheckUploadImage> getUploadUserList(int taskId) {
        List<User> uploadUserList = taskDao.getUploadUserList(taskId);
        List<CheckUploadImage> uploadList = new ArrayList<>();
        for (User user : uploadUserList) {
            CheckUploadImage checkUploadImage = new CheckUploadImage();
            checkUploadImage.setUserName(user.getUserName());
            checkUploadImage.setUploadUserId(user.getUserId());
            checkUploadImage.setTaskId(taskId);
            uploadList.add(checkUploadImage);
        }
        return uploadList;
    }


    private String getUnFinishedCartKey() {
        String key = String.format("task:unfinished");
        return key;
    }

    private String getFinishedCartKey() {
        String key = String.format("task:finished");
        return key;
    }

    /**
     * 获取未完成任务的redis操作类
     * <p>key task:unfinished:taskId<p/>
     *
     * @return BoundHashOperations
     */
    private BoundHashOperations<String, Object, Object> getUnFinishedTaskOps() {
        String key = getUnFinishedCartKey();
        return redisTemplate.boundHashOps(key);

    }

    private BoundHashOperations<String, Object, Object> getFinishedTaskOps() {
        String key = getFinishedCartKey();
        return redisTemplate.boundHashOps(key);

    }


    /**
     * 更新未完成的任务到redis中
     *
     * @param unFinishedTaskOps 操作类
     * @param unFinishedTask    未完成的任务
     */
    private void updateUnfinishedTaskToRedis(BoundHashOperations<String, Object, Object> unFinishedTaskOps, Task unFinishedTask) {
        unFinishedTaskOps.put(String.valueOf(unFinishedTask.getTaskId()), unFinishedTask);
    }

    /**
     * 更新完成的任务到redis中
     *
     * @param finishedTaskOps 操作类
     * @param finishedTask    已完成的任务
     */
    private void updateFinishedTaskToRedis(BoundHashOperations<String, Object, Object> finishedTaskOps, Task finishedTask) {
        finishedTaskOps.put(String.valueOf(finishedTask.getTaskId()), Objects.requireNonNull(JsonUtil.objectToJson(finishedTask)));
    }


    /**
     * 从redis中删除为完成的任务列表
     *
     * @param taskId 未完成的任务taskId
     */
    private void deleteUnfinishedTaskFromRedis(int taskId) {
        BoundHashOperations<String, Object, Object> unFinishedTaskOps = getUnFinishedTaskOps();
        unFinishedTaskOps.delete(String.valueOf(taskId));
    }

}
