package org.example.order_inventory_system.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.order_inventory_system.model.Inventory;
import org.example.order_inventory_system.model.Order;
import org.example.order_inventory_system.model.Shipment;
import org.example.order_inventory_system.model.Store;
import org.example.order_inventory_system.service.InventoryService;
import org.example.order_inventory_system.service.OrderService;
import org.example.order_inventory_system.service.ShipmentService;
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
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final ShipmentService shipmentService;

    @GetMapping
    public ResponseEntity<List<Store>> getAll() {
        return ResponseEntity.ok(storeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Store> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(storeService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Store>> search(@RequestParam String name) {
        return ResponseEntity.ok(storeService.searchByName(name));
    }

    // GET inventory at a store
    @GetMapping("/{id}/inventory")
    public ResponseEntity<List<Inventory>> getInventory(@PathVariable Integer id) {
        storeService.findById(id);
        return ResponseEntity.ok(inventoryService.findByStoreId(id));
    }

    // GET orders at a store
    @GetMapping("/{id}/orders")
    public ResponseEntity<List<Order>> getOrders(@PathVariable Integer id) {
        storeService.findById(id);
        return ResponseEntity.ok(orderService.findByStoreId(id));
    }

    // GET shipments from a store
    @GetMapping("/{id}/shipments")
    public ResponseEntity<List<Shipment>> getShipments(@PathVariable Integer id) {
        storeService.findById(id);
        return ResponseEntity.ok(shipmentService.findByStoreId(id));
    }

    @PostMapping
    public ResponseEntity<Store> create(@Valid @RequestBody Store store) {
        store.setStoreId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.save(store));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Store> update(@PathVariable Integer id,
                                        @Valid @RequestBody Store store) {
        storeService.findById(id);
        store.setStoreId(id);
        return ResponseEntity.ok(storeService.save(store));
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        storeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}