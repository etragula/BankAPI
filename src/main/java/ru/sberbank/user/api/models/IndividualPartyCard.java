package ru.sberbank.user.api.models;

import ru.sberbank.user.api.utils.CardStatus;

public class IndividualPartyCard {

    private Long id;
    private Long indPartyID;
    private String accountNumber;
    private String cardNumber;
    private CardStatus cardStatus;

    public IndividualPartyCard() {
    }

    public Long getId() {
        return id;
    }

    public Long getIndPartyID() {
        return indPartyID;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CardStatus getCardStatus() {
        return cardStatus;
    }

    @Override
    public String toString() {
        return "IndividualPartyCard{" +
                "id=" + id +
                ", indPartyID=" + indPartyID +
                ", accountNumber='" + accountNumber + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", cardStatus=" + cardStatus +
                '}';
    }

    public static IndividualPartyCard.Builder builder() {
        return new IndividualPartyCard().new Builder();
    }

    public class Builder {

        private Builder() {
        }

        public IndividualPartyCard.Builder id(long id) {
            IndividualPartyCard.this.id = id;
            return this;
        }

        public IndividualPartyCard.Builder indPartyId(long individualPartyNameID) {
            IndividualPartyCard.this.indPartyID = individualPartyNameID;
            return this;
        }

        public IndividualPartyCard.Builder cardNumber(String cardNumber) {
            IndividualPartyCard.this.cardNumber = cardNumber;
            return this;
        }

        public IndividualPartyCard.Builder accountNumber(String accountNumber) {
            IndividualPartyCard.this.accountNumber = accountNumber;
            return this;
        }

        public IndividualPartyCard.Builder status(CardStatus cardStatus) {
            IndividualPartyCard.this.cardStatus = cardStatus;
            return this;
        }

        public IndividualPartyCard build() {
            return IndividualPartyCard.this;
        }
    }
}
