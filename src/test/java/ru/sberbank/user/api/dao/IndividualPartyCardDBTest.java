package ru.sberbank.user.api.dao;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.models.IndividualPartyAccount;
import ru.sberbank.user.api.models.IndividualPartyCard;
import ru.sberbank.user.api.steps.BaseTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndividualPartyCardDBTest extends BaseTest {

    @Test(expected = SQLException.class)
    public void createIPCardTableTest() throws SQLException {
        ipCardCrud.creatCardTable();
    }

    @Test(expected = SQLException.class)
    public void dropIPCardTableTest() throws SQLException {
        ipCardCrud.dropCardTable();
        ipCardCrud.dropCardTable();
    }

    @Test
    public void insertIPCardTest() {
        IndividualParty party;
        IndividualPartyCard ipCard;
        IndividualPartyAccount ipAccount;
        for (int i = 1; i <= 16; i++) {
            party = ipCrud.insertIndParty("Name", phoneGenerator());
            ipAccount = ipAccountCrud.insertIPAccount(party.getIndividualPartyID());
            party.setAccounts(Collections.singletonList(ipAccount));
            ipCard = ipCardCrud.insertIPCard(party.getIndividualPartyID(), ipAccount.getNumber());
            Assert.assertEquals((party.getIndividualPartyID() == ipAccount.getIndPartyId()),
                    (ipAccount.getIndPartyId() == ipCard.getIndPartyID()));
            Assert.assertEquals((party.getAccounts().get(0).getNumber().equals(ipAccount.getNumber())),
                    (ipAccount.getNumber().equals(ipCard.getAccountNumber())));
        }
    }

    @Test
    public void selectIPCardTest() {
        List<IndividualPartyCard> ip1 = ipCardCrud.selectIPCard(123);
        IndividualPartyCard ip2 = ipCardCrud.selectIPCard("not_exist");
        List<IndividualPartyCard> ip3 = ipCardCrud.selectIPCardByAcc("not_exist");
        Assert.assertNull(ip1);
        Assert.assertNull(ip2);
        Assert.assertNull(ip3);
    }

    @Test
    public void deleteAndUndoDeleteIPCardTest() {
        IndividualParty party;
        IndividualPartyCard ipCard;
        IndividualPartyAccount ipAccount;
        List<IndividualPartyCard> individualPartyCards = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            party = ipCrud.insertIndParty("Name", phoneGenerator());
            ipAccount = ipAccountCrud.insertIPAccount(party.getIndividualPartyID());
            party.setAccounts(Collections.singletonList(ipAccount));
            ipCard = ipCardCrud.insertIPCard(party.getIndividualPartyID(), ipAccount.getNumber());
            individualPartyCards.add(ipCard);
        }
        for (int i = 1; i <= 6; i++) {
            String cardNumber = individualPartyCards.get(i - 1).getCardNumber();
            ipCardCrud.deleteIPCard(cardNumber);
            Assert.assertNull(ipCardCrud.selectIPCard(cardNumber));
        }
        for (int i = 1; i <= 6; i++) {
            ipCard = ipCardCrud.undoDeleteIPCard();
            Assert.assertEquals(individualPartyCards.get(individualPartyCards.size() - i).toString(), ipCard.toString());
            Assert.assertEquals(individualPartyCards.get(individualPartyCards.size() - i).toString(),
                    ipCardCrud.selectIPCard(ipCard.getCardNumber()).toString());
            Assert.assertNotNull(ipCard);
        }
        Assert.assertNull(ipCardCrud.undoDeleteIPCard());
    }
}
