// 05-spring-ai/src/main/java/dio/budgeting/domain/Transaction.java
package dio.budgeting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Transaction {
    private TransactionId id;
    private String description;
    private long amount;
    private Category category;

    public Transaction(String description, long amount, Category category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("O valor da transação deve ser obrigatoriamente maior que zero.");
        }
        if (category == null) {
            throw new IllegalArgumentException("A transação precisa pertencer a uma categoria válida.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("A descrição da transação não pode estar vazia!");
        }

        this.id = new TransactionId();
        this.description = description;
        this.amount = amount;
        this.category = category;
    }
}

