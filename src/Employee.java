import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

class Employee
{
    private String empId;
    private String name;
    private int designation;
    private int age;
    private String gender;
    private String password;
    private LocalDate joiningDate;

    private HashMap<String, Integer> leaveBalance;
    private ArrayList<Leave> leaveRequests;

    public Employee() {

        leaveBalance = new HashMap<>();
        leaveRequests = new ArrayList<>();

        leaveBalance.put("CL", 10);
        leaveBalance.put("EL", 10);
        leaveBalance.put("SL", 12);
        leaveBalance.put("ML", 10);
        leaveBalance.put("PL", 10);
        leaveBalance.put("DL", 10);
        leaveBalance.put("LWP", 10);
    }

    public HashMap<String, Integer> getLeaveBalance() {
        return leaveBalance;
    }

    public ArrayList<Leave> getLeaveRequests() {
        return leaveRequests;
    }

    public String getEmpId() {
        return empId;
    }
    public String getName() {
        return name;
    }
    public int getDesignation() {
        return designation;
    }
    public int getAge() {
        return age;
    }
    public String getGender() {
        return gender;
    }
    public String getPassword() {
        return password;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDesignation(int designation) {
        this.designation = designation;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

//    protected boolean login(String empId, String password) {
//
//    }
}