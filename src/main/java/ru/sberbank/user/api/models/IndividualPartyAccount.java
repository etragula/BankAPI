package ru.sberbank.user.api.models;

import java.util.List;

public class IndividualPartyAccount {

    private Long id;
    private Long indPartyId;
    private String number;
    private Double balance;
    private List<IndividualPartyCard> cards;

    public IndividualPartyAccount() {
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public Double getBalance() {
        return balance;
    }

    public Long getIndPartyId() {
        return indPartyId;
    }

    public List<IndividualPartyCard> getCards() {
        return cards;
    }

    public void setCards(List<IndividualPartyCard> cards) {
        this.cards = cards;
    }

    public static Builder builder() {
        return new IndividualPartyAccount().new Builder();
    }

    @Override
    public String toString() {
        StringBuilder s;
        s = new StringBuilder("Account{" +
                "id=" + id +
                ", indPartyId=" + indPartyId +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                ", cards= {");
        if (cards != null) {
            for (IndividualPartyCard card : cards) {
                s.append(card.toString()).append(",");
            }
        }
        s.append("}");
        return s.toString();
    }

    public class Builder {

        private Builder() {
        }

        public Builder id(long id) {
            IndividualPartyAccount.this.id = id;
            return this;
        }

        public Builder indPartyId(long individualPartyNameID) {
            IndividualPartyAccount.this.indPartyId = individualPartyNameID;
            return this;
        }

        public Builder number(String accountNumber) {
            IndividualPartyAccount.this.number = accountNumber;
            return this;
        }

        public Builder balance(Double accountBalance) {
            IndividualPartyAccount.this.balance = accountBalance;
            return this;
        }

        public IndividualPartyAccount build() {
            return IndividualPartyAccount.this;
        }
    }
}
