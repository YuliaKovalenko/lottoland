package common.entities;

public enum User {
    USER1 ("Jan van Dam"),
    USER2 ("Chack Norris"),
    USER3 ("Klark n Kent"),
    USER4 ("John Daw"),
    USER5 ("Bat Man"),
    USER6 ("Tim Los"),
    USER7 ("Dave o Core"),
    USER8 ("Pay Pal"),
    USER9 ("Lazy Cat"),
    USER10 ("Jack & Johnes");

    private String name;

    User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return name.split(" ")[0];
    }
    public String getLastName() {
        int lengthName = name.split(" ").length;
        return name.split(" ")[lengthName - 1];
    }
}
