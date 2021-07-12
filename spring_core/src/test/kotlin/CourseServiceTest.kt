import com.infinum.academy.hw2.Course
import com.infinum.academy.hw2.CourseNotFoundException
import com.infinum.academy.hw2.CourseRepository
import com.infinum.academy.hw2.CourseService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CourseRepoTest {
    private lateinit var courseService: CourseService
    private val courseRepository = mockk<CourseRepository>()

    @BeforeEach
    fun setUp() {
        courseService = CourseService(courseRepository)
    }

    @Test
    fun testInsert() {
        every {
            courseRepository.insert("Algorithms")
        } returns 1
        val act = courseService.insertIntoRepo("Algorithms")
        Assertions.assertThat(act).isEqualTo(1)
        verify(exactly = 1) {
            courseRepository.insert(any())
        }
    }

    @Test
    fun testFind() {
        every {
            courseRepository.findById(1)
        } returns Course(1, "French")
        every {
            courseRepository.findById(2)
        } throws CourseNotFoundException(2)

        val first = courseService.findCourseById(1)
        Assertions.assertThat(first).isEqualTo(Course(1, "French"))
        verify(exactly = 1) {
            courseRepository.findById(1)
        }
        Assertions.assertThatThrownBy {
            courseService.findCourseById(2)
        }.isInstanceOf(CourseNotFoundException::class.java)
    }

    @Test
    fun testDelete() {
        every {
            courseRepository.deleteById(1)
        } returns Course(1, "Sociology")
        every {
            courseRepository.deleteById(2)
        } throws CourseNotFoundException(2)

        val first = courseService.deleteCourseById(1)
        Assertions.assertThat(first).isEqualTo(Course(1, "Sociology"))
        verify(exactly = 1) {
            courseRepository.deleteById(1)
        }
        verify(exactly=0){
            courseRepository.insert(any())
        }
        Assertions.assertThatThrownBy {
            courseService.deleteCourseById(2)
        }.isInstanceOf(CourseNotFoundException::class.java).hasMessage("com.infinum.academy.hw2.Course with and ID 2 not found")
    }
}