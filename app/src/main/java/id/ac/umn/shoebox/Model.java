package id.ac.umn.shoebox;

/**
 * CardView Help
 */

public class Model {
    private int ImageService;
    private String Title;

    public Model(int imageService, String title) {
        ImageService = imageService;
        Title = title;
    }

    public int getImageService() {
        return ImageService;
    }

    public void setImageService(int imageService) {
        ImageService = imageService;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
