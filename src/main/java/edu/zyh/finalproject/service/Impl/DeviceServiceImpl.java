package edu.zyh.finalproject.service.Impl;

import edu.zyh.finalproject.POJO.Device;
import edu.zyh.finalproject.dao.DeviceDao;
import edu.zyh.finalproject.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceDao deviceDao;

    @Override
    public int insertNewDevice(int userId, String hardwareAddress,String deviceName,String devicePrice) {
        int duplicateDevice = checkDuplicateDevice(userId, hardwareAddress);
        if (duplicateDevice > 0) {
            return -1;
        }
        int result;
        try {
            result = deviceDao.insertNewDevice(userId, hardwareAddress,deviceName,devicePrice);
        } catch (Exception e) {
            return -2;
        }
        return result;
    }

    @Override
    public List<Device> getDeviceListByUserId(int userId) {
        List<Device> deviceList = deviceDao.getDeviceListByUserId(userId);
        return deviceList;
    }

    @Override
    public List<Double> getDeviceLastLocation(int deviceId) {
        double longitude = deviceDao.getDeviceLastLongitude(deviceId);
        double latitude = deviceDao.getDeviceLastLatitude(deviceId);
        ArrayList<Double> list = new ArrayList<>();
        list.add(longitude);
        list.add(latitude);
        return list;
    }

    @Override
    public int dropDevice(int deviceId) {
        int result = deviceDao.dropDevice(deviceId);
        return result;
    }

    /**
     * 检查设备是否重复绑定
     *
     * @param hardwareAddress 蓝牙MAC地址
     * @return 1 绑定过了 0 未绑定
     */
    private int checkDuplicateDevice(int userId, String hardwareAddress) {
        int result = deviceDao.checkDuplicateDevice(userId, hardwareAddress);
        return result;
    }
}
