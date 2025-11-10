package main.service;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import main.model.EmploymentType;
import main.model.Offer;
import main.model.User;
import main.repository.OfferRepository;
import main.repository.UserRepository;
import main.security.UserData;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final UserRepository userRepository;

    public OfferService(OfferRepository offerRepository, UserRepository userRepository) {
        this.offerRepository = offerRepository;
        this.userRepository = userRepository;
    }

    public void appliedOffer(UUID id, UserData userData) {

        Offer offer = offerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Quest not found with id: " + id));

        Optional<User> user = userRepository.findById(userData.getUserId());
        offer.setAppliedUser(user.get());

        offerRepository.save(offer);
    }

    public List<Offer> searchOffers(String keyword, String location, EmploymentType employmentType) {
        String normalizedKeyword = StringUtils.hasText(keyword) ? keyword.trim() : null;
        String normalizedLocation = StringUtils.hasText(location) ? location.trim() : null;

        return offerRepository.searchOffers(normalizedKeyword, normalizedLocation, employmentType);
    }
}

