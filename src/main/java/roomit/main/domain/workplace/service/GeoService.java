package roomit.main.domain.workplace.service;

import org.springframework.stereotype.Service;
import roomit.main.global.util.GeoUtils;

import java.util.Map;

@Service
public class GeoService {
    private final GeoUtils geoUtils;

    public GeoService(GeoUtils geoUtils) {
        this.geoUtils = geoUtils;
    }

    public Map<String, Double> geocoding(String address) {
        return geoUtils.geocoding(address);
    }

    public Map<String, Double> parseAddress(String workplaceAddress) {
        return geoUtils.parseAddress(workplaceAddress);
    }
}

