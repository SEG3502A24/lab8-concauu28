package seg3x02.employeeGql.resolvers
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import seg3x02.employeeGql.entity.Employee
import seg3x02.employeeGql.repository.EmployeesRepository
import seg3x02.employeeGql.resolvers.types.CreateEmployeeInput
import java.util.*

@Controller
class EmployeesResolver(private val employeesRepository: EmployeesRepository) {

    // Query to get all employees
    @QueryMapping
    fun employees(): List<Employee> = employeesRepository.findAll()

    // Query to get an employee by ID
    @QueryMapping
    fun employeeById(@Argument id: String): Employee? = employeesRepository.findById(id).orElse(null)

    // Mutation to create a new employee
    @MutationMapping
    fun createEmployee(@Argument input: CreateEmployeeInput): Employee {
        val employee = Employee(
            name = input.name ?: throw IllegalArgumentException("Name is required"),
            dateOfBirth = input.dateOfBirth ?: "",
            city = input.city ?: "",
            salary = input.salary ?: 0.0f,
            gender = input.gender,
            email = input.email
        )
        employee.id = UUID.randomUUID().toString()
        return employeesRepository.save(employee)
    }

    // Mutation to delete an employee by ID
    @MutationMapping
    fun deleteEmployee(@Argument id: String): Boolean {
        return if (employeesRepository.existsById(id)) {
            employeesRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    // Mutation to update an existing employee by ID
    @MutationMapping
    fun updateEmployee(@Argument id: String, @Argument input: CreateEmployeeInput): Employee? {
        val employee = employeesRepository.findById(id).orElse(null) ?: return null
        val updatedEmployee = employee.copy(
            name = input.name ?: employee.name,
            dateOfBirth = input.dateOfBirth ?: employee.dateOfBirth,
            city = input.city ?: employee.city,
            salary = input.salary ?: employee.salary,
            gender = input.gender ?: employee.gender,
            email = input.email ?: employee.email
        )
        return employeesRepository.save(updatedEmployee)
    }
}

