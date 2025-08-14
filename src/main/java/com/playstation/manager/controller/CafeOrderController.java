package com.playstation.manager.controller;

import com.playstation.manager.entity.CafeOrder;
import com.playstation.manager.entity.CafeOrderItem;
import com.playstation.manager.service.CafeOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cafe-orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CafeOrderController {

    private final CafeOrderService cafeOrderService;


    @PostMapping("/create")
    public CafeOrder createOrder(@RequestParam(required = false) Long clientId,
                                 @RequestParam(defaultValue = "true") boolean isGuest) {
        return cafeOrderService.createOrder(clientId, isGuest);
    }


    @PostMapping("/{orderId}/add-item")
    public CafeOrderItem addItemToOrder(@PathVariable Long orderId,
                                        @RequestParam Long productId,
                                        @RequestParam int quantity) {
        return cafeOrderService.addItemToOrder(orderId, productId, quantity);
    }


    @DeleteMapping("/item/{itemId}")
    public void removeItemFromOrder(@PathVariable Long itemId) {
        cafeOrderService.removeItemFromOrder(itemId);
    }


    @PostMapping("/{orderId}/finalize")
    public CafeOrder finalizeOrder(@PathVariable Long orderId) {
        return cafeOrderService.finalizeOrder(orderId);
    }


    @GetMapping("/open")
    public List<CafeOrder> getOpenOrders() {
        return cafeOrderService.getOpenOrder();
    }
}
