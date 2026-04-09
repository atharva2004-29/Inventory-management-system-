package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.StoreService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreRestController {

    private final StoreService storeService;

    // GET all stores
    @GetMapping
    public ResponseEntity<List<Store>> getAll() {
        return ResponseEntity.ok(storeService.findAll());
    }

    // GET single store by ID
    @GetMapping("/{id}")
    public ResponseEntity<Store> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(storeService.findById(id));
    }

    // GET search by name
    @GetMapping("/search")
    public ResponseEntity<List<Store>> search(@RequestParam String name) {
        return ResponseEntity.ok(storeService.searchByName(name));
    }

    // POST create new store
    @PostMapping
    public ResponseEntity<Store> create(@Valid @RequestBody Store store) {
        store.setStoreId(null); // ensure insert, not update
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.save(store));
    }

    // PUT update existing store
    @PutMapping("/{id}")
    public ResponseEntity<Store> update(@PathVariable Integer id,
                                        @Valid @RequestBody Store store) {
        storeService.findById(id); // throws if not found
        store.setStoreId(id);
        return ResponseEntity.ok(storeService.save(store));
    }

    // DELETE store
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        storeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}