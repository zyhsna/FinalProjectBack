package edu.zyh.finalproject.service.Impl;

import edu.zyh.finalproject.POJO.Task;
import edu.zyh.finalproject.dao.MapDao;
import edu.zyh.finalproject.dao.TaskDao;
import edu.zyh.finalproject.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyhsna
 */
@Service
public class MapServiceImpl implements MapService {
    @Autowired
    private MapDao mapDao;

    @Autowired
    private TaskDao taskDao;

    @Override
    public int updateLocation(int userId, String deviceId, double longitude, double latitude) {
        int result = mapDao.updateLocation(userId, deviceId, longitude, latitude);
        return result;
    }

    @Override
    public Map<String, Double> getLastLocation(int userId,String deviceId) {
        Double longitude = mapDao.getLongitude(userId, deviceId);
        Double latitude = mapDao.getLatitude(userId, deviceId);
        HashMap<String, Double> location = new HashMap<>(2);
        location.put("longitude", longitude);
        location.put("latitude",latitude);
        return location;
    }

    @Override
    public int updateDistance(int userId, int taskId) {
        Task taskByTaskId = taskDao.getTaskByTaskId(taskId);
        int taskLevel = taskByTaskId.getTaskLevel();
        double distance = taskLevel * 10000 + 5000 *Math.random();
        int result = mapDao.updateDistance(userId,taskId,distance);

        return 0;
    }
}
