package common;

public class User {
    private int id;
    private int rating;
    private int credit;
    private String username;
    private String contact;

    public User(int id, int rating, int credit, String username, String contact) {
        this.id = id;
        this.rating = rating;
        this.credit = credit;
        this.username = username;
        this.contact = contact;
    }

    public int getID() {
        return id;
    }

    public int getRating() {
        return rating;
    }

    public int getCredit() {
        return credit;
    }

    public String getUsername() {
        return username;
    }

    public String getContact() {
        return contact;
    }
}
