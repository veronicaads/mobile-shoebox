package id.ac.umn.shoebox;

/**
 * Created by Stefanus K on 5/2/2018.
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
