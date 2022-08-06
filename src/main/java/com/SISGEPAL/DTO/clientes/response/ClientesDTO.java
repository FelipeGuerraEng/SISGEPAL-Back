package com.SISGEPAL.DTO.clientes.response;

import lombok.Data;
import java.util.List;

@Data
public class ClientesDTO {
    private List<ClienteDTO> clientes;
}