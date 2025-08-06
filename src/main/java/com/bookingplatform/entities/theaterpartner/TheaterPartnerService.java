package com.bookingplatform.entities.theaterpartner;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TheaterPartnerService {

    private final TheaterPartnerRepository partnerRepository;

    public TheaterPartnerService(TheaterPartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    public Flux<TheaterPartner> findAll() {
        return partnerRepository.findAll();
    }

    public Flux<TheaterPartner> findActivePartners() {
        return partnerRepository.findByActiveStatus(true);
    }

    public Mono<TheaterPartner> findById(Long id) {
        return partnerRepository.findById(id);
    }

    public Mono<TheaterPartner> save(TheaterPartner partner) {
        if (partner.getPartnerId() != null) {
            return Mono.error(new IllegalArgumentException("New partner cannot have an ID"));
        }
        partner.updateTimestamps();
        return partnerRepository.save(partner);
    }

    public Mono<TheaterPartner> update(Long id, TheaterPartner partner) {
        return partnerRepository.findById(id)
                .flatMap(existingPartner -> {
                    // Update fields
                    existingPartner.setCompanyName(partner.getCompanyName());
                    existingPartner.setContactPerson(partner.getContactPerson());
                    existingPartner.setEmail(partner.getEmail());
                    existingPartner.setPhone(partner.getPhone());
                    existingPartner.setAddress(partner.getAddress());
                    existingPartner.setTaxId(partner.getTaxId());
                    existingPartner.setActive(partner.getActive());
                    existingPartner.updateTimestamps();
                    return partnerRepository.save(existingPartner);
                });
    }

    public Mono<TheaterPartner> updateStatus(Long id, boolean isActive) {
        return partnerRepository.findById(id)
                .flatMap(partner -> {
                    partner.setActive(isActive);
                    partner.updateTimestamps();
                    return partnerRepository.save(partner);
                });
    }

    public Mono<Void> deleteById(Long id) {
        return partnerRepository.deleteById(id);
    }

    public Mono<Boolean> existsByEmail(String email) {
        return partnerRepository.existsByEmail(email);
    }
}
