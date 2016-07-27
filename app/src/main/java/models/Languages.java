package models;

/**
 * Languages - an enum of language codes supported by the Yandex API
 */
public enum Languages {
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
  UKRAINIAN("uk"),
  JAPANESE("ja");

  private final String language;


  private Languages(final String pLanguage) {
    language = pLanguage;
  }

  public static Languages fromString(final String pLanguage) {
  Languages  result = null;
      for (Languages l: values()){
          if(l.language.equals(pLanguage)){
              result = l;
          }
      }
  return result;
  }

  @Override
  public String toString() {
    return name();
  }

}
