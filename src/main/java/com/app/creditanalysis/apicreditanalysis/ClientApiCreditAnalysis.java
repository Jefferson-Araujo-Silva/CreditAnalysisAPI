package com.app.creditanalysis.apicreditanalysis;

import com.app.creditanalysis.apicreditanalysis.clientdto.ClientDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client", url = "${url.client.host}")
public interface ClientApiCreditAnalysis {
    // Esta url esta fora do padrão REST em clientes /clients?cpf={cpf}
    @GetMapping(path = "/search-by-cpf/cpf?cpf={cpf}")
    ClientDto getClientByCpf(@PathVariable(value = "cpf") String cpf);

    // Esta url esta fora do padrão REST em clientes /clients/{id}
    @GetMapping(path = "/search-by-id/id?id={id}")
    ClientDto getClientById(@PathVariable(value = "id") UUID id);
}
