package org.zerock.libraryapp.domain.user.loanhistory;

import jakarta.persistence.*;
import org.zerock.libraryapp.domain.user.User;

@Entity
public class UserLoanHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @JoinColumn(nullable = false)
    @ManyToOne
    private User user;

    private String bookName;

    private boolean isReturn; // 여기서 boolean으로 하면 DB에 tinyInt로 한것과 잘 매핑이 된다. 0이면 true 1이면 False로

    protected UserLoanHistory() {}

    public UserLoanHistory(User user, String bookName) {
        this.user = user;
        this.bookName = bookName;
        this.isReturn = false;
    }

    public void doReturn(){
        this.isReturn = true;
    }

    public String getBookName() {
        return this.bookName;
    }
}
