package ci.babatchai.nouvelleslocales.data;

public enum ApiLangages {
    ARABIC("ar"),
    GERMAN("de"),
    ENGLISH("en"),
    SPANISH("es"),
    FRENCH("fr"),
    HEBREW("he"),
    ITALIAN("it"),
    DUTCH("nl"),
    NORWEGIAN("no"),
    PORTUGESE("pt"),
    RUSSIAN("ru"),
    SWEDISH("se"),
    CHINESE("zh");

    private String text;

    ApiLangages(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static ApiLangages fromString(String text) {
        for (ApiLangages apiLangages : ApiLangages.values()) {
            if (apiLangages.text.equalsIgnoreCase(text)) {
                return apiLangages;
            }
        }
        return null;
    }
}
