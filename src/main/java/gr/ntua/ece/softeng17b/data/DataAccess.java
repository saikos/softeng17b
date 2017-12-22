package gr.ntua.ece.softeng17b.data;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class DataAccess {

    private static final int MAX_TOTAL_CONNECTIONS = 16;
    private static final int MAX_IDLE_CONNECTIONS   = 8;
    
    private DataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public void setup(String driverClass, String url, String user, String pass) throws SQLException {

        //initialize the data source
	    BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(driverClass);
        bds.setUrl(url);
        bds.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        bds.setMaxIdle(MAX_IDLE_CONNECTIONS);
        bds.setUsername(user);
        bds.setPassword(pass);
        bds.setValidationQuery("SELECT 1");
        bds.setTestOnBorrow(true);
        bds.setDefaultAutoCommit(true);

        //check that everything works OK        
        bds.getConnection().close();

        //initialize the jdbc template utilitiy
        jdbcTemplate = new JdbcTemplate(bds);

        //keep the dataSource for the low-level manual example to function (not actually required)
        dataSource = bds;
    }


    public List<Place> getAllPlaces() {
        return jdbcTemplate.query("select * from place", new PlaceRowMapper());
    }

    public Optional<Place> getPlace(Long id) {        
        Long[] params = new Long[]{id};
        List<Place> places = jdbcTemplate.query("select * from place where id = ?", params, new PlaceRowMapper());
        if (places.size() == 1)  {
            return Optional.of(places.get(0));
        }
        else {
            return Optional.empty();
        }        
    }


    public List<Place> getAllPlacesManually() {        
        
        List<Place> places = new ArrayList<>();
        String query = "select * from place";

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = dataSource.getConnection(); //borrow the connection from the pool
            stmt = con.createStatement();            
            rs = stmt.executeQuery(query);            
            while(rs.next()) {
                
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");

                Place place = new Place(id, name, description, latitude, longitude);
                places.add(place);                
            }

            return places;
        } 
        catch (SQLException e) {
            //report the error as a runtime exception
            throw new RuntimeException(e.getMessage(), e);
        } 
        finally {
            if (stmt != null) {
                try { 
                    stmt.close(); //closes the ResultSet too
                } 
                catch (Exception e) {
                    //log this (leak)
                } 
            }
            if (con != null) {
                try {
                    con.close(); //return the connection to the pool
                } 
                catch (Exception e) {
                    //log this (leak)
                }                    
            }    
        }
    }

}