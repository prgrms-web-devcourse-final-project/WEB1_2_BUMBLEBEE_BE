package roomit.main.global.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PointUtil {
    private static final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory();

    @Value("${gis.srid}")
    private int srid;

    public Point createPoint(double longitude, double latitude) {
        Point point = GEOMETRY_FACTORY.createPoint(new Coordinate(longitude, latitude));
        point.setSRID(srid); // 주입받은 SRID 값 설정
        return point;
    }
}