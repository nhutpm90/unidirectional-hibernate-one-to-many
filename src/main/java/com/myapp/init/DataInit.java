package com.myapp.init;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myapp.dao.DepartmentDAO;
import com.myapp.dao.EmployeeDAO;
import com.myapp.entity.Department;
import com.myapp.entity.Employee;

@Component
public class DataInit implements ApplicationRunner {

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DepartmentDAO departmentDAO;

	@Autowired
	private EmployeeDAO employeeDAO;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		boolean insertDataOnStartUp = true;
		if (insertDataOnStartUp) {
			Employee employee1 = new Employee("Adam");
			Employee employee2 = new Employee("Miller");
			Employee employee3 = new Employee("Smith");
			
			Department department = new Department("IT Department");
			department.getEmployees().add(employee1);
			department.getEmployees().add(employee2);
			department.getEmployees().add(employee3);
			
			departmentDAO.save(department);
			
			/*
			 * the above implementation causes 7 sql insert
			 * 
			 * 
				Hibernate: insert into department (dpt_id, name) values (null, ?)
				Hibernate: insert into employee (emp_id, name) values (null, ?)
				Hibernate: insert into employee (emp_id, name) values (null, ?)
				Hibernate: insert into employee (emp_id, name) values (null, ?)
				Hibernate: insert into department_employees (department_dpt_id, employees_emp_id) values (?, ?)
				Hibernate: insert into department_employees (department_dpt_id, employees_emp_id) values (?, ?)
				Hibernate: insert into department_employees (department_dpt_id, employees_emp_id) values (?, ?)
			 */
		}
		
		boolean updateDataOnStartUp = true;
		if(updateDataOnStartUp) {
			Department department = departmentDAO.findById(1l).orElse(null);
			/*
			 Unidirectional → In this type of association, only source entity has a relationship field that refers to the target entity. We can navigate this type of association from one side.
			 
			 select department0_.dpt_id as dpt_id1_0_0_, department0_.name as name2_0_0_, employees1_.department_dpt_id as departme1_1_1_, employee2_.emp_id as employee2_1_1_, employee2_.emp_id as emp_id1_2_2_, employee2_.name as name2_2_2_ 
				from department department0_ 
					left outer join department_employees employees1_ on department0_.dpt_id=employees1_.department_dpt_id 
					left outer join employee employee2_ on employees1_.employees_emp_id=employee2_.emp_id 
						where department0_.dpt_id=? 
				
				DPT_ID1_0_0_  	NAME2_0_0_  	DEPARTME1_1_1_  	EMPLOYEE2_1_1_  	EMP_ID1_2_2_  	NAME2_2_2_  
				1				IT Department		1					1					1				Adam
				1				IT Department		1					2					2				Miller
				1				IT Department		1					3					3				Smith
			 */
			
			List<Employee> employees = department.getEmployees();
//			department.getEmployees().size()
//			System.out.println(objectMapper.writeValueAsString(department));
//			employees.forEach(employee -> {
//				System.out.println(employee);
//			});
		}
	}

}