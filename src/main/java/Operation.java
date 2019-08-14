import java.util.Date;

/**
 * Created by User on 12 Авг., 2019
 * Тип счёта,
 * Номер счета,
 * Валюта,
 * Дата операции,
 * Референс проводки,
 * Описание операции,
 * Приход,
 * Расход
 */
public class Operation {
    public String accountType;
    public String accountNumber;
    public String currency;
    public Date date; //31.05.17
    public String reference;
    public String description;
    public double incoming;
    public double expense;

    public Operation(String accountType, String accountNumber, String currency,
                     Date date, String reference, String description,
                     double incoming, double expense) {
        this.accountType = accountType;
        this.accountNumber = accountNumber;
        this.currency = currency;
        this.date = date;
        this.reference = reference;
        this.description = description;
        this.incoming = incoming;
        this.expense = expense;
    }
}
