package util;

import java.util.ArrayList;
import java.util.List;

public class CityDTO {
    private int id;
    private String name;
    private List<StationDTO> stations;
    
    public CityDTO(int id, String name) {
        this.id = id;
        this.name = name;
        this.stations = new ArrayList<>();
    }
    
    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<StationDTO> getStations() { return stations; }
    public void setStations(List<StationDTO> stations) { this.stations = stations; }
}
