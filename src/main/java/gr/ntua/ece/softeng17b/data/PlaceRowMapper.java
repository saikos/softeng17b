package gr.ntua.ece.softeng17b.data;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

class PlaceRowMapper implements RowMapper<Place>  {

	@Override
    public Place mapRow(ResultSet rs, int rowNum) throws SQLException {
        
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        double latitude = rs.getDouble("latitude");
        double longitude = rs.getDouble("longitude");

        return new Place(id, name, description, latitude, longitude);
    }
}