package driver;

/**
 * Created by dhrumil on 14/06/16.
 */
public abstract class FileTypes {
    String name;

    public FileTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
