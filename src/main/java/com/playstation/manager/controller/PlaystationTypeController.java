package com.playstation.manager.controller;

import com.playstation.manager.entity.PlaystationType;
import com.playstation.manager.entity.User;
import com.playstation.manager.exception.UnauthorizedException;
import com.playstation.manager.service.PlaystationTypeService;
import com.playstation.manager.service.UserService;
import com.playstation.manager.utilz.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/playstation-types")
@RequiredArgsConstructor
public class PlaystationTypeController {

    private final PlaystationTypeService playstationTypeService;
    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping("/add")
    public ResponseEntity<PlaystationType> add(@RequestBody PlaystationType type) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can add Playstation types");
        return ResponseEntity.ok(playstationTypeService.add(type));
    }

    @GetMapping
    public ResponseEntity<List<PlaystationType>> getAll() {
        return ResponseEntity.ok(playstationTypeService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can delete Playstation types");
        playstationTypeService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaystationType> update(@PathVariable Long id, @RequestBody PlaystationType updatedType) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can update Playstation types");
        return ResponseEntity.ok(playstationTypeService.update(id, updatedType));
    }
}