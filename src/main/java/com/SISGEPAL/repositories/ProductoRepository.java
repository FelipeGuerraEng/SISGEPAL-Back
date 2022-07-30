package com.SISGEPAL.repositories;

import com.SISGEPAL.entities.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoEntity,Integer>{
    public  ProductoEntity findByCodigo(String codigo);

    public ProductoEntity findByNombre(String nombre);

    public ProductoEntity findById(int id);
}
