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
        log.debug(Thread.currentThread().getName() + " | " + Thread.currentThread().getId() + "scanning with {}", antenna);

        Map<String, RFIDTag> result;
        Vector<Integer> antennaSet = new Vector();
        antennaSet.add(3);
        RFIDInventory rfidInventory = new RFIDInventory("     RFID LISTENER ==========> ", RFIDReaderStartup.getInstance().getConsumers(),
                antennaSet, scanRequest.getCount(), TIMEOUT);

        Callable<HashMap<String, RFIDTag>> rfidInventoryCallable = rfidInventory; 
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HashMap<String, RFIDTag>> future = executorService.submit(rfidInventoryCallable);

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        log.debug(this.toString() + " - Total RFID tags read: " + result.size());
        TagsList rfidTags = new TagsList(result.values().stream().map(RFIDTag::get_EPC).collect(Collectors.toList()));
        rfidInventory.unregister();
        return rfidTags;
    }
}
