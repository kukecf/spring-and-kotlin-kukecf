import java.lang.RuntimeException
import java.util.*

object Library {
    val books: MutableSet<Book> = mutableSetOf()
    private var rentals: MutableMap<Int,Pair<String, Date>?> = mutableMapOf() // map of (invNo,(customerOIB,rentDueDate))
    class BookNotFoundException : RuntimeException{
        constructor() : this("")
        constructor(message:String):super(message)
    }
    class BookInStockException : RuntimeException{
        constructor() : this("")
        constructor(message:String):super(message)
    }
    init{
        //for same books, but different instances (inventory numbers), we can use copy
        books.add(Book("It","Stephen King", 1))
        books.add(Book("Doctor Sleep","Stephen King", 7))
        books.add(Book("A Random Book","Fran Kukec", 8))
        books.add(Book("Harry Potter and the Deathly Hallows","J. K. Rowling", 5))
        books.add(Book("Ready Player One","Ernest Cline", 10))

        books.add(Book("The Great Gatsby","Francis Scott Fitzgerald", 28))
        books.add(Book("A Game Of Thrones","George R. R. Martin", 52))
        books.add(Book("The Last Wish","Andrzej Sapkowski", 50))
        books.add(Book("Harry Potter and the Philosopher's stone","J. K. Rowling", 17))
        books.add(Book("Ready Player Two","Ernest Cline", 100))
    }

    init{
        rentals.put(1,null)
        rentals.put(7,null)
        rentals.put(8,null)
        rentals.put(5,null)
        rentals.put(10,null)

        rentals.put(28,null)
        rentals.put(52,null)
        rentals.put(50,null)
        rentals.put(17,null)
        rentals.put(100,null)
    }

    private fun computeDate(duration:RentDuration):Date{
        val cal=Calendar.getInstance()
        when(duration){
            RentDuration.TWO_WEEKS -> cal.add(Calendar.DATE, 14)
            RentDuration.MONTH -> cal.add(Calendar.MONTH, 1)
            RentDuration.TWO_MONTHS -> cal.add(Calendar.MONTH, 2)
        }
        return cal.time
    }

    fun isBookAvailable(title: String, authorName: String):Boolean{
        for(book in books){
            if(book.title==title && book.authorName==authorName){
                if(rentals[book.inventoryNo] is Pair<String,Date>) {
                    return false
                }
                    return true
            }
        }
        return false
    }

    fun rentBook(title: String, authorName: String, customerOIB: String, duration:
    RentDuration):Book?{
        for(book in books){
            if(book.title==title && book.authorName==authorName && rentals[book.inventoryNo]==null){
                rentals[book.inventoryNo]=Pair(customerOIB,computeDate(duration))
                return book
            }
        }
        return null
    }

    fun returnBook(book:Book){
        if(!rentals.containsKey(book.inventoryNo)||!books.contains(book)) throw BookNotFoundException("Book does not exist in rental files or inventory.")
        if(rentals.containsKey(book.inventoryNo)){
            if(rentals[book.inventoryNo]==null) throw BookInStockException("Book was not rented in the first place.")
        }
        rentals.replace(book.inventoryNo,null)
    }

    fun isBookRented(book: Book):Boolean{
        if(books.contains(book) && rentals.containsKey(book.inventoryNo)){
            return rentals[book.inventoryNo] is Pair<String,Date>
        }
        return false
    }

    fun getRentedBooks(customerOIB: String):List<Book>{
        val bookList: MutableList<Book> = mutableListOf()
        for(book in books){
            if(rentals.containsKey(book.inventoryNo)){
                val rentalInfo:Pair<String, Date>? =rentals[book.inventoryNo]
                if (rentalInfo != null) {
                    if (rentalInfo.first == customerOIB) {
                        bookList.add(book)
                    }
                }
            }
        }
        return bookList
    }
}