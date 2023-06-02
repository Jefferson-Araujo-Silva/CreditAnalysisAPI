package com.app.creditanalysis.apicreditanalysis.clientdto;

import java.util.UUID;
import lombok.Builder;

public record ClientDto(UUID id) {
    @Builder(toBuilder = true)

    public ClientDto(UUID id) {
        this.id = id;
    }
}
