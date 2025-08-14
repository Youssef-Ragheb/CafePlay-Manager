package com.playstation.manager.service;

import com.playstation.manager.entity.PlaystationType;
import com.playstation.manager.entity.User;
import com.playstation.manager.repository.PlaystationTypeRepository;
import com.playstation.manager.utilz.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaystationTypeService {
    private final AuthUtil authUtil;
    private final UserService userService;
    private final PlaystationTypeRepository playstationTypeRepository;

    public PlaystationType add(PlaystationType type) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        return playstationTypeRepository.save(type);
    }

    public List<PlaystationType> getAll() {
        return playstationTypeRepository.findAll();
    }

    public void delete(Long id) {
        User currentUser =  userService.getCurrentUser();
        if(authUtil.isAdmin(currentUser)) {
            playstationTypeRepository.deleteById(id);
        }

    }

    public PlaystationType update(Long id, PlaystationType updatedType) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        PlaystationType type = playstationTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PlaystationType not found"));
        type.setName(updatedType.getName());
        type.setSinglePrice(updatedType.getSinglePrice());
        type.setMultiPrice(updatedType.getMultiPrice());
        return playstationTypeRepository.save(type);
    }
}