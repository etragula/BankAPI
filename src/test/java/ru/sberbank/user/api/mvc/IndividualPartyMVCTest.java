package ru.sberbank.user.api.mvc;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.steps.BaseTest;
import ru.sberbank.user.api.utils.ValidationException;

import java.util.ArrayList;
import java.util.List;

public class IndividualPartyMVCTest extends BaseTest {

    @Test
    public void createIndividualPartyTest() {
        String name = "name surname";
        String phone = phoneGenerator();

        individualPartyController.createIndividualParty(name, phone);
        ValidationException exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.createIndividualParty(name, phone));
        Assert.assertEquals(exception.getMessage(), "IndividualParty with phone - " + phone + " already exists.");
    }

    @Test
    public void createIndividualPartyValidationTest() {
        String badName = "Name 1Surname";
        String goodName = "Name Surname";

        String badPhone = phoneGenerator() + "1";
        String badPhone1 = "894354234245";
        String goodPhone = phoneGenerator();

        String exMessage1 = "Bad format for attribute";
        String exMessage = "Name is necessary.\n" +
                "Phone is necessary.";

        ValidationException exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.createIndividualParty(null, null));
        Assert.assertEquals(exception.getMessage(), exMessage);

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.createIndividualParty(null, phoneGenerator()));
        Assert.assertEquals(exception.getMessage(), exMessage);

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.createIndividualParty("null", null));
        Assert.assertEquals(exception.getMessage(), exMessage);

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.createIndividualParty(badName, goodPhone));
        Assert.assertEquals(exception.getMessage(), exMessage1 + " \"name\".");

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.createIndividualParty(goodName, badPhone));
        Assert.assertEquals(exception.getMessage(), exMessage1 + " \"phone\". (Format +711111111111)");

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.createIndividualParty(goodName, badPhone1));
        Assert.assertEquals(exception.getMessage(), exMessage1 + " \"phone\". (Format +711111111111)");

        Assert.assertNotNull(individualPartyController.createIndividualParty(goodName, goodPhone));
    }

    @Test
    public void getIndividualPartyValidationTest() {
        long individualPartyID = 30;
        String except = "ID have to be positive and bigger than zero.";

        ValidationException exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.getIndividualParty(individualPartyID));
        Assert.assertEquals(exception.getMessage(), "IndividualParty with id - " + individualPartyID + " was not found.");

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.getIndividualParty(0));
        Assert.assertEquals(exception.getMessage(), except);

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.getIndividualParty(-individualPartyID));
        Assert.assertEquals(exception.getMessage(), except);
    }

    @Test
    public void getIndividualPartyValidationTest2() {
        String phone = "+79111111111";

        ValidationException exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.getIndividualParty(phone));
        Assert.assertEquals(exception.getMessage(), "IndividualParty with phone - " + phone + " was not found.");

        exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.getIndividualParty(phone + 1));
        Assert.assertEquals(exception.getMessage(), "Bad format for attribute \"phone\". (Format +711111111111)");

        individualPartyController.createIndividualParty("test", phone);
        Assert.assertEquals(
                individualPartyController.getIndividualParty(1).toString(),
                individualPartyController.getIndividualParty(phone).toString());
    }

    @Test
    public void setIndividualPartyNameTest() {
        for (int i = 1; i <= 7; i++) {
            individualPartyController.createIndividualParty("name", phoneGenerator());
        }
        long id = 3;
        IndividualParty individualParty = individualPartyController.getIndividualParty(id);
        String oldName = individualParty.getName();
        String newName = "new" + oldName;

        individualParty = individualPartyController.setIndividualPartyName(id, newName);
        Assert.assertEquals(individualParty.getName(), newName);

        individualParty = individualPartyController.getIndividualParty(id);
        Assert.assertEquals(individualParty.getName(), newName);

        ValidationException exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.setIndividualPartyName(533, newName));
        Assert.assertEquals(exception.getMessage(), "IndividualParty with id - " + 533 + " was not found.");
    }

    @Test
    public void setIndividualPartyPhoneTest() {
        for (int i = 1; i <= 7; i++) {
            individualPartyController.createIndividualParty("name", phoneGenerator());
        }
        long id = 3;
        String newPhone = phoneGenerator();

        IndividualParty individualParty = individualPartyController.setIndividualPartyPhone(id, newPhone);
        Assert.assertEquals(individualParty.getPhone(), newPhone);

        individualParty = individualPartyController.getIndividualParty(id);
        Assert.assertEquals(individualParty.getPhone(), newPhone);

        ValidationException exception = Assert.assertThrows(ValidationException.class,
                () -> individualPartyController.setIndividualPartyPhone(533, newPhone));
        Assert.assertEquals(exception.getMessage(), "IndividualParty with id - " + 533 + " was not found.");
    }

    @Test
    public void deleteAndUndoDeleteIndividualPartyTest() {
        List<IndividualParty> parties = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            individualPartyController.createIndividualParty("name", phoneGenerator());
            IndividualParty individualParty = individualPartyController.getIndividualParty(i);
            parties.add(individualParty);
            Assert.assertNotNull(individualParty);
        }
        for (int i = 1; i <= 7; i++) {
            individualPartyController.deleteIndividualParty(i);
        }
        for (int i = 1; i <= 7; i++) {
            individualPartyController.undoDeleteIndividualParty();
            Assert.assertEquals(parties.get(7 - i).toString(),
                    individualPartyController.getIndividualParty(8 - i).toString());
        }
    }
}
