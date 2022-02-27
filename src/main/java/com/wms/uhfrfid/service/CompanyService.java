package com.wms.uhfrfid.service;

import com.wms.uhfrfid.domain.Company;
import com.wms.uhfrfid.repository.CompanyRepository;
import com.wms.uhfrfid.service.dto.CompanyDTO;
import com.wms.uhfrfid.service.mapper.CompanyMapper;
import com.wms.uhfrfid.service.rfid.reader.PoolRFIDConsumer;
import com.wms.uhfrfid.service.rfid.reader.RFIDInventory;
import com.wms.uhfrfid.service.rfid.reader.RFIDReaderStartup;
import com.wms.uhfrfid.service.rfid.reader.RFIDTag;

import java.util.HashMap;
import java.util.Optional;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Company}.
 */
@Service
@Transactional
public class CompanyService {

    private final Logger log = LoggerFactory.getLogger(CompanyService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    public void scanTag() {
		Vector<Integer> antennaSet = null;
		PoolRFIDConsumer consumers;

		antennaSet = new Vector();
        antennaSet.clear();
        antennaSet.add(3);

		RFIDReaderStartup rfidReaderStartup = RFIDReaderStartup.getInstance();

        consumers = rfidReaderStartup.getConsumers();

        Callable<HashMap<String, RFIDTag>> rfidInventory = new RFIDInventory("     RFID LISTENER ==========> ", consumers,
        		antennaSet, 50, 10000);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HashMap<String, RFIDTag>> future = executorService.submit(rfidInventory);

        HashMap<String, RFIDTag> result = null;
		try {
			result = future.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Aloha Ending: " + result.size());
		Integer totalRead = 0;
		for (RFIDTag rfidTag : result.values()) {
			System.out.println("\t\t\tTag: " + rfidTag.get_EPC() + " @ Antenna: " + rfidTag.get_ANT_NUM() + " Count: " + rfidTag.getReadCount());
			totalRead += rfidTag.getReadCount();
		}
		System.out.println("Aloha Ending: Total read count -> " + totalRead);

        consumers.printConsumersStat();
    }

    /**
     * Save a company.
     *
     * @param companyDTO the entity to save.
     * @return the persisted entity.
     */
    public CompanyDTO save(CompanyDTO companyDTO) {
        log.debug("Request to save Company : {}", companyDTO);

        scanTag();

        Company company = companyMapper.toEntity(companyDTO);
        company = companyRepository.save(company);
        return companyMapper.toDto(company);
    }

    /**
     * Partially update a company.
     *
     * @param companyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CompanyDTO> partialUpdate(CompanyDTO companyDTO) {
        log.debug("Request to partially update Company : {}", companyDTO);

        return companyRepository
            .findById(companyDTO.getId())
            .map(existingCompany -> {
                companyMapper.partialUpdate(existingCompany, companyDTO);

                return existingCompany;
            })
            .map(companyRepository::save)
            .map(companyMapper::toDto);
    }

    /**
     * Get all the companies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Companies");
        return companyRepository.findAll(pageable).map(companyMapper::toDto);
    }

    /**
     * Get one company by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CompanyDTO> findOne(Long id) {
        log.debug("Request to get Company : {}", id);
        return companyRepository.findById(id).map(companyMapper::toDto);
    }

    /**
     * Delete the company by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Company : {}", id);
        companyRepository.deleteById(id);
    }
}
