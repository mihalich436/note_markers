package com.easymarkersapp.easymarkersapp.service;

import com.easymarkersapp.easymarkersapp.dto.marker.MarkerRequest;
import com.easymarkersapp.easymarkersapp.model.AccessRole;
import com.easymarkersapp.easymarkersapp.model.Marker;
import com.easymarkersapp.easymarkersapp.model.ProjectAccess;
import com.easymarkersapp.easymarkersapp.model.User;
import com.easymarkersapp.easymarkersapp.repository.MarkerRepository;
import jakarta.transaction.Transactional;
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

    @Transactional
    public Marker findByIdAndCheckAccess(Long id, User user, AccessRole requiredRole) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(requiredRole)) {
                return marker;
            }
        }
        return null;
    }

    @Transactional
    public Marker updateByIdAndCheckAccess(MarkerRequest request, Long id, User user, AccessRole requiredRole) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            boolean visibilityPrev = marker.getVisibility();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(requiredRole)) {
                request.updateMarker(marker);
                Marker updatedMarker = markerRepository.save(marker);
                if (!visibilityPrev && updatedMarker.getVisibility()) {
                    updatedMarker.getMessages();
                }
                return updatedMarker;
            }
        }
        return null;
    }

    public void deleteById(Long id) {
        markerRepository.deleteById(id);
    }

    @Transactional
    public boolean deleteByIdAndCheckAccess(Long id, User user) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        if (markerOptional.isPresent()) {
            Marker marker = markerOptional.get();
            ProjectAccess access = mapService.getRoleByIdAndUser(marker.getMapId(), user);
            if (access != null && access.getRole().hasAccess(AccessRole.EDITOR)) {
                markerRepository.delete(marker);
                return true;
            }
        }
        return false;
    }

    public boolean existsByIdAndCheckAccess(Long id, User user) {
        Optional<Marker> markerOptional = markerRepository.findById(id);
        return markerOptional.filter(marker -> mapService.existsByIdAndUser(marker.getMapId(), user)).isPresent();
    }
}
