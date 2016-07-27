package models;

/**
 * Created by XPS on 07/27/2016.
 */
public class Language {
    private Languages mLanguage;

    private Integer mFlagRecourceId;
    public Language(Languages language,Integer flagRecourceId) {
        this.mLanguage = language;
        this.mFlagRecourceId = flagRecourceId;
    }

    public Language(Languages language) {
        this.mLanguage = language;
    }

    public Language(Integer flagRecourceId) {
        this.mFlagRecourceId = flagRecourceId;
    }

    public Languages getLanguage() {
        return mLanguage;
    }

    public Integer getFlagRecourceId() {
        return mFlagRecourceId;
    }

}
