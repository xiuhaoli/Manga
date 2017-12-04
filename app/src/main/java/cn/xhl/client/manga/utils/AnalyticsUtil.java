package cn.xhl.client.manga.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import cn.xhl.client.manga.MyApplication;

/**
 * Created by xiuhaoli on 2017/11/16.
 */

public class AnalyticsUtil {
    private String screenName;
    private String location;
    private String referrer;
    private String page;
    private String hostname;
    private String title;
    private String language;
    private String encoding;
    private String screenColors;
    private String viewportSize;
    private String clientId;
    private String appName;
    private String appId;
    private String appInstallerId;

    private AnalyticsUtil(ScreenBuilder screenBuilder) {
        Tracker tracker = MyApplication.getAppContext().getDefaultTracker();
        if (StringUtil.isNotEmpty(screenBuilder.screenName)) {
            tracker.setScreenName(screenBuilder.screenName);
        }
        if (StringUtil.isNotEmpty(screenBuilder.location)) {
            tracker.setLocation(screenBuilder.location);
        }
        if (StringUtil.isNotEmpty(screenBuilder.referrer)) {
            tracker.setReferrer(screenBuilder.referrer);
        }
        if (StringUtil.isNotEmpty(screenBuilder.appInstallerId)) {
            tracker.setAppInstallerId(screenBuilder.appInstallerId);
        }
        if (StringUtil.isNotEmpty(screenBuilder.appId)) {
            tracker.setAppId(screenBuilder.appId);
        }
        if (StringUtil.isNotEmpty(screenBuilder.appName)) {
            tracker.setAppName(screenBuilder.appName);
        }
        if (StringUtil.isNotEmpty(screenBuilder.clientId)) {
            tracker.setClientId(screenBuilder.clientId);
        }
        if (StringUtil.isNotEmpty(screenBuilder.viewportSize)) {
            tracker.setViewportSize(screenBuilder.viewportSize);
        }
        if (StringUtil.isNotEmpty(screenBuilder.screenColors)) {
            tracker.setScreenColors(screenBuilder.screenColors);
        }
        if (StringUtil.isNotEmpty(screenBuilder.encoding)) {
            tracker.setEncoding(screenBuilder.encoding);
        }
        if (StringUtil.isNotEmpty(screenBuilder.language)) {
            tracker.setLanguage(screenBuilder.language);
        }
        if (StringUtil.isNotEmpty(screenBuilder.title)) {
            tracker.setTitle(screenBuilder.title);
        }
        if (StringUtil.isNotEmpty(screenBuilder.hostname)) {
            tracker.setHostname(screenBuilder.hostname);
        }
        if (StringUtil.isNotEmpty(screenBuilder.page)) {
            tracker.setPage(screenBuilder.page);
        }
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public static class ScreenBuilder {
        private String screenName;
        private String location;
        private String referrer;
        private String page;
        private String hostname;
        private String title;
        private String language;
        private String encoding;
        private String screenColors;
        private String viewportSize;
        private String clientId;
        private String appName;
        private String appId;
        private String appInstallerId;

        public ScreenBuilder setScreenName(String screenName) {
            this.screenName = screenName;
            return this;
        }

        public ScreenBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public ScreenBuilder setReferrer(String referrer) {
            this.referrer = referrer;
            return this;
        }

        public ScreenBuilder setPage(String page) {
            this.page = page;
            return this;
        }

        public ScreenBuilder setHostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public ScreenBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        public ScreenBuilder setLanguage(String language) {
            this.language = language;
            return this;
        }

        public ScreenBuilder setEncoding(String encoding) {
            this.encoding = encoding;
            return this;
        }

        public ScreenBuilder setScreenColors(String screenColors) {
            this.screenColors = screenColors;
            return this;
        }

        public ScreenBuilder setViewportSize(String viewportSize) {
            this.viewportSize = viewportSize;
            return this;
        }

        public ScreenBuilder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public ScreenBuilder setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public ScreenBuilder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        public ScreenBuilder setAppInstallerId(String appInstallerId) {
            this.appInstallerId = appInstallerId;
            return this;
        }

        public void build() {
            new AnalyticsUtil(this);
        }
    }
}
