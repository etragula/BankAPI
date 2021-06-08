package ru.sberbank.user.api.controllers;

import ru.sberbank.user.api.database.cruds.IndividualPartyCardCRUD;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.models.IndividualPartyAccount;
import ru.sberbank.user.api.models.IndividualPartyCard;
import ru.sberbank.user.api.utils.CardStatus;
import ru.sberbank.user.api.utils.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class IndividualPartyCardController {

    private final IndividualPartyAccountController ipAccountController = new IndividualPartyAccountController();
    private final IndividualPartyController individualPartyController = new IndividualPartyController();
    private final IndividualPartyCardCRUD ipCardCrud = new IndividualPartyCardCRUD();

    public IndividualPartyCard createIndPartyCard(String indPartyAccountNumber) {
        IndividualPartyAccount ipAccount = ipAccountController.getIndPartyAccount(indPartyAccountNumber);
        return ipCardCrud.insertIPCard(ipAccount.getIndPartyId(), indPartyAccountNumber);
    }

    public List<IndividualPartyCard> getIndividualPartyCards(long individualPartyID) {
        List<IndividualPartyCard> indPartyCards = new ArrayList<>();
        for (IndividualPartyAccount account : ipAccountController.getIndPartyAccounts(individualPartyID)) {
            indPartyCards.addAll(account.getCards());
        }
        return indPartyCards;
    }

    public List<IndividualPartyCard> getIndividualPartyCards(String individualPartyPhone) {
        IndividualParty individualParty = individualPartyController.getIndividualParty(individualPartyPhone);
        return getIndividualPartyCards(individualParty.getIndividualPartyID());
    }

    public IndividualPartyCard updateIndPartyCardStatus(String indPartyCardNumber, CardStatus cardStatus) {
        checkIPCardNumber(indPartyCardNumber);
        IndividualPartyCard individualPartyCard = ipCardCrud.setIPCardStatus(indPartyCardNumber, cardStatus);
        if (individualPartyCard == null) {
            throw new ValidationException("cardNumber - " + indPartyCardNumber + " doesn't exist.");
        }
        return individualPartyCard;
    }

    private void checkIPCardNumber(String cardNumber) {
        if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
            throw new ValidationException("CardNumber contains only 16 digits.");
        }
    }

    // TODO - other methods
}
