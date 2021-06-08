package ru.sberbank.user.api.mvc;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.models.IndividualPartyAccount;
import ru.sberbank.user.api.models.IndividualPartyCard;
import ru.sberbank.user.api.steps.BaseTest;
import ru.sberbank.user.api.utils.CardStatus;
import ru.sberbank.user.api.utils.ValidationException;

import java.sql.SQLException;
import java.util.List;

public class IndividualPartyCardMVCTest extends BaseTest {

    @Test
    public void createIndPartyCardTest() {
        String accountNumber = "12342356789912345345";
        ValidationException exception = Assert.assertThrows(
                ValidationException.class,
                () -> ipCardController.createIndPartyCard(accountNumber));
        Assert.assertEquals(exception.getMessage(), "IndividualParty with accountNumber - " + accountNumber + " was not found.");
        for (int i = 1; i <= 9; i++) {
            IndividualParty individualParty = individualPartyController.createIndividualParty("Name", phoneGenerator());
            IndividualPartyAccount indPartyAccount = ipAccountController.createIndPartyAccount(individualParty.getIndividualPartyID());
            ipCardController.createIndPartyCard(indPartyAccount.getNumber());
            ipCardController.createIndPartyCard(indPartyAccount.getNumber());
            ipCardController.createIndPartyCard(indPartyAccount.getNumber());
            indPartyAccount = ipAccountController.createIndPartyAccount(individualParty.getIndividualPartyID());
            ipCardController.createIndPartyCard(indPartyAccount.getNumber());
            ipCardController.createIndPartyCard(indPartyAccount.getNumber());
            ipCardController.createIndPartyCard(indPartyAccount.getNumber());
        }
        for (int i = 1; i <= 9; i++) {
            IndividualParty individualParty = individualPartyController.getIndividualParty(i);
            List<IndividualPartyAccount> accounts = ipAccountController.getIndPartyAccounts(i);
            Assert.assertEquals(individualParty.getAccounts().toString(), accounts.toString());
            Assert.assertEquals(individualPartyController.getIndividualParty(individualParty.getPhone()).getAccounts().toString(), accounts.toString());

            List<IndividualPartyCard> individualPartyCards = ipCardController.getIndividualPartyCards(i);
            Assert.assertEquals(2, accounts.size());
            Assert.assertTrue(individualPartyCards.toString().contains(accounts.get(0).getCards().toString().replace("]", "")));
            Assert.assertTrue(individualPartyCards.toString().contains(accounts.get(1).getCards().toString().replace("[", "")));
        }
    }

    @Test
    public void updateIndPartyStatusTest() throws SQLException {
        Assert.assertThrows(ValidationException.class, () -> ipCardController.updateIndPartyCardStatus("12342356789912345345", CardStatus.APPROVED));
        Assert.assertThrows(ValidationException.class, () -> ipCardController.updateIndPartyCardStatus("123423567 991234", CardStatus.APPROVED));
        Assert.assertThrows(ValidationException.class, () -> ipCardController.updateIndPartyCardStatus("123423567s991234", CardStatus.APPROVED));
        Assert.assertThrows(ValidationException.class, () -> ipCardController.updateIndPartyCardStatus("1234235672991234", CardStatus.APPROVED));
        for (int i = 1; i <= 9; i++) {
            IndividualParty individualParty = individualPartyController.createIndividualParty("Name", phoneGenerator());
            IndividualPartyAccount indPartyAccount = ipAccountController.createIndPartyAccount(individualParty.getIndividualPartyID());
            Assert.assertEquals(ipCardController.createIndPartyCard(indPartyAccount.getNumber()).getCardStatus(), CardStatus.IN_REVIEW);
            Assert.assertEquals(ipCardController.createIndPartyCard(indPartyAccount.getNumber()).getCardStatus(), CardStatus.IN_REVIEW);
            Assert.assertEquals(ipCardController.createIndPartyCard(indPartyAccount.getNumber()).getCardStatus(), CardStatus.IN_REVIEW);
        }
        for (int i = 1; i <= 9; i++) {
            List<IndividualPartyCard> individualPartyCards = ipCardController.getIndividualPartyCards(i);
            for (IndividualPartyCard ipCard : individualPartyCards) {
                ipCardController.updateIndPartyCardStatus(ipCard.getCardNumber(), CardStatus.APPROVED);
                Assert.assertEquals(ipCardCrud.selectIPCard(ipCard.getCardNumber()).getCardStatus(), CardStatus.APPROVED);
            }
        }
        for (int i = 1; i <= 9; i++) {
            List<IndividualPartyCard> individualPartyCards = ipCardController.getIndividualPartyCards(i);
            for (IndividualPartyCard ipCard : individualPartyCards) {
                ipCardController.updateIndPartyCardStatus(ipCard.getCardNumber(), CardStatus.DECLINED);
                Assert.assertEquals(ipCardCrud.selectIPCard(ipCard.getCardNumber()).getCardStatus(), CardStatus.DECLINED);
            }
        }
    }
}
