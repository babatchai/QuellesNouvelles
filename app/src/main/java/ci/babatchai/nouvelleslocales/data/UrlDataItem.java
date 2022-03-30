package ci.babatchai.nouvelleslocales.data;

import java.util.UUID;

public class UrlDataItem {
    String feedName;
    String feedUrl;
    String feedGuid;
    String titleTagClass;
    String titleTag;
    String articleTagClass;
    String articleTag;
    String titleTagSuite;
    String articleTagSuite;
    String feedDistiller;
    String feedCategorie;

    public String getFeedCategorie() {
        return feedCategorie;
    }

    public void setFeedCategorie(String feedCategorie) {
        this.feedCategorie = feedCategorie;
    }

    public String getFeedDistiller() {
        return feedDistiller;
    }

    public void setFeedDistiller(String feedDistiller) {
        this.feedDistiller = feedDistiller;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public String getFeedUrl() {
        return feedUrl;
    }

    public void setFeedUrl(String feedUrl) {
        this.feedUrl = feedUrl;
    }

    public String getFeedGuid() {
        return feedGuid;
    }

    public void setFeedGuid(String feedGuid) {
        this.feedGuid = feedGuid;
    }

    public String getTitleTagClass() {
        return titleTagClass;
    }

    public void setTitleTagClass(String titleTagClass) {
        this.titleTagClass = titleTagClass;
    }

    public String getTitleTag() {
        return titleTag;
    }

    public void setTitleTag(String titleTag) {
        this.titleTag = titleTag;
    }

    public String getArticleTagClass() {
        return articleTagClass;
    }

    public void setArticleTagClass(String articleTagClass) {
        this.articleTagClass = articleTagClass;
    }

    public String getArticleTag() {
        return articleTag;
    }

    public void setArticleTag(String articleTag) {
        this.articleTag = articleTag;
    }

    public String getTitleTagSuite() {
        return titleTagSuite;
    }

    public void setTitleTagSuite(String titleTagSuite) {
        this.titleTagSuite = titleTagSuite;
    }

    public String getArticleTagSuite() {
        return articleTagSuite;
    }

    public void setArticleTagSuite(String articleTagSuite) {
        this.articleTagSuite = articleTagSuite;
    }
}
