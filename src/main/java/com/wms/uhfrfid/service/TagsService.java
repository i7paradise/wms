package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.UHFRFIDAntenna;
import com.wms.uhfrfid.repository.UHFRFIDAntennaRepository;
import com.wms.uhfrfid.service.dto.ScanRequest;
import com.wms.uhfrfid.service.dto.TagsList;
import com.wms.uhfrfid.service.rfid.reader.RFIDInventory;
import com.wms.uhfrfid.service.rfid.reader.RFIDReaderStartup;
import com.wms.uhfrfid.service.rfid.reader.RFIDTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagsService {

    private static final int TIMEOUT = 10000;
    private final Logger log = LoggerFactory.getLogger(TagsService.class);

    private final UHFRFIDAntennaRepository antennaRepository;

    public TagsService(UHFRFIDAntennaRepository antennaRepository) {
        this.antennaRepository = antennaRepository;
    }

    public TagsList scan(ScanRequest scanRequest) {
        UHFRFIDAntenna antenna = antennaRepository.getById(scanRequest.getAntennaId());
        log.debug("scanning with {}", antenna);

        // ###
        Vector<Integer> antennaSet = new Vector();
        antennaSet.add(3);
        Callable<HashMap<String, RFIDTag>> rfidInventory = new RFIDInventory("     RFID LISTENER ==========> ", RFIDReaderStartup.getInstance().getConsumers(),
            antennaSet, scanRequest.getCount(), TIMEOUT);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HashMap<String, RFIDTag>> future = executorService.submit(rfidInventory);

        Map<String, RFIDTag> result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Aloha Ending: " + result.size());
        int totalRead = 0;
        for (RFIDTag rfidTag : result.values()) {
            System.out.println("\t\t\tTag: " + rfidTag.get_EPC() + " @ Antenna: " + rfidTag.get_ANT_NUM() + " Count: " + rfidTag.getReadCount());
            totalRead += rfidTag.getReadCount();
        }
        System.out.println("Aloha Ending: Total read count -> " + totalRead);
        return new TagsList(
            result.values().stream().map(RFIDTag::get_EPC).collect(Collectors.toList())
        );
    }


}
