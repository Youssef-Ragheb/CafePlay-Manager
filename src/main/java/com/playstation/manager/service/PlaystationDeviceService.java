package com.playstation.manager.service;

import com.playstation.manager.entity.PlaystationDevice;
import com.playstation.manager.entity.User;
import com.playstation.manager.repository.PlaystationDeviceRepository;
import com.playstation.manager.utilz.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaystationDeviceService {
    private final AuthUtil authUtil;
    private final UserService userService;
    private final PlaystationDeviceRepository deviceRepository;

    public PlaystationDevice add(PlaystationDevice device) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        return deviceRepository.save(device);
    }

    public List<PlaystationDevice> getAll() {
        return deviceRepository.findAll();
    }

    public void delete(Long id) {
        User currentUser =  userService.getCurrentUser();
        if(authUtil.isAdmin(currentUser)) {
            deviceRepository.deleteById(id);
        }

    }

    public PlaystationDevice update(Long id, PlaystationDevice updatedDevice) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        PlaystationDevice device = deviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Device not found"));
        device.setName(updatedDevice.getName());
        device.setAvailable(updatedDevice.isAvailable());
        device.setType(updatedDevice.getType());
        return deviceRepository.save(device);
    }
}