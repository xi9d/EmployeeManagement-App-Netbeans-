package com.employee;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named("employeeBean")
@SessionScoped
public class EmployeeBean implements Serializable {

    private static final long serialVersionUID = 1L;
    private Employee employee = new Employee();
    private Employee selectedEmployee;
    private List<Employee> employees;
    private EmployeeDAO employeeDAO = new EmployeeDAO();

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Constructor
    public EmployeeBean() {
        employees = employeeDAO.getAllEmployees();
    }

    // Save employee (create or update)
    public void saveEmployee() {
        if (!validateInput()) {
            return;
        }

        boolean success;
        if (employee.getId() == 0) {
            // Create new employee
            success = employeeDAO.addEmployee(employee);
            if (success) {
                employees = employeeDAO.getAllEmployees();
                employee = new Employee(); // Reset form
            }
        } else {
            // Update existing employee
            success = employeeDAO.updateEmployee(employee);
            if (success) {
                employees = employeeDAO.getAllEmployees();
                employee = new Employee(); // Reset form
            }
        }
    }

    // Delete employee
    public void deleteEmployee(int id) {
        boolean success = employeeDAO.deleteEmployee(id);
        if (success) {
            employees = employeeDAO.getAllEmployees();
        }
    }

    // Prepare form for editing
    public void prepareEdit(Employee emp) {
        employee = new Employee();
        employee.setId(emp.getId());
        employee.setFirstName(emp.getFirstName());
        employee.setLastName(emp.getLastName());
        employee.setEmail(emp.getEmail());
        employee.setDepartment(emp.getDepartment());
        employee.setSalary(emp.getSalary());
    }

    // Reset form
    public void reset() {
        employee = new Employee();
    }

    // Validate input fields
    private boolean validateInput() {
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty()) {
            return false;
        }
        if (employee.getLastName() == null || employee.getLastName().trim().isEmpty()) {
            return false;
        }
        if (employee.getEmail() == null || employee.getEmail().trim().isEmpty()) {
            return false;
        }
        if (!EMAIL_PATTERN.matcher(employee.getEmail()).matches()) {
            return false;
        }
        return true;
    }

    // Getters and setters
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public List<Employee> getEmployees() {
        if (employees == null) {
            employees = employeeDAO.getAllEmployees();
        }
        return employees;
    }
}
