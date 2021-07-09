import java.lang.RuntimeException
import java.util.*
import java.time.LocalDate
import java.util.stream.Collectors

object Library {
    // map of (invNo,(customerOIB,rentDueDate))
    private var rentals: MutableMap<Int, Pair<String, LocalDate>> = mutableMapOf()

    class BookNotFoundException(message: String) : RuntimeException(message)

    val books: Set<Book> = setOf(
        //for same books, but different instances (inventory numbers), we can use copy
        Book("It", "Stephen King", 1),
        Book("It", "Stephen King", 2),
        Book("It", "Stephen King", 3),
        Book("Harry Potter and the Deathly Hallows", "J. K. Rowling", 5),
        Book("Ready Player One", "Ernest Cline", 10),

        Book("The Great Gatsby", "Francis Scott Fitzgerald", 28),
        Book("The Great Gatsby", "Francis Scott Fitzgerald", 29),
        Book("The Last Wish", "Andrzej Sapkowski", 50),
        Book("Harry Potter and the Philosopher's stone", "J. K. Rowling", 17),
        Book("Ready Player Two", "Ernest Cline", 100)
    )

    private fun computeDate(duration: RentDuration): LocalDate {
        return when (duration) {
            RentDuration.TWO_WEEKS -> LocalDate.now().plusDays(14)
            RentDuration.MONTH -> LocalDate.now().plusMonths(1)
            RentDuration.TWO_MONTHS -> LocalDate.now().plusMonths(2)
        }
    }

    fun isBookAvailable(title: String, authorName: String): Boolean {
        return books.any { book ->
            book.title == title && book.authorName == authorName && !rentals.containsKey(book.inventoryNo)
        }
    }

    fun rentBook(
        title: String, authorName: String, customerOIB: String, duration:
        RentDuration
    ): Book? {
        val book = books.find { book ->
            book.title == title && book.authorName == authorName && !rentals.containsKey(book.inventoryNo)
        }
        if (book != null) {
            rentals[book.inventoryNo] = Pair(customerOIB, computeDate(duration))
            return book
        }
        return null
    }

    fun returnBook(book: Book) {
        if (rentals.remove(book.inventoryNo) == null) {
            throw BookNotFoundException("Book does not exist in current rental files.")
        }
    }

    fun isBookRented(book: Book): Boolean {
        return rentals.containsKey(book.inventoryNo)
    }

    fun getRentedBooks(customerOIB: String): List<Book> {
        return books.filter { book ->
            rentals[book.inventoryNo]?.first == customerOIB
        }
    }
}