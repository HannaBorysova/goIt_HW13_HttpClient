package ua.goit.http;

import ua.goit.user.Address;
import ua.goit.user.Company;
import ua.goit.user.Geo;
import ua.goit.user.User;

public class UserCreation {
    public static User createDefaultUser() {
        User user = new User();
        user.setId(225);
        user.setName("Hanna Borysova");
        user.setUsername("borya.13");
        user.setEmail("borysova@secret.net");
        user.setAddress(createDefaultAddress());
        user.setPhone("093-13-31-13");
        user.setWebsite("chsinc.com");
        user.setCompany(createDefaultCompany());
        return user;
    }

    public static Address createDefaultAddress() {
        Address address = new Address();
        address.setStreet("Radisna");
        address.setSuite("suite 7");
        address.setCity("Kyiv");
        address.setZipcode("03-035");
        address.setGeo(createDefaultGeo());
        return address;
    }

    public static Geo createDefaultGeo() {
        Geo geo = new Geo();
        geo.setLat(55.1254);
        geo.setLng(-68.146);
        return geo;
    }

    public static Company createDefaultCompany() {
        Company company = new Company();
        company.setName("CHS");
        company.setCatchPhrase("Grown powerful");
        company.setBs("Execute documents");
        return company;
    }
}
