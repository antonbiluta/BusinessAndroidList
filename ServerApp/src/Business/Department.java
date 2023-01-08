package Business;

public class Department {

    String nameOfDepartment;
    String fioManager;
    String managerStartDate;
    String streetAddress;
    int postalCode;
    String city;
    String region;
    int countEmployee;

    public Department(String nameOfDepartment, String fioManager, String managerStartDate, String streetAddress, int postalCode, String city, String region, int countEmployee) {
        this.nameOfDepartment = nameOfDepartment;
        this.fioManager = fioManager;
        this.managerStartDate = managerStartDate;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.region = region;
        this.countEmployee = countEmployee;
    }
}
