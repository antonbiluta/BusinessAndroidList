package Business;

import java.util.ArrayList;
import java.util.Objects;

public class CompanyOperator {
    private int id;
    private ArrayList<Company> companies = new ArrayList<>();

    public void addDepartment(String companyName, Department department) {
        boolean isNewCompanyNeeded = true;
        for (Company company : companies) {
            if (Objects.equals(company.name, companyName)) {
                isNewCompanyNeeded = false;
                company.listOfDepartments.add(department);
                break;
            }
        }
        if (isNewCompanyNeeded) {
            ArrayList<Department> tempArrayList = new ArrayList<>();
            tempArrayList.add(department);
            companies.add(new Company(companyName, tempArrayList));
        }
    }

    public void delDepartment(int companyId, int departmentId) {
        companies.get(companyId).listOfDepartments.remove(departmentId);
    }

}
