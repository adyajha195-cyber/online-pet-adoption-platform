package model;

/** Plain data object for one row of the Pet table. Immutable. */
public class Pet {

    private final int petId;
    private final int shelterUserId;
    private final String name;
    private final String type;
    private final String breed;
    private final int age;
    private final String description;
    private final String photo;
    private final String listingStatus;
    private final String petStatus;

    public Pet(int petId, int shelterUserId, String name, String type, String breed, int age, String description, String photo, String listingStatus, String petStatus) {
        this.petId = petId;
        this.shelterUserId = shelterUserId;
        this.name = name;
        this.type = type;
        this.breed = breed;
        this.age = age;
        this.description = description;
        this.photo = photo;
        this.listingStatus = listingStatus;
        this.petStatus = petStatus;
    }

    public int getPetId() { return petId; }
    public int getShelterUserId() { return shelterUserId; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getBreed() { return breed; }
    public int getAge() { return age; }
    public String getDescription() { return description; }
    public String getPhoto() { return photo; }
    public String getListingStatus() { return listingStatus; }
    public String getPetStatus() { return petStatus; }

    @Override
    public String toString() {
        return petId + " | " + shelterUserId + " | " + name + " | " + type + " | " + breed + " | " + age + " | " + description + " | " + photo + " | " + listingStatus + " | " + petStatus;
    }
}
