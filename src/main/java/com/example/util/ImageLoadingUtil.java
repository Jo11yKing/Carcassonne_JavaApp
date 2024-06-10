package com.example.util;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Utility class to load and manage images for the application.
 */
public class ImageLoadingUtil {

    /**
     * Loads an image from the specified path.
     * @param path The relative path to the image file.
     * @return The loaded image.
     */
    public static Image loadImage(String path) {
        try {
            return new Image(ImageLoadingUtil.class.getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("Error loading image: " + path + "\n" + e.getMessage());
            return null;
        }
    }

    /**
     * Creates an ImageView with a preloaded image from the specified path.
     * @param path The relative path to the image file.
     * @param width The width to resize the image to (preserving aspect ratio).
     * @param height The height to resize the image to (preserving aspect ratio).
     * @return An ImageView containing the resized image.
     */
    public static ImageView createImageView(String path, double width, double height) {
        Image image = loadImage(path);
        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);
            return imageView;
        }
        return null;
    }

    /**
     * Creates a high DPI image icon from the specified path.
     * @param path The relative path to the image file.
     * @param width The width to resize the image to.
     * @param height The height to resize the image to.
     * @return An ImageView containing the resized high DPI image.
     */
    public static ImageView createHighDpiImageIcon(String path, double width, double height) {
        // Assuming the source images are at a higher resolution and we scale them down for high DPI displays
        return createImageView(path, width, height);
    }
}
