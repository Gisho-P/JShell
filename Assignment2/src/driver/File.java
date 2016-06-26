package driver;

/**
 * Created by dhrumil on 12/06/16.
 */
public class File extends FileTypes {
  private String content;

  public File(String name) throws InvalidName {
    super(name);
    content = "";
  }

  public File(String name, String content) throws InvalidName {
    super(name);
    this.content = content;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void appendContent(String content) {
    this.content += "\n" + content;
  }

  public String toString() {
    return "File Name: " + getName();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof File) {
      if (((File) obj).getName().equals(getName())
          && ((File) obj).getContent().equals(getContent()))
        return true;
    }
    return false;
  }
}
