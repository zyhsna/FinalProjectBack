package edu.zyh.finalproject.controller;

import edu.zyh.finalproject.POJO.Device;
import edu.zyh.finalproject.service.DeviceService;
import edu.zyh.finalproject.util.JSONData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备控制类
 *
 * @author zyhsna
 */
@RequestMapping("/api/v1/device")
@RestController
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @RequestMapping("/insertNewDevice")
    public JSONData insertNewDevice(int userId, String hardwareAddress, String deviceName,String devicePrice) {
        int result = deviceService.insertNewDevice(userId, hardwareAddress,deviceName,devicePrice);
        if (result > 0) {
            return JSONData.buildSuccess();
        } else if (result == -1) {
            return JSONData.buildError(19, "设备重复绑定");
        }
        return JSONData.buildError(20, "绑定失败");
    }

    @RequestMapping("/getDeviceListByUserId")
    public JSONData getDeviceListByUserId(int userId) {
        List<Device> deviceList = deviceService.getDeviceListByUserId(userId);
        if (deviceList != null) {
            return JSONData.buildSuccess(deviceList);
        } else {
            return JSONData.buildError(26, "获取绑定设备列表失败");
        }
    }

    @RequestMapping("/getDeviceLastLocation")
    public JSONData getDeviceLastLocation(int deviceId){
       List<Double> location =  deviceService.getDeviceLastLocation(deviceId);
       if (location!=null && location.size()!=0){
       return JSONData.buildSuccess(location);}
       else {
           return JSONData.buildError(27,"获取最后位置失败");
       }
    }

    @RequestMapping("/dropDevice")
    public JSONData dropDevice(int deviceId){
        int result = deviceService.dropDevice(deviceId);
        if (result == 1){
            return JSONData.buildSuccess();
        }
        return JSONData.buildError(30,"挂失失败");
    }

}
