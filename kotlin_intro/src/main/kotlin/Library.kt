import java.lang.RuntimeException
import java.util.*
import java.time.LocalDate

object Library {
    private var rentals: MutableMap<Int,Pair<String, LocalDate>> = mutableMapOf() // map of (invNo,(customerOIB,rentDueDate))
    class BookNotFoundException(message: String) : RuntimeException(message){}
    val books: Set<Book> = setOf(
        //for same books, but different instances (inventory numbers), we can use copy
        Book("It","Stephen King", 1),
        Book("It","Stephen King", 2),
        Book("It","Stephen King", 3),
        Book("Harry Potter and the Deathly Hallows","J. K. Rowling", 5),
        Book("Ready Player One","Ernest Cline", 10),

        Book("The Great Gatsby","Francis Scott Fitzgerald", 28),
        Book("The Great Gatsby","Francis Scott Fitzgerald", 29),
        Book("The Last Wish","Andrzej Sapkowski", 50),
        Book("Harry Potter and the Philosopher's stone","J. K. Rowling", 17),
        Book("Ready Player Two","Ernest Cline", 100)
    )

    private fun computeDateOld(duration:RentDuration):Date{
        val cal=Calendar.getInstance()
        when(duration){
            RentDuration.TWO_WEEKS -> cal.add(Calendar.DATE, 14)
            RentDuration.MONTH -> cal.add(Calendar.MONTH, 1)
            RentDuration.TWO_MONTHS -> cal.add(Calendar.MONTH, 2)
        }
        return cal.time
    }

    //mislili ste ovako?
    private fun computeDate(duration:RentDuration):LocalDate{
        return when(duration){
            RentDuration.TWO_WEEKS -> LocalDate.now().plusDays(14)
            RentDuration.MONTH -> LocalDate.now().plusMonths(1)
            RentDuration.TWO_MONTHS -> LocalDate.now().plusMonths(2)
        }
    }

    fun isBookAvailable(title: String, authorName: String):Boolean{
        for(book in books){
            if(book.title==title && book.authorName==authorName){
                return !rentals.containsKey(book.inventoryNo)
            }
        }
        return false
    }

    fun rentBook(title: String, authorName: String, customerOIB: String, duration:
    RentDuration):Book?{
        for(book in books){
            if(book.title==title && book.authorName==authorName && !rentals.containsKey(book.inventoryNo)){
                rentals[book.inventoryNo]=Pair(customerOIB,computeDate(duration))
                return book
            }
        }
        return null
    }

    fun returnBook(book:Book){
        if(rentals.remove(book.inventoryNo) == null) throw BookNotFoundException("Book does not exist in current rental files.")
    }

    fun isBookRented(book: Book):Boolean{
        return rentals.containsKey(book.inventoryNo)
    }

    fun getRentedBooks(customerOIB: String):List<Book>{
        val bookList: MutableList<Book> = mutableListOf()
        for(book in books){
            val rentalInfo:Pair<String, LocalDate>? =rentals[book.inventoryNo]
            if (rentalInfo != null) {
                if (rentalInfo.first == customerOIB) {
                    bookList.add(book)
                }
            }
        }
        return bookList
    }
}