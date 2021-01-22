package com.base.web.config.upload;

/**
 * @author ming
 * @version 1.0.0
 * @date 2021/1/5 10:54
 **/
public class Oss {
    private AliOss aliOss;

    public AliOss getAliOss() {
        return aliOss;
    }

    public void setAliOss(AliOss aliOss) {
        this.aliOss = aliOss;
    }

    @Override
    public String toString() {
        return "Oss{" +
                "aliOss=" + aliOss +
                '}';
    }
}
