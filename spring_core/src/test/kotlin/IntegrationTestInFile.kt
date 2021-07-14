import com.infinum.academy.hw2.*
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.io.Resource
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(ApplicationConfiguration::class)
@TestPropertySource(properties=[
    "data.database-name=VelkaBP.txt",
    "data.username=kukecf",
    "data.password=frank",
    "repo.switch=false"
])
class IntegrationTestInFile @Autowired constructor(
    private val ctx: ApplicationContext
) {

    @Test
    @DisplayName("checking if required beans are present")
    fun checkAllBeans() {
        val possibleBeans = listOf(
            CourseService::class.java,
            DataSource::class.java,
            CourseRepository::class.java,
            ApplicationConfiguration::class.java,
            Resource::class.java
        )

        for (bean in possibleBeans) {
            assertThat(ctx.getBean(bean)).isNotNull
        }
    }

    @Test
    @DisplayName("should insert and find course by id")
    @DirtiesContext
    fun courseServiceTest1() {
        val courseService = ctx.getBean(CourseService::class.java)
        val id = courseService.insertIntoRepo("Psychology")
        val course = courseService.findCourseById(id)
        assertThat(course).isEqualTo(Course(id, "Psychology"))
    }

    @Test
    @DisplayName("should insert and delete course by id")
    @DirtiesContext
    fun courseServiceTest2() {
        val courseService = ctx.getBean(CourseService::class.java)
        val id = courseService.insertIntoRepo("Psychology")
        val course = courseService.deleteCourseById(id)
        assertThat(course).isEqualTo(Course(id, "Psychology"))
    }

    @Test
    @DisplayName("should throw exception when deleteCourseById is called on nonexisting entry")
    @DirtiesContext
    fun courseServiceTest3() {
        val courseService = ctx.getBean(CourseService::class.java)
        val id = courseService.insertIntoRepo("Psychology")
        courseService.deleteCourseById(id)
        assertThatThrownBy {
            courseService.deleteCourseById(id)
        }.isInstanceOf(CourseNotFoundException::class.java)
    }

    @Test
    @DisplayName("should throw exception for attempting to find or delete a negative id")
    @DirtiesContext
    fun courseServiceTest4() {
        val courseService = ctx.getBean(CourseService::class.java)
        assertThatThrownBy {
            courseService.deleteCourseById(-1)
        }.isInstanceOf(CourseNotFoundException::class.java)
        assertThatThrownBy {
            courseService.findCourseById(-1)
        }.isInstanceOf(CourseNotFoundException::class.java)
        courseService.insertIntoRepo("Psychology")
        assertThatThrownBy {
            courseService.deleteCourseById(-1)
        }.isInstanceOf(CourseNotFoundException::class.java)
        assertThatThrownBy {
            courseService.findCourseById(-1)
        }.isInstanceOf(CourseNotFoundException::class.java)
    }

}