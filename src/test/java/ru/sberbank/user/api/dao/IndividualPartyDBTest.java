package ru.sberbank.user.api.dao;

import org.junit.Assert;
import org.junit.Test;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.steps.BaseTest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndividualPartyDBTest extends BaseTest {

    @Test(expected = SQLException.class)
    public void createIndPartyTableTest() throws SQLException {
        ipCrud.creatIndPartyTable();
    }

    @Test(expected = SQLException.class)
    public void dropIndPartyTableTest() throws SQLException {
        ipCrud.dropIndPartyTable();
        ipCrud.dropIndPartyTable();
    }

    @Test
    public void insertIndPartyTest() {
        IndividualParty party;
        IndividualParty party1;
        for (int i = 1; i <= 16; i++) {
            String individualPartyNamePhone = phoneGenerator();
            party1 = IndividualParty.builder().id(i).name("Name").phone(individualPartyNamePhone).build();
            party = ipCrud.insertIndParty("Name", individualPartyNamePhone);
            Assert.assertEquals(party.toString(), party1.toString());
            Assert.assertNull(party.getAccounts());
        }
    }

    @Test
    public void selectIndPartyTest() {
        IndividualParty ip1 = ipCrud.selectIndPartyById(123);
        IndividualParty ip2 = ipCrud.selectIndPartyByName("not_exist");
        IndividualParty ip3 = ipCrud.selectIndPartyByPhone("not_exist");
        Assert.assertNull(ip1);
        Assert.assertNull(ip2);
        Assert.assertNull(ip3);
        for (int i = 1; i <= 16; i++) {
            String name = "Name";
            String individualPartyNamePhone = phoneGenerator();
            IndividualParty ip = ipCrud.insertIndParty(name, individualPartyNamePhone);
            ip1 = ipCrud.selectIndPartyById(i);
            ip2 = ipCrud.selectIndPartyByName(name);
            ip3 = ipCrud.selectIndPartyByPhone(individualPartyNamePhone);
            Assert.assertEquals(ip.toString(), ip2.toString());
            Assert.assertEquals(ip1.toString(), ip2.toString());
            Assert.assertEquals(ip2.toString(), ip3.toString());
        }
    }

    @Test
    public void setIndPartyTest() {
        IndividualParty ip = ipCrud.insertIndParty("name1", phoneGenerator());
        IndividualParty ip2 = ipCrud.insertIndParty("name2", phoneGenerator());
        String newName = "TestName";
        String newPhone = phoneGenerator();
        ip = ipCrud.selectIndPartyById(1);
        ip2 = ipCrud.selectIndPartyById(2);
        ip = ipCrud.setIndPartyName(ip.getIndividualPartyID(), newName);
        ip2 = ipCrud.setIndPartyPhone(ip2.getIndividualPartyID(), newPhone);
        Assert.assertEquals(ip.getName(), newName);
        Assert.assertEquals(ip2.getPhone(), newPhone);
    }

    @Test
    public void deleteAndUndoDeleteIndPartyTest() {
        IndividualParty party;
        List<IndividualParty> parties = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            parties.add(ipCrud.insertIndParty("Name", phoneGenerator()));
            Assert.assertNotNull(ipCrud.selectIndPartyById(i));
            Assert.assertNotNull(parties.get(i - 1));
        }
        for (int i = 1; i <= 16; i++) {
            ipCrud.deleteIndParty(i);
            Assert.assertNull(ipCrud.selectIndPartyById(i));
        }
        for (int i = 1; i <= 16; i++) {
            party = ipCrud.undoDeleteIndParty();
            Assert.assertEquals(parties.get(parties.size() - i).toString(), party.toString());
            Assert.assertNotNull(party);
        }
        Assert.assertNull(ipCrud.undoDeleteIndParty());
    }
}
