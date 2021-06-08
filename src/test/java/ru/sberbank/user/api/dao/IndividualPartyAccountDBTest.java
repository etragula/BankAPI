package ru.sberbank.user.api.dao;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.models.IndividualPartyAccount;
import ru.sberbank.user.api.steps.BaseTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IndividualPartyAccountDBTest extends BaseTest {

    IndividualPartyDBTest individualPartyDBTest = new IndividualPartyDBTest();

    @Test(expected = SQLException.class)
    public void createIPAccountTableTest() throws SQLException {
        ipAccountCrud.createIPAccountTable();
    }

    @Test(expected = SQLException.class)
    public void dropIPAccountTableTest() throws SQLException {
        ipAccountCrud.dropIPAccountTable();
        ipAccountCrud.dropIPAccountTable();
    }

    @Test
    public void insertIPAccountTest() {
        individualPartyDBTest.insertIndPartyTest();
        for (int i = 1; i <= 16; i++) {
            IndividualParty party = ipCrud.selectIndPartyById(i);
            IndividualPartyAccount partyAccount = ipAccountCrud.insertIPAccount(i);
            party.setAccounts(Collections.singletonList(partyAccount));
            Assert.assertNotNull(party.getAccounts());
            Assert.assertEquals(party.getAccounts().get(0).toString(), partyAccount.toString());
        }
    }

    @Test
    public void selectIPAccountTest() {
        insertIPAccountTest();
        for (int i = 1; i <= 16; i++) {
            IndividualParty party = ipCrud.selectIndPartyById(i);
            List<IndividualPartyAccount> partyAccounts = ipAccountCrud.selectIPAccounts(i);
            party.setAccounts(partyAccounts);
            List<IndividualPartyAccount> accounts = party.getAccounts();
            Assert.assertNotNull(accounts);
            Assert.assertNotNull(partyAccounts);
            Assert.assertEquals(accounts.toString(), partyAccounts.toString());
            IndividualPartyAccount partyAccount = ipAccountCrud.selectIPAccByNumber(partyAccounts.get(0).getNumber());
            Assert.assertEquals(accounts.get(0).getNumber(), partyAccount.getNumber());
        }
    }

    @Test
    public void setIPAccBalanceTest() {
        insertIPAccountTest();
        for (int i = 1; i <= 16; i++) {
            IndividualParty party = ipCrud.selectIndPartyById(i);
            List<IndividualPartyAccount> partyAccounts = ipAccountCrud.selectIPAccounts(i);
            IndividualPartyAccount partyAccount = partyAccounts.get(0);
            Assert.assertNotNull(partyAccounts);
            Assert.assertEquals(0.0, partyAccount.getBalance(), 0.0);
            double newBalance = 100 + (i * 10.0);
            ipAccountCrud.setIPAccBalance(partyAccount.getNumber(), newBalance);
            Assert.assertEquals(newBalance,
                    ipAccountCrud.selectIPAccByNumber(partyAccount.getNumber()).getBalance(), 0.0);
        }
    }

    @Test
    public void deleteAndUndoDeleteAccTest() {
        insertIPAccountTest();
        IndividualPartyAccount account;
        List<IndividualPartyAccount> partyAccounts = new ArrayList<>();
        for (int i = 1; i <= 16; i++)
            partyAccounts.add(ipAccountCrud.selectIPAccounts(i).get(0));
        for (int i = 1; i <= 16; i++) {
            String number = ipAccountCrud.selectIPAccounts(i).get(0).getNumber();
            ipAccountCrud.deleteAccount(number);
            Assert.assertNull(ipAccountCrud.selectIPAccByNumber(number));
        }
        for (int i = 1; i <= 16; i++) {
            account = ipAccountCrud.undoDeleteAccount();
            Assert.assertEquals(partyAccounts.get(partyAccounts.size() - i).getIndPartyId(), account.getIndPartyId());
            Assert.assertEquals(partyAccounts.get(partyAccounts.size() - i).getNumber(),
                    ipAccountCrud.selectIPAccByNumber(account.getNumber()).getNumber());
            Assert.assertNotNull(account);
        }
        Assert.assertNull(ipCrud.undoDeleteIndParty());
    }
}
