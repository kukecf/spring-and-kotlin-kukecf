import com.infinum.academy.hw2.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationContext
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

@SpringJUnitConfig(classes = [ApplicationConfiguration::class])
@TestPropertySource("classpath:application.properties")
class IntegrationTest {
    @Autowired
    private lateinit var ctx: ApplicationContext

    @Test
    @DisplayName("pocetni")
    fun checkAllBeans(@Value("\${repo.switch}") switch: String) {
        val possibleBeans = mutableListOf(
            CourseService::class.java,
            DataSource::class.java,
            CourseRepository::class.java,
            ApplicationConfiguration::class.java
        )
        when (switch) {
            "turned-on" -> possibleBeans.add(InMemoryCourseRepository::class.java)
            else -> possibleBeans.add(InFileCourseRepository::class.java)
        }

        for (bean in possibleBeans) {
            Assertions.assertThat(ctx.getBean(bean)).isNotNull
        }
    }

    @Test
    @DisplayName("service test")
    @DirtiesContext
    fun courseServiceTest(){
        val courseService = ctx.getBean(CourseService::class.java)
        val id = courseService.insertIntoRepo("Psychology")
        val course = courseService.findCourseById(id)
        Assertions.assertThat(course).isEqualTo(Course(id,"Psychology"))
        val course2 = courseService.deleteCourseById(id)
        Assertions.assertThat(course2).isEqualTo(Course(id,"Psychology"))
        Assertions.assertThatThrownBy {
             courseService.deleteCourseById(id)
        }.isInstanceOf(CourseNotFoundException::class.java)
    }
    
}