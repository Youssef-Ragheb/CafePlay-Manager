package com.playstation.manager.controller;

import com.playstation.manager.dto.ProductDTO;
import com.playstation.manager.entity.User;
import com.playstation.manager.exception.UnauthorizedException;
import com.playstation.manager.service.ProductService;
import com.playstation.manager.service.UserService;
import com.playstation.manager.utilz.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;
    private final AuthUtil authUtil;

    @PostMapping("/add")
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO dto) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Access denied. Only admins can add products.");
        return ResponseEntity.ok(productService.addProduct(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can delete products.");
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductDTO dto) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can update products.");
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }
    @PutMapping("/stock/{id}")
    public ResponseEntity<ProductDTO> updateProductStock(@PathVariable Long id, @RequestParam int stock) {
        User user = userService.getCurrentUser();
        if (!authUtil.isAdmin(user)) throw new UnauthorizedException("Only admins can update products.");
        return ResponseEntity.ok(productService.updateProductStock(id, stock));
    }

}
