/**
 * Created by dhrumil on 12/06/16.
 */
public class File {
    private String name;
    private String content;

    public File(String name) {
        this.name = name;
        content = "";
    }

    public File(String name, String content) {
        this.content = content;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void appendContent(String content) {
        this.content += content;
    }

    public String toString() {
        return "File Name: " + getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof File) {
            if (((File)obj).getName().equals(getName()) && ((File)obj).getContent().equals(getName()))
                return true;
        }
        return false;
    }
}
