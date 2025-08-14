package com.playstation.manager.service;

import com.playstation.manager.entity.CafeOrder;
import com.playstation.manager.entity.CafeOrderItem;
import com.playstation.manager.entity.Client;
import com.playstation.manager.entity.Product;
import com.playstation.manager.repository.CafeOrderItemRepository;
import com.playstation.manager.repository.CafeOrderRepository;
import com.playstation.manager.repository.ClientRepository;
import com.playstation.manager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeOrderService {

    private final CafeOrderRepository cafeOrderRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final CafeOrderItemRepository cafeOrderItemRepository;
    private final ActionLogService actionLogService;
    private final UserService userService;


    public CafeOrder createOrder(Long clientId, boolean isGuest) {
        CafeOrder order = new CafeOrder();
        order.setCreatedAt(LocalDateTime.now());
        order.setFinalized(false);
        order.setGuest(isGuest);
        order.setCreatedBy(userService.getCurrentUser());

        if (!isGuest && clientId != null) {
            Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
            order.setClient(client);
        }
        CafeOrder saved = cafeOrderRepository.save(order);
        actionLogService.log("CREATE", "Cafe Order", saved.getId());
        return saved;
    }


    public CafeOrderItem addItemToOrder(Long orderId, Long productId, int quantity) {
        CafeOrder order = cafeOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isFinalized()) {
            throw new RuntimeException("Order is already finalized.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        CafeOrderItem existingItem = null;

        // Check if the product is already in the order
        for (CafeOrderItem item : order.getItems()) {
            if (item.getProduct().getId().equals(productId)) {
                existingItem = item;
                break;
            }
        }

        if (existingItem != null) {
            // Update existing item
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalItemPrice(existingItem.getTotalItemPrice() + product.getPrice() * quantity);
            cafeOrderItemRepository.save(existingItem);
        } else {
            // Add new item
            CafeOrderItem newItem = new CafeOrderItem();
            newItem.setOrder(order);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setTotalItemPrice(product.getPrice() * quantity);
            existingItem = cafeOrderItemRepository.save(newItem);
        }

        // Recalculate total order amount from scratch (better than incremental)
        double totalItemPrice = product.getPrice() * quantity;
        double newTotal = order.getItems().stream()
                .mapToDouble(CafeOrderItem::getTotalItemPrice)
                .sum();
        order.setTotalAmount(newTotal + totalItemPrice);
        cafeOrderRepository.save(order);
        actionLogService.log("ADD_ITEM", "Cafe Order Item", existingItem.getId());
        return existingItem;
    }



    public CafeOrder finalizeOrder(Long orderId) {
        CafeOrder order = cafeOrderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.isFinalized()) {
            throw new RuntimeException("Order already finalized");
        }

        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new RuntimeException("Cannot finalize an empty order");
        }

        order.setFinalized(true);
        actionLogService.log("CLOSE", "Cafe Order", order.getId());
        return cafeOrderRepository.save(order);
    }

    public void removeItemFromOrder(Long itemId) {
        CafeOrderItem item = cafeOrderItemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        CafeOrder order = item.getOrder();

        if (order.isFinalized()) {
            throw new RuntimeException("Cannot modify a finalized order.");
        }


        double itemPrice = item.getTotalItemPrice();
        double newTotal = order.getTotalAmount() - itemPrice;
        order.setTotalAmount(Math.max(newTotal, 0));
        actionLogService.log("REMOVE_ITEM", "Cafe Order Item", itemId);
        cafeOrderItemRepository.delete(item);
        cafeOrderRepository.save(order);

    }



    public List<CafeOrder> getOpenOrder() {
        return cafeOrderRepository.findAllByFinalizedFalse();
    }
}

