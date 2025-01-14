package Zarzadzanie.przekaznikami.Termometry;

import jakarta.persistence.*;

import jakarta.persistence.*;

@Entity
@Table(name = "termometry")
public class TermometrTabela {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    private String name;
    
    private String description;
    
    private String location;

    private double offset;

    private Double lastMeasurement;
    
    public double getOffset() {
        return offset;
    }

    public void setOffset(double offset) {
        this.offset = offset;
    }

    public Double getLastMeasurement() {
        return lastMeasurement;
    }

    public void setLastMeasurement(Double lastMeasurement) {
        this.lastMeasurement = lastMeasurement;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}