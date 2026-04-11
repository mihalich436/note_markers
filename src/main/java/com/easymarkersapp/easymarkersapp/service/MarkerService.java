package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.model.Marker;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.repository.MarkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MarkerService {
    @Autowired
    private MarkerRepository markerRepository;
    @Autowired
    private MapService mapService;

    public Marker save(Marker marker) {
        return markerRepository.save(marker);
    }

    public Marker findByIdAndCheckAccess(Long id, User user) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            if (mapService.existsByIdAndUser(markerOptional.get().getMapId(), user)) {
                return marker;
            }
        }
        return null;
    }

    public boolean existsByIdAndCheckAccess(Long id, User user) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        return markerOptional.filter(marker -> mapService.existsByIdAndUser(marker.getMapId(), user)).isPresent();
    }
}
