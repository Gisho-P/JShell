package driver;

import com.sun.deploy.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by dhrumil on 14/06/16.
 */
public abstract class FileTypes {
    private String name;

    public FileTypes(String name) throws InvalidName {
        if (isValid(name))
            this.name = name;
        else
            throw new InvalidName("Name contains invalid characters");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws InvalidName {
        if (isValid(name))
            this.name = name;
        else
            throw new InvalidName("Name contains invalid characters");
    }

    public boolean isValid(String name) {
        if (name == ".." || name == ".")
            return false;
        else{
            Pattern p = Pattern.compile("[^a-zA-Z0-9.]");
            return !p.matcher(name).find();
        }
    }

    public class InvalidName extends Exception {
        private static final long serialVersionUID = 59L;

        public InvalidName(String message) {
            super(message);
        }
    }


}
