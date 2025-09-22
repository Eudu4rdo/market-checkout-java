package br.com.market.stock.controller;

import br.com.market.stock.dto.ProductDTO;
import br.com.market.stock.dto.QuantityDTO;
import br.com.market.stock.model.Product;
import br.com.market.stock.service.StockService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
@Tag(name = "Stock", description = "Stock management operations")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping("/products")
    @Operation(
            summary = "List all products",
            description = "List all products in stock",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List all products"
                    )
            }
    )
    public ResponseEntity<List<ProductDTO>> show(){
        return ResponseEntity.ok(stockService.getAllProducts());
    }

    @PostMapping("/product/add")
    @Operation(
            summary = "Add new product",
            description = "Add a new product to the stock",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product added successfully"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid product data"
                    )
            }
    )
    public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product){
        return ResponseEntity.ok(stockService.addProduct(product));
    }

    @DeleteMapping("/product/{id}/delete")
    @Operation(
            summary = "Delete product",
            description = "Delete a product from the stock",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid product data"
                    )
            }
    )
    public ResponseEntity<ProductDTO> deleteProduct(
            @Parameter(description = "Product ID") @PathVariable Long id){
        return ResponseEntity.ok(stockService.deleteProduct(id));
    }

    @PostMapping("/product/{id}/edit")
    @Operation(
            summary = "Update product",
            description = "Update an existing product in the stock",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product updated successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    public ResponseEntity<ProductDTO> updateProduct(
            @Parameter(description = "Product ID") @PathVariable Long id, 
            @RequestBody Product product){
        return ResponseEntity.ok(stockService.updateProduct(id, product));
    }

    @PostMapping("/product/{id}/increase")
    @Operation(
            summary = "Increase product quantity",
            description = "Increase the quantity of a product in stock",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product quantity increased successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    )
            }
    )
    public ResponseEntity<ProductDTO> increaseQuantity(
            @Parameter(description = "Product ID") @PathVariable Long id, 
            @RequestBody QuantityDTO dto){
        return ResponseEntity.ok(stockService.increaseQuantity(id, dto.getQuantity()));
    }

    @PostMapping("/product/{id}/decrease")
    @Operation(
            summary = "Decrease product quantity",
            description = "Decrease the quantity of a product in stock",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Product quantity decreased successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Product not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid quantity"
                    )
            }
    )
    public ResponseEntity<ProductDTO> decreaseQuantity(
            @Parameter(description = "Product ID") @PathVariable Long id, 
            @RequestBody QuantityDTO dto){
        return ResponseEntity.ok(stockService.decreaseQuantity(id, dto.getQuantity()));
    }
}
