package org.zerock.libraryapp.service.book;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.libraryapp.domain.book.Book;
import org.zerock.libraryapp.domain.book.BookRepository;
import org.zerock.libraryapp.domain.user.User;
import org.zerock.libraryapp.domain.user.UserRepository;
import org.zerock.libraryapp.domain.user.loanhistory.UserLoanHistory;
import org.zerock.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import org.zerock.libraryapp.dto.book.request.BookCreateRequest;
import org.zerock.libraryapp.dto.book.request.BookLoanRequest;
import org.zerock.libraryapp.dto.book.request.BookReturnRequest;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserLoanHistoryRepository userLoanHistoryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveBook(BookCreateRequest request) {
        bookRepository.save(new Book(request.getName()));
    }

    @Transactional
    public void loanBook(BookLoanRequest request) {
        // 1. 책 정보를 가져 온다.
        Book book = bookRepository.findByName(request.getBookName())
                .orElseThrow(IllegalArgumentException::new);

        // 2. 대출기록 정보를 확인해서 대출중인지 확인.
        // 3. 만약에 확인했는데 대출 중이라면 예외를 발생
        if (userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)){
            throw new IllegalArgumentException("진작 대출되어있는 책입니다.");
        }

        // 4. 유저 정보를 가져온다.
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        // cascade 옵션을 사용해서 아래와 같은 기능을 할 수 있게 된것이다.
        user.loanBook(book.getName());

//        // 5. 유저 정보와 책 정보를 기반으로 UserLoanHistory를 저장
//        userLoanHistoryRepository.save(new UserLoanHistory(user, book.getName()));

    }

    @Transactional
    public void returnBook(BookReturnRequest request) {
        User user = userRepository.findByName(request.getUserName())
                .orElseThrow(IllegalArgumentException::new);

        user.returnBook(request.getBookName());
    }
}
