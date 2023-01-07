package Business;

import java.util.ArrayList;

public class Company {

    String name;
    ArrayList<Department> listOfDepartments;

    public Company(String name, ArrayList<Department> listOfDepartments) {
        this.name = name;
        this.listOfDepartments = listOfDepartments;
    }
}
