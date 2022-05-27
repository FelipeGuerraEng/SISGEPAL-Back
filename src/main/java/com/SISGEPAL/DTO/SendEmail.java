package com.SISGEPAL.DTO;

import lombok.Data;

@Data
public class SendEmail {
    private String subject,  to,  name,  username, password;
}
