package codemeharder.gbasket;

/**
 * Created by seokyung on 4/2/16.
 */
public class Card {

    private String Card = "Card";
    private String Exp_Month = "Exp_Month";
    private String Exp_Year = "Exp_Year";
    private String cvc = "cvc";

    public Card() {

    }

    public Card(String card, String eMonth, String eYear, String cvc) {

        this.Card = card;
        this.Exp_Month = eMonth;
        this.Exp_Year = eYear;
        this.cvc = cvc;
    }

    public void setCard(String card) {
        this.Card = card;
    }

    public String getCard() {
        return this.Card;
    }

    public void seteMonth(String eMonth) {
        this.Exp_Year = eMonth;
    }

    public String geteMonth() {
        return this.Exp_Month;
    }

    public void seteYear(String eYear) {
        this.Exp_Year = eYear;
    }

    public String geteYear() {
        return this.Exp_Year;
    }

    public void setCVC (String CVC) {
        this.cvc = CVC;
    }

    public String getCVC() {
        return this.cvc;
    }

}
