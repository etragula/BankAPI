package ru.sberbank.user.api.mvc;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.models.IndividualPartyAccount;
import ru.sberbank.user.api.steps.BaseTest;
import ru.sberbank.user.api.utils.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class IndividualPartyAccountMVCTest extends BaseTest {

    @Test
    public void createIndPartyAccountTest() {
        ArrayList<IndividualParty> individualParties = new ArrayList<>();
        ArrayList<IndividualPartyAccount> individualPartyAccounts = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            individualPartyController.createIndividualParty("name", phoneGenerator());
            individualPartyAccounts.add(ipAccountController.createIndPartyAccount(i + 1));
            individualParties.add(individualPartyController.getIndividualParty(i + 1));
        }
        for (int i = 0; i < 11; i++) {
            Assert.assertEquals(individualParties.get(i).getAccounts().get(0).getNumber(),
                    individualPartyAccounts.get(i).getNumber());
        }
    }

    @Test
    public void createIndPartyAccountTest2() {
        ArrayList<IndividualParty> individualParties = new ArrayList<>();
        ArrayList<IndividualPartyAccount> individualPartyAccounts = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            String individualPartyPhone = phoneGenerator();
            individualPartyController.createIndividualParty("name", individualPartyPhone);
            individualPartyAccounts.add(ipAccountController.createIndPartyAccount(individualPartyPhone));
            individualParties.add(individualPartyController.getIndividualParty(individualPartyPhone));
        }
        for (int i = 0; i < 11; i++) {
            Assert.assertEquals(individualParties.get(i).getAccounts().get(0).getNumber(),
                    individualPartyAccounts.get(i).getNumber());
        }
    }

    @Test
    public void getIndPartyAccountsTest() {
        int accountsAmount = 5;
        String individualPartyPhone = phoneGenerator();
        List<IndividualPartyAccount> indPartyAccounts;

        individualPartyController.createIndividualParty("name", individualPartyPhone);
        for (int i = 0; i < accountsAmount; i++) {
            ipAccountController.createIndPartyAccount(1);
        }

        indPartyAccounts = individualPartyController.getIndividualParty(1).getAccounts();
        Assert.assertEquals(accountsAmount, indPartyAccounts.size());
        for (IndividualPartyAccount ipAcc : indPartyAccounts) {
            Assert.assertEquals((long) ipAcc.getIndPartyId(), 1L);
            Assert.assertNotNull(ipAccountController.getIndPartyAccount(ipAcc.getNumber()));
        }

        indPartyAccounts = individualPartyController.getIndividualParty(individualPartyPhone).getAccounts();
        Assert.assertEquals(accountsAmount, indPartyAccounts.size());
        for (IndividualPartyAccount ipAcc : indPartyAccounts) {
            Assert.assertEquals((long) ipAcc.getIndPartyId(), 1L);
            Assert.assertNotNull(ipAccountController.getIndPartyAccount(ipAcc.getNumber()));
        }

        String phone = phoneGenerator();
        individualPartyController.createIndividualParty("name", phone);
        Assert.assertNull(individualPartyController.getIndividualParty(2).getAccounts());
        Assert.assertNull(individualPartyController.getIndividualParty(phone).getAccounts());
    }

    @Test
    public void getIndPartyAccountValidationTest() {
        String accountNumber = "46527429480298520531";
        String excMessage = "AccountNumber contains only 20 digits.";

        ValidationException e = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.getIndPartyAccount(accountNumber));
        Assert.assertEquals(e.getMessage(), "IndividualParty with accountNumber - " + accountNumber + " was not found.");

        e = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.getIndPartyAccount("1343m690786857456747"));
        Assert.assertEquals(e.getMessage(), excMessage);

        e = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.getIndPartyAccount("4652742948  02985205"));
        Assert.assertEquals(e.getMessage(), excMessage);

        e = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.getIndPartyAccounts(individualPartyController
                        .createIndividualParty("name", phoneGenerator()).getIndividualPartyID()));
        Assert.assertEquals(e.getMessage(), "IndividualParty with id - 1 doesn't have open accounts.");
    }

    @Test
    public void setIPAccBalanceValidationTest() {
        for (int i = 1; i <= 9; i++) {
            individualPartyController.createIndividualParty("name", phoneGenerator());
            ipAccountController.createIndPartyAccount(i);
            List<IndividualPartyAccount> accounts = ipAccountController.getIndPartyAccounts(i);
            String number = accounts.get(0).getNumber();
            int amount = 100 + (i * 10);
            Assert.assertEquals(1, accounts.size());
            ipAccountController.setIndPartyAccountBalance(number, amount);
            Assert.assertEquals(ipAccountController.getIndPartyAccount(number).getBalance(), amount, 0.0);
        }
        for (int i = 1; i <= 9; i++) {
            List<IndividualPartyAccount> accounts = ipAccountController.getIndPartyAccounts(i);
            Assert.assertEquals(1, accounts.size());
            String number = accounts.get(0).getNumber();
            double oldBalance = accounts.get(0).getBalance();
            int amountToAdd = 100 + (i * 10);
            ipAccountController.addIndPartyAccountBalance(number, amountToAdd);
            Assert.assertEquals(ipAccountController.getIndPartyAccount(number).getBalance(), oldBalance + amountToAdd, 0.0);
        }
        for (int i = 1; i <= 9; i++) {
            List<IndividualPartyAccount> accounts = ipAccountController.getIndPartyAccounts(i);
            Assert.assertEquals(1, accounts.size());
            String number = accounts.get(0).getNumber();
            double oldBalance = accounts.get(0).getBalance();
            int amountToReduce = 100 + (i * 10);
            ipAccountController.reduceIndPartyAccountBalance(number, amountToReduce);
            Assert.assertEquals(ipAccountController.getIndPartyAccount(number).getBalance(), oldBalance - amountToReduce, 0.0);
        }

        IndividualPartyAccount ipAcc = ipAccountController.getIndPartyAccounts(5).get(0);
        ValidationException exception = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.setIndPartyAccountBalance(ipAcc.getNumber(), -31.3));
        Assert.assertEquals(exception.getMessage(), "amount have to be positive");

        exception = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.addIndPartyAccountBalance(ipAcc.getNumber(), -31.3));
        Assert.assertEquals(exception.getMessage(), "amountToAdd have to be positive");

        exception = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.reduceIndPartyAccountBalance(ipAcc.getNumber(), -31.3));
        Assert.assertEquals(exception.getMessage(), "amountToReduce have to be positive");

        exception = Assert.assertThrows(ValidationException.class,
                () -> ipAccountController.reduceIndPartyAccountBalance(ipAcc.getNumber(), 311000.3));
        Assert.assertEquals(exception.getMessage(), "amount have to be positive");
    }

    @Test
    public void deleteIndPartyAccountTest() {
        List<IndividualPartyAccount> parties = new ArrayList<>();
        String number = null;
        for (int i = 1; i <= 9; i++) {
            individualPartyController.createIndividualParty("name", phoneGenerator());
            ipAccountController.createIndPartyAccount(i);
            number = ipAccountController.getIndPartyAccounts(i).get(0).getNumber();
            parties.add(ipAccountController.getIndPartyAccount(number));
            Assert.assertNotNull(parties);
        }
        for (int i = 1; i <= 9; i++) {
            number = ipAccountController.getIndPartyAccounts(i).get(0).getNumber();
            ipAccountController.deleteIndPartyAccount(number);
            String finalNumber = number;
            Assert.assertThrows(
                    ValidationException.class,
                    () -> ipAccountController.getIndPartyAccount(finalNumber)
            );
        }
        for (int i = 1; i <= 9; i++) {
            ipAccountController.undoDeleteIndPartyAccount();
            Assert.assertEquals(parties.get(9 - i).getNumber(),
                    ipAccountController.getIndPartyAccounts(10 - i).get(0).getNumber());
        }
    }
}
