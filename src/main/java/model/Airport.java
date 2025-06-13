package model;

public class Airport {
    private String name;
    private String code;
    private String city;
    private String country;
    private boolean status;

    public Airport(String code,String name, String city, String country) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.status = true;
    }
    public Airport(String code,String name, String city, String country, boolean status) {
        this.name = name;
        this.code = code;
        this.city = city;
        this.country = country;
        this.status = status;
    }

    public Airport() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String status="Inactive";
        if(this.status)
            status="Active";
        return "Airport{"+code+','+name+','+city+','+country+','+status+'}';
    }
}
