package com.app.creditanalysis.apicreditanalysis.clientdto;

import java.util.UUID;
import lombok.Builder;

public record ClientDto(UUID id, String cpf) {
    @Builder(toBuilder = true)

    public ClientDto(UUID id, String cpf) {
        this.id = id;
        this.cpf = cpf;
    }
}
