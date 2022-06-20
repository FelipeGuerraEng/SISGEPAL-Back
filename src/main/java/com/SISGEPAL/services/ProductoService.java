package com.SISGEPAL.services;

import com.SISGEPAL.DTO.productos.request.NewProductoDTO;
import com.SISGEPAL.DTO.productos.request.UpdateProductoDTO;
import com.SISGEPAL.DTO.productos.response.ProductoDTO;
import com.SISGEPAL.DTO.productos.response.ProductosDTO;
import com.SISGEPAL.entities.ProductoEntity;
import com.SISGEPAL.exceptions.BadRequestException;
import com.SISGEPAL.exceptions.ConflictException;
import com.SISGEPAL.exceptions.NotFoundException;
import com.SISGEPAL.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private LoginService logiService;

    @Autowired
    private MailingService mailingService;

    @Autowired
private AdministradorService administradorService;



    public ProductoEntity findProductoByCodigo(String codigo){
        return productoRepository.findByCodigo(codigo);
    }


    public List<ProductoEntity> findAll(){
        return productoRepository.findAll();
    }


       public ProductosDTO mapToProductosDTO(List<ProductoEntity> productos){
           List<ProductoDTO> productosDTOList =
                   new ArrayList<ProductoDTO>();
           ProductosDTO productosDTO = new ProductosDTO();

           productos.forEach(e -> {
               ProductoDTO productoDTO = mapToProductoDTO(e);
               productosDTOList.add(productoDTO);
           });

           productosDTO.setProductos(productosDTOList);

           return productosDTO;
       }



public ProductoDTO mapToProductoDTO(ProductoEntity producto){
    ProductoDTO productoDTO = new ProductoDTO();

    productoDTO.setProducto_id(producto.getId());
    productoDTO.setCodigo_producto(producto.getCodigo_producto());
    productoDTO.setNombre(producto.getNombre());
    productoDTO.setProveedor_id(producto.getProovedor_id());
    productoDTO.setStock(producto.getStock());
    productoDTO.setPrecio(producto.getPrecio());

        return productoDTO;
    }

    public ProductoEntity createProducto (NewProductoDTO productoDTO) throws BadRequestException, ConflictException, MessagingException, IOException {
        final String codigo = productoDTO.getCodigo_producto();
        String message = "";
        final boolean isrepeatedCodigo = isRepeatedCodigo(productoDTO.getCodigo_producto());


        if(!isrepeatedCodigo) {
            ProductoEntity producto = new ProductoEntity();

            producto.setCodigo_producto(productoDTO.getCodigo_producto());
            producto.setProovedor_id(productoDTO.getProveedor_id());
            producto.setNombre(productoDTO.getNombre());
            producto.setStock(productoDTO.getStock());
            producto.setPrecio(productoDTO.getPrecio());

            producto = productoRepository.save(producto);

            return producto;
        }else{
            message = "el Codigo ya esta en uso";

        }

        throw new BadRequestException(message);
    }


    public ProductoEntity updateProducto(UpdateProductoDTO productoDTO, int productoID)

            throws NotFoundException, BadRequestException, MessagingException, IOException {
        ProductoEntity producto = productoRepository.findById(productoID);
        if(producto == null) {
            throw new NotFoundException(String.format(
                    "No existe un producto con id %d",productoID
            ));
        }else {

            final boolean isValidUpdate = isRepeatedCodigo(productoDTO.getCodigo_producto())
                    && producto.getNombre().equals(productoDTO.getNombre());

            if (isValidUpdate) {
                final String oldCodigo = producto.getCodigo_producto();
                final String newCodigo = productoDTO.getCodigo_producto();
                producto.setNombre(productoDTO.getNombre());
                producto.setCodigo_producto(newCodigo);
                producto.setPrecio(productoDTO.getPrecio());
                producto.setProovedor_id(productoDTO.getProveedor_id());
                producto.setStock(productoDTO.getStock());
                productoRepository.save(producto);
                return producto;
            } else {

                String message = "";

                message = "codigo no válido";
                throw new BadRequestException(message);
            }
        }
    }


    public ProductoEntity deleteProducto(int productoID, Object principal) throws BadRequestException {
        ProductoEntity producto = productoRepository.findById(productoID);
        if(producto==null){
            throw new BadRequestException("No es posible eliminar el producto. Dado que no existe");
        }
      else {

            productoRepository.delete(producto);
            return producto;


        }

    }

    public boolean isRepeatedCodigo(String codigo) {
        return productoRepository.findByCodigo(codigo) != null ? true : false;
    }

}
