package ru.sberbank.user.api.controllers;

import ru.sberbank.user.api.database.cruds.IndividualPartyAccountCRUD;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.models.IndividualPartyAccount;
import ru.sberbank.user.api.utils.ValidationException;

import java.util.List;

public class IndividualPartyAccountController {

    private final IndividualPartyAccountCRUD ipAccountCrud = new IndividualPartyAccountCRUD();
    private final IndividualPartyController individualPartyController = new IndividualPartyController();

    public IndividualPartyAccount createIndPartyAccount(long individualPartyId) {
        individualPartyController.getIndividualParty(individualPartyId);
        return ipAccountCrud.insertIPAccount(individualPartyId);
    }

    public IndividualPartyAccount createIndPartyAccount(String individualPartyPhone) {
        IndividualParty individualParty = individualPartyController.getIndividualParty(individualPartyPhone);
        return ipAccountCrud.insertIPAccount(individualParty.getIndividualPartyID());
    }

    public List<IndividualPartyAccount> getIndPartyAccounts(long individualPartyId) {
        individualPartyController.getIndividualParty(individualPartyId);
        List<IndividualPartyAccount> individualPartyAccounts = ipAccountCrud.selectIPAccounts(individualPartyId);
        if (individualPartyAccounts == null) {
            throw new ValidationException("IndividualParty with id - " + individualPartyId + " doesn't have open accounts.");
        }
        return individualPartyAccounts;
    }

    public IndividualPartyAccount getIndPartyAccount(String ipAccountNumber) {
        checkIPAccountNumber(ipAccountNumber);
        IndividualPartyAccount individualPartyAccount = ipAccountCrud.selectIPAccByNumber(ipAccountNumber);
        if (individualPartyAccount == null) {
            throw new ValidationException("IndividualParty with accountNumber - " + ipAccountNumber + " was not found.");
        }
        return individualPartyAccount;
    }

    public IndividualPartyAccount setIndPartyAccountBalance(String ipAccountNumber, double amount) {
        checkAmount(amount, "amount");
        getIndPartyAccount(ipAccountNumber);
        return ipAccountCrud.setIPAccBalance(ipAccountNumber, amount);
    }

    public IndividualPartyAccount addIndPartyAccountBalance(String ipAccountNumber, double amountToAdd) {
        checkAmount(amountToAdd, "amountToAdd");
        IndividualPartyAccount account = getIndPartyAccount(ipAccountNumber);
        return setIndPartyAccountBalance(ipAccountNumber, account.getBalance() + amountToAdd);
    }

    public IndividualPartyAccount reduceIndPartyAccountBalance(String ipAccountNumber, double amountToReduce) {
        checkAmount(amountToReduce, "amountToReduce");
        IndividualPartyAccount account = getIndPartyAccount(ipAccountNumber);
        return setIndPartyAccountBalance(ipAccountNumber, account.getBalance() - amountToReduce);
    }

    public void deleteIndPartyAccount(String accountNumber) {
        getIndPartyAccount(accountNumber);
        ipAccountCrud.deleteAccount(accountNumber);
    }

    public void undoDeleteIndPartyAccount() {
        ipAccountCrud.undoDeleteAccount();
    }

    private void checkIPAccountNumber(String ipAccountNumber) {
        if (ipAccountNumber.length() != 20 || !ipAccountNumber.matches("\\d+")) {
            throw new ValidationException("AccountNumber contains only 20 digits.");
        }
    }

    private void checkAmount(double amount, String a) {
        if (amount < 0) {
            throw new ValidationException(a + " have to be positive");
        }
    }
}

