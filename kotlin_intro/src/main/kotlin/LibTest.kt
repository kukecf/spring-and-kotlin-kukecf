fun availabilityTest(){
    val titles = listOf("It","Jack Reacher","Joyland")
    val authors = listOf("Stephen King", "Lee Child", "Stephen King")
    for(bookInfo in titles zip authors){
        print("Book: "+bookInfo+" ")
        println(Library.isBookAvailable(bookInfo.first,bookInfo.second))
    }
    println()
}

fun rentingTest(){
    val oib1 = "0123456"
    val oib2 = "6543210"
    val rentAttempts: MutableList<Book?> = mutableListOf()
    rentAttempts.add(Library.rentBook("It","Stephen King",oib1,RentDuration.TWO_WEEKS))
    rentAttempts.add(Library.rentBook("It","Stephen King",oib2,RentDuration.TWO_WEEKS))
    rentAttempts.add(Library.rentBook("The Shining","Stephen King",oib1,RentDuration.TWO_MONTHS))
    rentAttempts.add(Library.rentBook("Ready Player One","Ernest Cline",oib1,RentDuration.MONTH))
    println("Rent attempts: if successful, they are a book, if not, they are null.")
    for(attempt in rentAttempts){
        println(attempt)
    }
    val listRented1 = Library.getRentedBooks(oib1)
    for (book in listRented1){
        println("Osoba 1 je posudila "+book)
    }
    println()
}

fun returnTest(){
    val oib1 = "0123456"
    val oib2 = "6543210"
    val rentAttempts: MutableList<Book?> = mutableListOf()
    rentAttempts.add(Library.rentBook("It","Stephen King",oib1,RentDuration.TWO_WEEKS))
    rentAttempts.add(Library.rentBook("It","Stephen King",oib2,RentDuration.TWO_WEEKS))
    rentAttempts.add(Library.rentBook("The Shining","Stephen King",oib1,RentDuration.TWO_MONTHS))
    rentAttempts.add(Library.rentBook("Ready Player One","Ernest Cline",oib1,RentDuration.MONTH))
    rentAttempts.add(Library.rentBook("Ready Player Two","Ernest Cline",oib2,RentDuration.MONTH))
    var listRented1 = Library.getRentedBooks(oib1)
    for (book in listRented1){
        println("Osoba 1 je posudila "+book)
    }
    val listRented2 = Library.getRentedBooks(oib2)
    for (book in listRented2){
        println("Osoba 2 je posudila "+book)
    }
    println("\nKoje su knjige posudjene?")
    for (book in Library.books){
        println(book.toString() +", posudjena? "+Library.isBookRented(book))
    }
    println()
    for (book in rentAttempts){
        if(book!=null){
            println("Vracamo knjigu: "+book)
            Library.returnBook(book)
        }
    }
    listRented1 = Library.getRentedBooks(oib1)
    for (book in listRented1){
        println("Osoba 1 je posudila "+book)
    }

    val notInInv=Book("Comic Book","Stan Lee",2)
    try{
        Library.returnBook(notInInv)
    }
    catch(e:Exception){
        println("Uhvacena iznimka "+e)
    }

    try{
        for (book in rentAttempts){
            if(book!=null){
                println("Pokusavamo vratiti knjigu: "+book)
                Library.returnBook(book)
            }
        }
    }
    catch(e:Exception){
        println("Uhvacena iznimka "+e)
    }
}

fun main(){
    availabilityTest()
    rentingTest()
    returnTest()
}