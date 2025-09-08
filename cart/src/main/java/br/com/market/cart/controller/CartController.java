package br.com.market.cart.controller;

import br.com.market.cart.dto.CartDTO;
import br.com.market.cart.model.Cart;
import br.com.market.cart.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar carrinho pelo ID",
            description = "Retorna os detalhes de um carrinho pelo seu identificador único.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID do carrinho",
                            required = true,
                            example = "123"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Carrinho encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Carrinho não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Cart not found.\" }")
                            )
                    )
            }
    )
    public ResponseEntity<CartDTO> show(@PathVariable long id) {
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/{id}")
    @Operation(
            summary = "Adicionar produto ao carrinho",
            description = """
        Adiciona um produto ao carrinho. 
        
        - Se o produto já existe no carrinho, a quantidade é incrementada.  
        - Caso contrário, o produto é incluído.
        """,
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID do carrinho",
                            required = true,
                            example = "123"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do produto a ser adicionado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                {
                  "productId": 456,
                  "quantity": 2
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto adicionado com sucesso ao carrinho",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida (ex.: productId ausente ou inválido)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Product ID Not Found.\" }")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Carrinho não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Cart not found.\" }")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Produto não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Product not found.\" }")
                            )
                    )
            }
    )
    public ResponseEntity<?> addProduct(@PathVariable long id,@RequestBody Map<String, Object> productIntent) {
        Number value = (Number) productIntent.get("productId");
        Long productId = value != null ? value.longValue() : null;
        int quantity = (int) productIntent.getOrDefault("quantity", 1);
        if(productId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID Not Found.");
        }
        return ResponseEntity.ok(cartService.addProduct(id, productId, quantity));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Remove um produto do carrinho",
            description = """
        Remove um produto específico de um carrinho pelo seu ID.
        
        - Se a quantidade informada for menor que a quantidade no carrinho, apenas diminui a quantidade.
        - Se a quantidade informada for igual ou maior, o produto é removido totalmente.
        """,
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID do carrinho",
                            required = true,
                            example = "123"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Dados do produto a ser removido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    example = """
                {
                  "productId": 456,
                  "quantity": 2
                }
                """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Produto removido com sucesso do carrinho",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CartDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Requisição inválida (ex.: productId ausente ou inválido)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Product ID Not Found.\" }")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Carrinho não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Cart not found.\" }")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Produto não encontrado",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Product not found.\" }")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Produto não encontrado no carrinho",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(example = "{ \"error\": \"Product not found in Cart.\" }")
                            )
                    )
            }
    )
    public ResponseEntity<?> deleteProduct(@PathVariable long id, @RequestBody Map<String, Object> productIntent) {
        Number value = (Number) productIntent.get("productId");
        Long productId = value != null ? value.longValue() : null;
        int quantity = (int) productIntent.getOrDefault("quantity", 1);
        if(productId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product ID Not Found.");
        }
        return ResponseEntity.ok(cartService.removeProduct(id, productId, quantity));
    }
}
