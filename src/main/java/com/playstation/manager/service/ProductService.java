package com.playstation.manager.service;

import com.playstation.manager.dto.ProductDTO;
import com.playstation.manager.entity.Product;
import com.playstation.manager.entity.User;
import com.playstation.manager.mapper.ProductMapper;
import com.playstation.manager.repository.ProductRepository;
import com.playstation.manager.utilz.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ActionLogService actionLogService;
    private final AuthUtil authUtil;
    private final UserService userService;

    public ProductDTO addProduct(ProductDTO dto) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        Product saved = productRepository.save(productMapper.toEntity(dto));
        actionLogService.log("CREATE", "Product", saved.getId());
        return productMapper.toDTO(saved);
    }

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteProduct(Long id) {
        User currentUser =  userService.getCurrentUser();
        if(authUtil.isAdmin(currentUser)) {
            actionLogService.log("DELETE", "Product", id);
            productRepository.deleteById(id);
        }

    }
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());

        Product updated = productRepository.save(existing);
        actionLogService.log("UPDATE", "Product", updated.getId());
        return productMapper.toDTO(updated);
    }
    public ProductDTO updateProductStock(Long id, int stock) {
        User currentUser =  userService.getCurrentUser();
        if(!authUtil.isAdmin(currentUser)) {
            return null;
        }
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        existing.setStock(existing.getStock() + stock);

        Product updated = productRepository.save(existing);
        actionLogService.log("UPDATE Stock", "Product", updated.getId());
        return productMapper.toDTO(updated);
    }
}
