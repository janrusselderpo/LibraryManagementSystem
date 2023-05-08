package com.example.librarymanagementsystem
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun main(){
    val library = Library(ArrayList())
    displayMenu()
    var input: MainMenuOption
    do {
        input = readMainMenuChoice()
        when (input) {
            MainMenuOption.ADD_BOOK -> {
                val book = readBookInfo()
                library.addBook(book.title, book.author, book.publicationDate)
            }
            MainMenuOption.REMOVE_BOOK -> {
                print("Enter the book title to remove: ")
                val title = readLine()?.trim() ?: ""
                library.removeBook(title)
            }
            MainMenuOption.DISPLAY_ALL_BOOKS -> {
                library.displayAllBooks()
                println()
            }
            MainMenuOption.CREATE_LOAN -> {
                val borrower = readBorrowerInfo()
                print("Enter the book title to borrow:")
                val bookToBorrow:String = readLine()!!
                library.createLoan(bookToBorrow, borrower)
                println()
            }
            MainMenuOption.DISPLAY_ALL_LOANS -> {
                library.displayAllLoans()
                println()

            }
            MainMenuOption.EXIT -> println("Exiting program.")
        }
    } while (input != MainMenuOption.EXIT)
}
fun displayMenu(){
    println("----- Library Management System -----")
    println("1 - add book \n2 - remove book \n3 - display all books \n4 - create loan \n5 - display loans \n6 - exit")
    println("-------------------------------------")
}
fun readMainMenuChoice(): MainMenuOption {
    while (true) {
        print("Enter your choice: ")
        when (readLine()) {
            "1" -> return MainMenuOption.ADD_BOOK
            "2" -> return MainMenuOption.REMOVE_BOOK
            "3" -> return MainMenuOption.DISPLAY_ALL_BOOKS
            "4" -> return MainMenuOption.CREATE_LOAN
            "5" -> return MainMenuOption.DISPLAY_ALL_LOANS
            "6" -> return MainMenuOption.EXIT
            else -> println("Invalid choice. Please try again.")
        }
    }
}
fun readBookInfo(): Book {
    print("Enter the book title: ")
    val title = readLine()!!
    print("Enter the book author: ")
    val author = readLine()!!
    var publicationDate: LocalDate? = null
    while (publicationDate == null) {
        print("Enter the book's publication date (yyyy-mm-dd): ")
        try {
            publicationDate = LocalDate.parse(readLine())
        } catch (e: DateTimeParseException) {
            println("Invalid date format. Please follow the correct format(yyyy-mm-dd).")
        }
    }
    return Book(title, author, publicationDate, BookStatus.AVAILABLE)
}
fun readBorrowerInfo(): Borrower {
    print("Enter the borrower's library card number: ")
    val libraryCardNumber = readLine()!!
    print("Enter the borrower's name: ")
    val name = readLine()!!
    print("Enter the borrower's phone number: ")
    val phone = readLine()!!
    return Borrower(libraryCardNumber, name, phone)
}

class Library(private val libraryBooks: ArrayList<LibraryBook>){
    private val loan = LibraryLoan(ArrayList())

    fun addBook(title: String, author: String, publicationDate: LocalDate){
        val book = Book(title, author, publicationDate, BookStatus.AVAILABLE)
        val libraryBook = LibraryBook(book, BookStatus.AVAILABLE)
        libraryBooks.add(libraryBook)
        println("Book has been added to library.\n")
    }
    fun removeBook(title: String){
        val bookToRemove = libraryBooks.find {it.book.title == title}
        if(bookToRemove != null){
            libraryBooks.remove(bookToRemove)
            println("Book has been removed.\n")
        }else{
            print("Book doesn't exist")
        }
    }
    fun displayAllBooks(){
        for(books in libraryBooks){
            println("Book:${books.book.title} | Status:${books.status}")
        }
    }
    fun createLoan(title: String, borrower: Borrower) {
        val dueDate = LocalDate.now().plusDays(15)

        val isBookAvailable = libraryBooks.filter { it.book.title == title && it.status == BookStatus.AVAILABLE }
        if (isBookAvailable.isEmpty()) {
            println("Sorry, that book is not available for loan.")
        } else {
            val bookToBorrow = isBookAvailable.first()
            val loanItem = Loan(bookToBorrow.book, borrower.name, dueDate, null)
            loan.loanList.add(loanItem)
            bookToBorrow.status = BookStatus.ON_LOAN
            println("The book '${bookToBorrow.book.title}' has been borrowed by ${borrower.name}.")
        }
    }
    fun displayAllLoans(){
        for(loans in loan.loanList){
            println("Title: ${loans.book.title} | Borrower: ${loans.borrower} | Due date: ${loans.dueDate}")
        }
    }
}
class LibraryLoan(
    var loanList: ArrayList<Loan>
)
interface Person {
    val name: String
    val phone: String
}
data class Book(
    val title: String,
    val author: String,
    val publicationDate: LocalDate,
    var status: BookStatus
)
data class Borrower(
    val libraryCardNumber: String,
    override val name: String,
    override val phone: String
) : Person
data class Loan(
    val book: Book,
    val borrower: String,
    val dueDate: LocalDate,
    val returnDate: LocalDate?
)
data class LibraryBook(
    val book: Book,
    var status: BookStatus
)
enum class BookStatus{
    AVAILABLE,
    ON_LOAN
}
enum class MainMenuOption{
    ADD_BOOK,
    REMOVE_BOOK,
    DISPLAY_ALL_BOOKS,
    CREATE_LOAN,
    DISPLAY_ALL_LOANS,
    EXIT
}
//fun addData(library: Library){
//    val date = LocalDate.of(1997, 6, 26)
//    library.addBook("Hairy Potter and the Sorcerer's Bone", "J. K. Running", date)
//
//    val date2 = LocalDate.of(1996, 8, 1)
//    library.addBook("A Game of Chairs", "George M. M, Latin", date2)
//
//    val borrower = Borrower("2019-00123-BN-0", "Steve Petrick", "09179874473")
//    library.createLoan("Hairy Potter and the Sorcerer's Bone", borrower)
//
//}







