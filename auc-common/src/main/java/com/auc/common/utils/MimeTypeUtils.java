package com.auc.common.utils;

/**
 * 媒体类型工具类
 */
public class MimeTypeUtils {
    public static final String IMAGE_PNG = "image/png";

    public static final String IMAGE_JPG = "image/jpg";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_BMP = "image/bmp";

    public static final String IMAGE_GIF = "image/gif";

    public static final String[] IMAGE_EXTENSION = {"gif", "jpg", "jpeg", "png"};

    public static final String[] OTHER_EXTENSION = {
        "bmp",
        // word excel powerpoint
        "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "css", "js", "xml", "txt",
        // 音频
        "mp3", "mp4", "m4a",
        // 压缩文件
        "rar", "zip", "gz", "war",
        // ts
        "ts",
        // pdf
        "pdf"
    };

    public static final String[] DEFAULT_ALLOWED_EXTENSION = {
        // 图片
        "gif", "jpg", "jpeg", "png",  "bmp",
        // word excel powerpoint
        "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "css", "js", "xml", "txt",
        // 音频
        "mp3", "mp4", "m4a",
        // 压缩文件
        "rar", "zip", "gz", "war",
        // ts
        "ts",
        // pdf
        "pdf"
    };

    public static String getExtension(String prefix) {
        switch (prefix) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case IMAGE_BMP:
                return "bmp";
            case IMAGE_GIF:
                return "gif";
            default:
                return "";
        }
    }
}
