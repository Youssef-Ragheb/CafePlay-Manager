package com.playstation.manager.service;

import com.playstation.manager.dto.DeviceSessionDTO;
import com.playstation.manager.dto.OrderDTO;
import com.playstation.manager.dto.SessionDTO;
import com.playstation.manager.entity.*;
import com.playstation.manager.repository.OrderRepository;
import com.playstation.manager.repository.PlaystationDeviceRepository;
import com.playstation.manager.repository.ProductRepository;
import com.playstation.manager.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.playstation.manager.mapper.SessionMapper.convertSessionToDTO;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final PlaystationDeviceRepository deviceRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final ActionLogService actionLogService;

    @Transactional
    public DeviceSessionDTO startSession(SessionDTO dto) {
        PlaystationDevice device = deviceRepository.findById(dto.getDeviceId()).orElseThrow(() -> new RuntimeException("Device not found"));
        if (!device.isAvailable()) throw new RuntimeException("Device is not available");

        Session session = new Session();
        session.setDevice(device);
        session.setCreatedBy(userService.getCurrentUser());
        session.setGameMode(dto.getGameMode());
        session.setStartTime(LocalDateTime.now());
        session.setStatus(SessionStatus.ACTIVE);
        session.setTotalOrdersPrice(0);
        session.setTotalPrice(0);

        device.setAvailable(false);
        Session savedSession = sessionRepository.save(session);
        actionLogService.log("START", "Session", savedSession.getId());
        return convertSessionToDTO(savedSession);
    }

    @Transactional
    public DeviceSessionDTO addOrder(Long sessionId, OrderDTO orderDTO) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != SessionStatus.ACTIVE) throw new RuntimeException("Session is not active");

        Product product = productRepository.findById(orderDTO.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));

        for (Order order : session.getOrders()) {
            if (order.getProduct().getId().equals(product.getId())) {
                int oldQuantity = order.getQuantity();
                int newQuantity = oldQuantity + orderDTO.getQuantity();

                int availableStock = product.getStock() + oldQuantity; // allow re-adding what was already reserved
                if (newQuantity > availableStock) {
                    throw new RuntimeException("Not enough stock for product: " + product.getName());
                }

                // Adjust stock
                int quantityDiff = newQuantity - oldQuantity;
                product.setStock(product.getStock() - quantityDiff);
                productRepository.save(product);

                // Update order
                double oldTotal = order.getTotalPrice();
                double newTotal = product.getPrice() * newQuantity;

                order.setQuantity(newQuantity);
                order.setTotalPrice(newTotal);

                session.setTotalOrdersPrice(session.getTotalOrdersPrice() - oldTotal + newTotal);

                orderRepository.save(order);
                return convertSessionToDTO(sessionRepository.save(session));
            }
        }

        // New order
        if (orderDTO.getQuantity() > product.getStock()) {
            throw new RuntimeException("Not enough stock for product: " + product.getName());
        }

        product.setStock(product.getStock() - orderDTO.getQuantity());
        productRepository.save(product);

        Order order = new Order();
        order.setSession(session);
        order.setProduct(product);
        order.setQuantity(orderDTO.getQuantity());
        double totalPrice = product.getPrice() * orderDTO.getQuantity();
        order.setTotalPrice(totalPrice);
        order = orderRepository.save(order);

        session.getOrders().add(order);
        session.setTotalOrdersPrice(session.getTotalOrdersPrice() + totalPrice);

        return convertSessionToDTO(sessionRepository.save(session));
    }

    @Transactional
    public DeviceSessionDTO removeOrder(Long sessionId, Long orderId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != SessionStatus.ACTIVE) throw new RuntimeException("Session is not active");

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (!session.getOrders().remove(order)) {
            throw new RuntimeException("Order does not belong to this session");
        }

        // Restore stock
        Product product = order.getProduct();
        product.setStock(product.getStock() + order.getQuantity());
        productRepository.save(product);

        session.setTotalOrdersPrice(session.getTotalOrdersPrice() - order.getTotalPrice());

        orderRepository.delete(order);
        return convertSessionToDTO(sessionRepository.save(session));
    }

    public DeviceSessionDTO getCurrentSessionForDevice(Long deviceId) {
        return convertSessionToDTO(sessionRepository.findByDeviceIdAndStatus(deviceId, SessionStatus.ACTIVE).orElseThrow(() -> new RuntimeException("No active session for this device")));
    }

    @Transactional
    public DeviceSessionDTO endSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId).orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() != SessionStatus.ACTIVE) throw new RuntimeException("Session is not active");

        session.setEndTime(LocalDateTime.now());

        long totalSeconds = Duration.between(session.getStartTime(), session.getEndTime()).getSeconds();
        int roundedMinutes = (int) Math.ceil(totalSeconds / 60.0);

        session.setDurationMinutes(roundedMinutes);

        double timeRate = session.getGameMode() == GameMode.SINGLE ? session.getDevice().getType().getSinglePrice() : session.getDevice().getType().getMultiPrice();

        double timePrice = (roundedMinutes) * timeRate;
        session.setTimePrice(timePrice);
        session.setTotalPrice(timePrice + session.getTotalOrdersPrice());
        session.setStatus(SessionStatus.FINISHED);

        PlaystationDevice device = session.getDevice();
        device.setAvailable(true);
        deviceRepository.save(device);
        Session saved = sessionRepository.save(session);
        actionLogService.log("END", "Session", saved.getId());
        return convertSessionToDTO(saved);
    }

    public Page<Session> getSessionsBetweenDates(LocalDate start, LocalDate end, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").descending());

        if (start != null && end != null) {
            return sessionRepository.findByStartTimeBetween(start.atStartOfDay(), end.atTime(23, 59), pageable);
        }

        return sessionRepository.findAll(pageable); // fallback when no filter
    }


}