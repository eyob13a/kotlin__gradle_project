package org.dbu.library.ui

import org.dbu.library.model.Book
import org.dbu.library.model.Patron
import org.dbu.library.repository.LibraryRepository
import org.dbu.library.service.BorrowResult
import org.dbu.library.service.LibraryService

fun handleMenuAction(
    choice: String,
    service: LibraryService,
    repository: LibraryRepository
): Boolean {

    return when (choice) {

        "1" -> {
            addBook(service)
            true
        }

        "2" -> {
            registerPatron(repository)
            true
        }

        "3" -> {
            borrowBook(service)
            true
        }

        "4" -> {
            returnBook(service)
            true
        }

        "5" -> {
            search(service)
            true
        }

        "6" -> {
            listAllBooks(repository)
            true
        }

        "7" -> {
            viewAllPatrons(repository)
            true
        }

        "0" -> false

        else -> {
            println("Invalid option")
            true
        }
    }
}

private fun addBook(service: LibraryService) {
    print("ISBN: ")
    val isbn = readln()
    print("Title: ")
    val title = readln()
    print("Author: ")
    val author = readln()
    print("Year: ")
    val year = readln().toIntOrNull() ?: 0

    val book = Book(isbn, title, author, year)
    if (service.addBook(book)) {
        println("Book added successfully ✅")
    } else {
        println("Book already exists ❌")
    }
}

private fun registerPatron(repository: LibraryRepository) {
    print("ID: ")
    val id = readln()
    print("Name: ")
    val name = readln()
    print("Email: ")
    val email = readln()
    print("Phone: ")
    val phone = readln()

    val patron = Patron(id, name, email, phone)
    if (repository.addPatron(patron)) {
        println("Patron registered successfully ✅")
    } else {
        println("Patron already exists ❌")
    }
}

private fun borrowBook(service: LibraryService) {
    print("Patron ID: ")
    val patronId = readln()
    print("ISBN: ")
    val isbn = readln()

    val result = service.borrowBook(patronId, isbn)
    when (result) {
        BorrowResult.SUCCESS -> println("Book borrowed successfully ✅")
        BorrowResult.BOOK_NOT_FOUND -> println("Book not found ❌")
        BorrowResult.PATRON_NOT_FOUND -> println("Patron not found ❌")
        BorrowResult.NOT_AVAILABLE -> println("Book is not available ❌")
        BorrowResult.LIMIT_REACHED -> println("Borrow limit reached ❌")
    }
}

private fun returnBook(service: LibraryService) {
    print("Patron ID: ")
    val patronId = readln()
    print("ISBN: ")
    val isbn = readln()

    if (service.returnBook(patronId, isbn)) {
        println("Book returned successfully ✅")
    } else {
        println("Return failed ❌")
    }
}

private fun search(service: LibraryService) {
    print("Search query (title/author): ")
    val query = readln()
    val results = service.search(query)
    if (results.isEmpty()) {
        println("No books found")
    } else {
        results.forEach { println(it.title + " by " + it.author + " (" + it.year + ")") }
    }
}

private fun listAllBooks(repository: LibraryRepository) {
    val books = repository.getAllBooks()
    if (books.isEmpty()) {
        println("No books in library")
    } else {
        println("\nISBN | Title | Author | Year | Status")
        books.forEach { book ->
            val status = if (book.isAvailable) "Available" else "Borrowed"
            println("${book.isbn} | ${book.title} | ${book.author} | ${book.year} | $status")
        }
    }
}

private fun viewAllPatrons(repository: LibraryRepository) {
    val patrons = repository.getAllPatrons()
    if (patrons.isEmpty()) {
        println("No patrons registered")
    } else {
        println("\nID | Name | Email | Phone | Borrowed Books")
        patrons.forEach { patron ->
            val borrowed = if (patron.borrowedBooks.isEmpty()) "None" else patron.borrowedBooks.joinToString(", ")
            println("${patron.id} | ${patron.name} | ${patron.email} | ${patron.phone} | $borrowed")
        }
    }
}