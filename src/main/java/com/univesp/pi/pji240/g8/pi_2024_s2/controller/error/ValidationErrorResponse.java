package com.univesp.pi.pji240.g8.pi_2024_s2.controller.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationErrorResponse {

    private String campo;
    private String erro;
}
