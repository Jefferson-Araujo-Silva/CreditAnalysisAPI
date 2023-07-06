package com.app.creditanalysis.apicreditanalysis;

import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client", url = "${url.client.host}")
public interface ClientApiCreditAnalysis {
    @GetMapping(path = "?cpf={cpf}")
    List<ClientDto> getClientByCpf(@PathVariable(value = "cpf") String cpf);

    @GetMapping(path = "/{id}")
    ClientDto getClientById(@PathVariable(value = "id") UUID id);
}
