package com.app.creditanalysis.apicreditanalysis.clientdto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

public record ClientDto(UUID id, String name, String cpf, LocalDate dateOfBirth, AddressResponse address) {
    @Builder
    public ClientDto {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record AddressResponse(UUID id, String street, String neighborhood, String state, Integer numberOfHouse, String complement, String cep,
                                  LocalDateTime createdAt, LocalDateTime updatedAt) {
        @Builder
        public AddressResponse {
        }
    }
}
