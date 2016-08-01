package models;

/**
 * Created by XPS on 07/27/2016.
 */
public class Language {
    private TranslateApiLanguage mLanguage;
    private Integer mFlagRecourceId;

    public Language(TranslateApiLanguage language, Integer flagRecourceId) {
        this.mLanguage = language;
        this.mFlagRecourceId = flagRecourceId;
    }

    public Language(TranslateApiLanguage language) {
        this.mLanguage = language;
    }

    public Language(Integer flagRecourceId) {
        this.mFlagRecourceId = flagRecourceId;
    }

    public TranslateApiLanguage getLanguage() {
        return mLanguage;
    }

    public Integer getFlagRecourceId() {
        return mFlagRecourceId;
    }

}
