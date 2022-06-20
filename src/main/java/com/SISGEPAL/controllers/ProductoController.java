package com.SISGEPAL.controllers;

import com.SISGEPAL.DTO.productos.request.NewProductoDTO;
import com.SISGEPAL.DTO.productos.request.UpdateProductoDTO;
import com.SISGEPAL.DTO.productos.response.ProductoDTO;
import com.SISGEPAL.DTO.productos.response.ProductosDTO;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.services.MailingService;
import com.SISGEPAL.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private MailingService mailingService;


    @GetMapping
    public ResponseEntity<ProductosDTO> getProductos(){
        ProductosDTO productosDTO =
                productoService.mapToProductosDTO(
                        productoService.findAll()
                );
        ResponseEntity<ProductosDTO> response
                = new ResponseEntity<ProductosDTO>(productosDTO, HttpStatus.OK);

        return response;
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> postProducto(@RequestBody NewProductoDTO newProductoDTO)
            throws BadRequestException, ConflictException, MessagingException, IOException {
        ProductoDTO productoDTO =
                productoService.mapToProductoDTO(productoService.createProducto(newProductoDTO));
        ResponseEntity<ProductoDTO> response
                = new ResponseEntity<ProductoDTO>(productoDTO,HttpStatus.CREATED);
        return response;

    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> putProducto(@RequestBody UpdateProductoDTO updateProductoDTO,
                                                     @PathVariable int id)
            throws BadRequestException, ConflictException, MessagingException, IOException, NotFoundException {
        ProductoDTO productoDTO =
                productoService.mapToProductoDTO(productoService.updateProducto(updateProductoDTO,id));
        ResponseEntity<ProductoDTO> response
                = new ResponseEntity<ProductoDTO>(productoDTO,HttpStatus.OK);
        return response;

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductoDTO> deleteProveedor(Authentication authentication, @PathVariable int id)
            throws BadRequestException {
        ProductoDTO productoDTO =
                productoService.mapToProductoDTO(productoService.deleteProducto(id, authentication.getPrincipal()));
        ResponseEntity<ProductoDTO> response
                = new ResponseEntity<ProductoDTO>(productoDTO,HttpStatus.OK);
        return response;

    }



}
