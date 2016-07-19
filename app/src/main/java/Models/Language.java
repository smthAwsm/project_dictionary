package models;

/**
 * Language - an enum of language codes supported by the Yandex API
 */
public enum Language {
  ALBANIAN("sq"),
  ARMENIAN("hy"),
  AZERBAIJANI("az"),
  BELARUSIAN("be"),
  BULGARIAN("bg"),
  CATALAN("ca"),
  CROATIAN("hr"),
  CZECH("cs"),
  DANISH("da"),
  DUTCH("nl"),
  ENGLISH("en"),
  ESTONIAN("et"),
  FINNISH("fi"),
  FRENCH("fr"),
  GERMAN("de"),
  GEORGIAN("ka"),
  GREEK("el"),
  HUNGARIAN("hu"),
  ITALIAN("it"),
  LATVIAN("lv"),
  LITHUANIAN("lt"),
  MACEDONIAN("mk"),
  NORWEGIAN("no"),
  POLISH("pl"),
  PORTUGUESE("pt"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SERBIAN("sr"),
  SLOVAK("sk"),
  SLOVENIAN("sl"),
  SPANISH("es"),
  SWEDISH("sv"),
  TURKISH("tr"),
  UKRAINIAN("uk");

  private final String language;


  private Language(final String pLanguage) {
    language = pLanguage;
  }

  public static Language fromString(final String pLanguage) {
    for (Language l : values()) {
      if (l.name().equals(pLanguage)) {
        return l;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return language;
  }

}
