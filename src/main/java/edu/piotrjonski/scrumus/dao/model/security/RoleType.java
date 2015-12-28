package edu.piotrjonski.scrumus.dao.model.security;


public enum RoleType {
    ADMIN("ADMIN"),
    PRODUCT_OWNER("PRODUCT_OWNER"),
    SCRUM_MASTER("SCRUM_MASTER"),
    DEVELOPER("DEVELOPER");

    private String name;

    RoleType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
