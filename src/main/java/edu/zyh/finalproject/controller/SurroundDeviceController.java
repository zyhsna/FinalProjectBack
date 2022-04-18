package edu.zyh.finalproject.controller;

import edu.zyh.finalproject.POJO.SurroundDevice;
import edu.zyh.finalproject.service.SurroundDeviceService;
import edu.zyh.finalproject.util.JSONData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zyhsna
 */
@RestController
@RequestMapping("/api/v1/surroundDevice")
public class SurroundDeviceController {
    @Autowired
    private SurroundDeviceService surroundDeviceService;


    @RequestMapping("/updateSurroundDevice")
    public JSONData updateSurroundDevice(@RequestBody SurroundDevice surroundDevice){
        int result = surroundDeviceService.updateSurroundDevice(surroundDevice);
        return JSONData.buildSuccess();
    }

    @RequestMapping("/getDropDeviceInfo")
    public JSONData getDropDeviceInfo(int deviceId){
        List<SurroundDevice> surroundDeviceList = surroundDeviceService.getDropDeviceInfo(deviceId);
        if (surroundDeviceList != null){
            return JSONData.buildSuccess(surroundDeviceList);
        }else {
            return JSONData.buildError(31, "获取丢失设备信息失败");
        }
    }

}
