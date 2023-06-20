package org.gvormbrock.carpark.service;

import lombok.RequiredArgsConstructor;
import org.gvormbrock.carpark.exception.ErrorCode;
import org.gvormbrock.carpark.exception.NotFoundException;
import org.gvormbrock.carpark.model.Town;
import org.gvormbrock.carpark.model.Url;
import org.gvormbrock.carpark.repository.TownRepository;
import org.gvormbrock.carpark.repository.UrlRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlService {
    private final UrlRepository urlRepository;

    public Optional<Url> findById(Long id) {
        return urlRepository.findById(id);
    }

    public void save(Url url) {
        urlRepository.save(url);
    }

    @Transactional
    public List<Url> findByTownAndCountryCode(String townName, String countryCode) {
        return urlRepository.findByTownAndCountryCode(townName, countryCode);
    }
}
