package ru.mirea.kovalikaa.employeedb;

//import androidx.room.Entity;
//import androidx.room.PrimaryKey;
//@Entity
//public class Employee {
//    @PrimaryKey(autoGenerate = true)
//    public long id;
//    public String name;
//    public String superPower;
//
//    public Employee(String name, String superPower){
//        this.name = name;
//        this.superPower = superPower;
//    }
//}
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Employee {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String element;

    public Employee(String name, String element) {
        this.name = name;
        this.element = element;
    }
}