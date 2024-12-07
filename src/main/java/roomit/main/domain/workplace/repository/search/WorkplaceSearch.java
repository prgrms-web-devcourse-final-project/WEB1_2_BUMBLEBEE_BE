package roomit.main.domain.workplace.repository.search;

import java.util.List;
import roomit.main.domain.workplace.dto.response.DistanceWorkplaceResponse;

public interface WorkplaceSearch {
  List<DistanceWorkplaceResponse> findNearbyWorkplaces(Double longitude, Double latitude, Double maxDistance);
}
