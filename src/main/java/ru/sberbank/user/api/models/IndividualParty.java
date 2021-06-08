package ru.sberbank.user.api.models;

import java.util.List;

public class IndividualParty {

    private Long individualPartyID;
    private String name;
    private String phone;
    private List<IndividualPartyAccount> accounts;
//    private List<IndividualParty> contrAgents;

    private IndividualParty() {
    }

    public Long getIndividualPartyID() {
        return individualPartyID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public List<IndividualPartyAccount> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<IndividualPartyAccount> accounts) {
        this.accounts = accounts;
    }

    public static Builder builder() {
        return new IndividualParty().new Builder();
    }

    @Override
    public String toString() {
        StringBuilder s;
        s = new StringBuilder("IndividualParty{" +
                "id=" + individualPartyID +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", {");
        if (accounts != null) {
            for (IndividualPartyAccount acc : accounts) {
                s.append(acc.toString()).append(";");
            }
        }
        s.append("}");
        return s.toString();
    }

    public class Builder {

        private Builder() {
        }

        public Builder id(long individualPartyNameID) {
            IndividualParty.this.individualPartyID = individualPartyNameID;
            return this;
        }

        public Builder name(String individualPartyName) {
            IndividualParty.this.name = individualPartyName;
            return this;
        }

        public Builder phone(String individualPartyNamePhone) {
            IndividualParty.this.phone = individualPartyNamePhone;
            return this;
        }

        public IndividualParty build() {
            return IndividualParty.this;
        }
    }
}
