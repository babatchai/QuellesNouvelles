package ci.babatchai.nouvelleslocales.data;

public enum ApiCategories {
    GENERAL("general"),
    BUSINESS("business"),
    ENTERTAINEMENT("entertainment"),
    HEALTH("health"),
    SCIENCE ("science"),
    SPORTS("sports"),
    TECHNOLOGY("technology");

    private String text;

    ApiCategories(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static ApiCategories fromString(String text) {
        for (ApiCategories rssTagTyes : ApiCategories.values()) {
            if (rssTagTyes.text.equalsIgnoreCase(text)) {
                return rssTagTyes;
            }
        }
        return null;
    }
}
