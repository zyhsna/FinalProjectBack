package edu.zyh.finalproject.controller;

import edu.zyh.finalproject.service.MapService;
import edu.zyh.finalproject.util.JSONData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于处理地图相关的控制器
 *
 * @author zyhsna
 */
@RequestMapping("/api/v1/map")
@RestController
public class MapController {
    @Autowired
    private MapService mapService;

    /**
     * 更新设备定位相关
     *
     * @param userId    用户ID
     * @param longitude 经度
     * @param latitude  纬度
     * @return 0 返回成功，17 更新位置失败
     */
    @GetMapping("/updateLocation")
    public JSONData updateLocation(int userId, String hardwareAddress, double longitude, double latitude) {
        int result = mapService.updateLocation(userId, hardwareAddress, longitude, latitude);
        if (result == 1){
            return JSONData.buildSuccess();
        }else {
            return JSONData.buildError(17, "更新位置失败");
        }
    }

    @GetMapping("/updateDistance")
    public JSONData updateDistance(int userId,int taskId){

        int result = mapService.updateDistance(userId, taskId);
        return JSONData.buildSuccess();
    }
}
