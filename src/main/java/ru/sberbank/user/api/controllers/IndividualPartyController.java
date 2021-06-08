package ru.sberbank.user.api.controllers;

import ru.sberbank.user.api.database.cruds.IndividualPartyAccountCRUD;
import ru.sberbank.user.api.database.cruds.IndividualPartyCRUD;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.utils.ValidationException;

public class IndividualPartyController {

    private final IndividualPartyAccountCRUD ipAccountCrud = new IndividualPartyAccountCRUD();
    private final IndividualPartyCRUD individualPartyCrud = new IndividualPartyCRUD();

    public IndividualParty createIndividualParty(String individualPartyName, String individualPartyPhone) {
        checkIPParams(individualPartyName, individualPartyPhone);
        if (individualPartyCrud.selectIndPartyByPhone(individualPartyPhone) != null) {
            throw new ValidationException("IndividualParty with phone - " + individualPartyPhone + " already exists.");
        }
        individualPartyCrud.insertIndParty(individualPartyName, individualPartyPhone);
        return getIndividualParty(individualPartyPhone);
    }

    public IndividualParty getIndividualParty(long individualPartyID) {
        checkIPId(individualPartyID);
        IndividualParty individualParty = individualPartyCrud.selectIndPartyById(individualPartyID);
        if (individualParty == null) {
            throw new ValidationException("IndividualParty with id - " + individualPartyID + " was not found.");
        }
        try {
            individualParty.setAccounts(ipAccountCrud.selectIPAccounts(individualPartyID));
        } catch (Exception e) {
        }
        return individualParty;
    }

    public IndividualParty getIndividualParty(String individualPartyPhone) {
        checkIPPhone(individualPartyPhone);
        IndividualParty individualParty = individualPartyCrud.selectIndPartyByPhone(individualPartyPhone);
        if (individualParty == null) {
            throw new ValidationException("IndividualParty with phone - " + individualPartyPhone + " was not found.");
        }
        try {
            individualParty.setAccounts(ipAccountCrud.selectIPAccounts(individualParty.getIndividualPartyID()));
        } catch (Exception e) {
        }
        return individualParty;
    }

    public IndividualParty setIndividualPartyName(long individualPartyID, String newIndividualPartyName) {
        checkIPId(individualPartyID);
        checkIPName(newIndividualPartyName);
        if (individualPartyCrud.selectIndPartyById(individualPartyID) == null) {
            throw new ValidationException("IndividualParty with id - " + individualPartyID + " was not found.");
        }
        return individualPartyCrud.setIndPartyName(individualPartyID, newIndividualPartyName);
    }

    public IndividualParty setIndividualPartyPhone(long individualPartyID, String newIndividualPartyPhone) {
        checkIPId(individualPartyID);
        checkIPPhone(newIndividualPartyPhone);
        if (individualPartyCrud.selectIndPartyById(individualPartyID) == null) {
            throw new ValidationException("IndividualParty with id - " + individualPartyID + " was not found.");
        }
        return individualPartyCrud.setIndPartyPhone(individualPartyID, newIndividualPartyPhone);
    }

    public void deleteIndividualParty(long individualPartyID) {
        checkIPId(individualPartyID);
        individualPartyCrud.deleteIndParty(individualPartyID);
    }

    public void undoDeleteIndividualParty() {
        individualPartyCrud.undoDeleteIndParty();
    }

    private void checkIPParams(String individualPartyName, String individualPartyPhone) {
        if (individualPartyName == null || individualPartyPhone == null) {
            throw new ValidationException("Name is necessary.\n" +
                    "Phone is necessary.");
        }
        checkIPName(individualPartyName);
        checkIPPhone(individualPartyPhone);
    }

    private static void checkIPName(String individualPartyName) {
        if (individualPartyName == null ||
                individualPartyName.trim().isEmpty() ||
                !individualPartyName.matches("[a-zA-Z\\s]+")) {
            throw new ValidationException("Bad format for attribute \"name\".");
        }
    }

    private void checkIPId(long individualPartyID) {
        if (individualPartyID <= 0) {
            throw new ValidationException("ID have to be positive and bigger than zero.");
        }
    }

    private void checkIPPhone(String individualPartyPhone) {
        if (individualPartyPhone.length() != 12 ||
                !individualPartyPhone.startsWith("+79") ||
                !individualPartyPhone.substring(1).matches("\\d+")) {
            throw new ValidationException("Bad format for attribute \"phone\". (Format +711111111111)");
        }
    }
}
