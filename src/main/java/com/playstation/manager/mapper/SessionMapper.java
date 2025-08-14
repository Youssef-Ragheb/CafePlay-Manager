package com.playstation.manager.mapper;

import com.playstation.manager.dto.DeviceSessionDTO;
import com.playstation.manager.dto.OrderSessionDTO;
import com.playstation.manager.entity.Order;
import com.playstation.manager.entity.Product;
import com.playstation.manager.entity.Session;
import com.playstation.manager.repository.ProductRepository;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class SessionMapper {

    public static DeviceSessionDTO convertSessionToDTO(Session session) {
        DeviceSessionDTO dto = new DeviceSessionDTO();
        dto.setId(session.getId());
        dto.setDevice(session.getDevice());
        dto.setStatus(session.getStatus());
        dto.setEndTime(session.getEndTime());
        dto.setStartTime(session.getStartTime());
        dto.setCreatedBy(session.getCreatedBy());
        dto.setGameMode(session.getGameMode());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setTimePrice(session.getTimePrice());
        dto.setTotalOrdersPrice(session.getTotalOrdersPrice());
        dto.setTotalPrice(session.getTotalPrice());
        List<OrderSessionDTO> orderSessionDTOList = new ArrayList<>();
        for(Order order : session.getOrders()) {
            OrderSessionDTO orderSessionDTO = new OrderSessionDTO();
            orderSessionDTO.setOrderId(order.getId());
            orderSessionDTO.setQuantity(order.getQuantity());
            orderSessionDTO.setProductName(order.getProduct().getName());
            orderSessionDTO.setPrice(order.getProduct().getPrice());
            orderSessionDTOList.add(orderSessionDTO);
        }
        dto.setOrders(orderSessionDTOList);
        return dto;
    }
}
