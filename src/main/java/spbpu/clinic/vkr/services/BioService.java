package spbpu.clinic.vkr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spbpu.clinic.vkr.repository.BioRepository;

@Service
public class BioService {

    @Autowired
    private BioRepository bioRepository;

    public void deleteById(Long bioId) {
        bioRepository.deleteById(bioId);
    }
}
