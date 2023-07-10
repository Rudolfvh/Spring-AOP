package org.example.http.exeption;

public class ImageSizeException extends RuntimeException {

    public ImageSizeException() {
        this("Image size must be lower than 1 MB");
    }

    public ImageSizeException(String message) {
        super(message);
    }
}
