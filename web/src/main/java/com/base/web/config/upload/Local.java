package com.base.web.config.upload;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 10:53
 **/

public class Local {
    private String path;
    private String url;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Local{" +
                "path='" + path + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
