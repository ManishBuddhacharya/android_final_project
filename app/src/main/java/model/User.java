package model;

public class User {
    private String name, username, usertype, imageName, email, address, contact, password, path;
    private int id;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public User(String name, String username, String email, String address, String contact, int id) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.id = id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String name, String username, String usertype, String email, String address, String contact, String password) {
        this.name = name;
        this.username = username;
        this.usertype = usertype;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.password = password;
    }

    public User(String name, String username, String email, String address, String contact) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.contact = contact;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public User(String name, String username, String email, String address, String contact, String imageName, String password, int id) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.imageName = imageName;
        this.password = password;
        this.id = id;
    }

    public User(String name, String username, String email, String address, String contact, String imageName, int id) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.imageName = imageName;
        this.password = password;
        this.id = id;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
