package edu.zyh.finalproject.service.Impl;

import edu.zyh.finalproject.POJO.SurroundDevice;
import edu.zyh.finalproject.dao.DeviceDao;
import edu.zyh.finalproject.dao.SurroundDeviceDao;
import edu.zyh.finalproject.service.SurroundDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zyhsna
 */
@Service
public class SurroundDeviceServiceImpl implements SurroundDeviceService {
    @Autowired
    private SurroundDeviceDao surroundDeviceDao;

    @Autowired
    private DeviceDao deviceDao;

    @Override
    public int updateSurroundDevice(SurroundDevice surroundDevice) {
        int result = 0;
        if (deviceDao.checkExistenceByHardwareAddress(surroundDevice.getHardwareAddress()) == 1) {
            //存在这个设备
            int dropFlag = deviceDao.getDropFlagByHardwareAddress(surroundDevice.getHardwareAddress());
            if (dropFlag == 1) {
                //已经丢失了，先查询是否在数据库中，如果有则更新相关信息，没有则新建
                int existence = surroundDeviceDao.checkExistence(surroundDevice.getHardwareAddress(), surroundDevice.getScanUserId());
                if (existence > 0) {
                    //存在，更新
                    result = surroundDeviceDao.updateSurroundDevice(surroundDevice);
                } else {
                    result = surroundDeviceDao.insertNewSurroundDevice(surroundDevice);
                }
            } else {
                result = -1;
            }
            return result;
        }
        return -2;
    }

    @Override
    public List<SurroundDevice> getDropDeviceInfo(int deviceId) {
        String hardwareAddress = deviceDao.getHardwareAddressByDeviceId(deviceId);
        List<SurroundDevice> surroundDeviceList = surroundDeviceDao.getDropDeviceInfo(hardwareAddress);
        return surroundDeviceList;
    }
}
