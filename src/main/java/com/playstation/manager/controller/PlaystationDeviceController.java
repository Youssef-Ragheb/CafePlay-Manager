package com.playstation.manager.controller;

import com.playstation.manager.entity.PlaystationDevice;
import com.playstation.manager.entity.User;
import com.playstation.manager.exception.UnauthorizedException;
import com.playstation.manager.service.PlaystationDeviceService;
import com.playstation.manager.service.UserService;
import com.playstation.manager.utilz.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class PlaystationDeviceController {

    private final PlaystationDeviceService deviceService;
    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping("/add")
    public ResponseEntity<PlaystationDevice> addDevice(@RequestBody PlaystationDevice device) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can add devices");
        return ResponseEntity.ok(deviceService.add(device));
    }

    @GetMapping
    public ResponseEntity<List<PlaystationDevice>> getAllDevices() {
        return ResponseEntity.ok(deviceService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can delete devices");
        deviceService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaystationDevice> updateDevice(@PathVariable Long id, @RequestBody PlaystationDevice updatedDevice) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can update devices");
        return ResponseEntity.ok(deviceService.update(id, updatedDevice));
    }
}