package com.wms.uhfrfid.config;

import com.wms.uhfrfid.service.OrderItemService;
import com.wms.uhfrfid.service.ReaderService;
import com.wms.uhfrfid.service.ReceptionService;
import com.wms.uhfrfid.service.UHFRFIDAntennaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Profile("dev")
@Configuration
public class DevDataLoader {

    @Bean
    public CommandLineRunner loadData(ReceptionService receptionService, OrderItemService orderItemService,
                                      ReaderService readerService, UHFRFIDAntennaService antennaService) {
        return (args) -> {

            receptionService.findOne(1L, "user")
                .filter(e -> e.getOrderItems().isEmpty())
                .ifPresent(order -> {
                    PageRequest pageable = PageRequest.ofSize(2).withSort(Sort.by("id"));
                    orderItemService.findAll(pageable)
                        .stream()
                        .forEach(e -> {
                            e.setOrder(order);
                            orderItemService.save(e);
                        });
                });

            readerService.findOneDetailed(1L)
                .filter(e -> e.getUhfRFIDAntennas().isEmpty())
                .ifPresent(reader -> {
                    PageRequest pageable = PageRequest.ofSize(2).withSort(Sort.by("id"));
                    antennaService.findAll(pageable)
                        .stream()
                        .forEach(e -> {
                            e.setUhfRFIDReader(reader);
                            antennaService.save(e);
                        });
                });

        };
    }
}
