package net.javaguides.springboot.repository;

import net.javaguides.springboot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmailIgnoreCase(String email);

    //Define custom query using JPQL with index parameter
    @Query("select e from Employee e where e.firstName = ?1 and e.email = ?2")
    Employee findByJpqlIndexParam(String name, String email);

    //Define custom query using JPQL with named parameter
    @Query("select e from Employee e where e.firstName =:name and e.email =:email")
    Employee findByJpqlNamedParameters(@Param("name")String name, @Param("email") String email);

    //Define custom native query using index parameter
    @Query(value = "select * from employees e where e.first_name =?1 and e.email =?2",  nativeQuery = true)
    Employee findByNativeQueryIndexParameters(String name, String email);

    //Define custom native query using named parameter
    @Query(value = "select * from employees e where e.first_name =:name and e.email =:email",  nativeQuery = true)
    Employee findByNativeQueryNamedParameters(@Param("name") String name, @Param("email") String email);
}
