package com.base.web.config.upload;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 10:54
 **/
public class AliOss {
    private String accessKey;
    private String accessSecret;
    private String endPoint;
    private String bucketName;
    private String url;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAccessSecret() {
        return accessSecret;
    }

    public void setAccessSecret(String accessSecret) {
        this.accessSecret = accessSecret;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AliOss{" +
                "accessKey='" + accessKey + '\'' +
                ", accessSecret='" + accessSecret + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
