package ru.sberbank.user.api.managers;

import ru.sberbank.user.api.controllers.IndividualPartyAccountController;
import ru.sberbank.user.api.controllers.IndividualPartyCardController;
import ru.sberbank.user.api.database.cruds.IndividualPartyAccountCRUD;
import ru.sberbank.user.api.database.cruds.IndividualPartyCRUD;
import ru.sberbank.user.api.database.cruds.IndividualPartyCardCRUD;
import ru.sberbank.user.api.models.IndividualParty;
import ru.sberbank.user.api.models.IndividualPartyAccount;
import ru.sberbank.user.api.controllers.IndividualPartyController;

import java.sql.SQLException;
import java.util.Random;

public abstract class Base {
    protected static final IndividualPartyCRUD ipCrud = new IndividualPartyCRUD();
    protected static final IndividualPartyCardCRUD ipCardCrud = new IndividualPartyCardCRUD();
    protected static final IndividualPartyAccountCRUD ipAccountCrud = new IndividualPartyAccountCRUD();

    protected static final IndividualPartyController individualPartyController = new IndividualPartyController();
    protected static final IndividualPartyCardController ipCardController = new IndividualPartyCardController();
    protected static final IndividualPartyAccountController ipAccountController = new IndividualPartyAccountController();

    protected static void doBefore() {
        try {
            ipCrud.creatIndPartyTable();
            ipCardCrud.creatCardTable();
            ipAccountCrud.createIPAccountTable();
        } catch (SQLException throwables) {
            doAfter();
            doBefore();
        }
        for (int i = 0; i < 4; i++) {
            IndividualParty individualParty = individualPartyController.createIndividualParty("Name", phoneGenerator());
            IndividualPartyAccount indPartyAccount = ipAccountController.createIndPartyAccount(individualParty.getIndividualPartyID());
            ipCardController.createIndPartyCard(indPartyAccount.getNumber());
        }
    }

    protected static void doAfter() {
        try {
            ipCrud.dropIndPartyTable();
            ipCardCrud.dropCardTable();
            ipAccountCrud.dropIPAccountTable();
        } catch (SQLException throwables) {
        }
    }

    protected static String phoneGenerator() {
        int num1, num2;
        int set2, set3;

        Random generator = new Random();

        num1 = generator.nextInt(9);
        num2 = generator.nextInt(9);

        set2 = generator.nextInt(643) + 100;
        set3 = generator.nextInt(8999) + 1000;

        return ("+79" + num1 + num2 + set2 + set3);
    }
}
