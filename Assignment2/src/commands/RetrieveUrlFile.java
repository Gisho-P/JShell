package commands;

import java.io.*;
import java.net.*;
import java.util.List;

import driver.MySession;
import exceptions.*;
import structures.File;

public class RetrieveUrlFile implements Command {
  MySession session;

  public RetrieveUrlFile(MySession session) {
    this.session = session;
  }

  /**
   * Store the man page for the curl command
   * 
   * @return The string representation of the man page
   */
  public void man() {
    session.setOutput("CURL(1)\t\t\t\tUser Commands\t\t\t\tCURL(1)\n"
        + "\nNAME\n\t\tcurl - Add the file at the given url "
        + "to the current directory\n\nSYNOPSIS\n\t\t"
        + "curl URL\n\nDESCRIPTION\n\t\t"
        + "Add the file at given url to the current directory."
        + "\n\t\tThe name of the file is the same as the file in"
        + " the url.");
  }

  /**
   * Interpret the command arguments
   * 
   * @param args The arguments given in the curl command
   * @return Message after the command is executed
   */
  public void interpret(List<String> args) {
    // usage error if incorrect num of args
    if (args.size() != 2) {
      session.setError("usage: curl url");
    }
    // Otherwise execute the command
    exec(args.subList(1, args.size()));
  }

  /**
   * Execute the command given the arguments
   * 
   * @param args Arguments used by function to complete command
   * @return Message related to the command execution
   */
  public void exec(List<String> args) {
    // http://www.cs.cmu.edu/~spok/grimmtmp/073.txt
    try {
      // Connect to the url
      URL url = new URL(args.get(0));
      BufferedReader in =
          new BufferedReader(new InputStreamReader(url.openStream()));

      // what about urls that end with slashes? Do we have to consider
      // those? What about urls with no files at the end? Or urls with
      // no slashes at all

      // Determine the file name to insert in the current directory
      String[] parseUrl = args.get(0).split("/");
      String filename = parseUrl[parseUrl.length - 1];
      File file = new File(filename);

      String inputLine;
      // Get data from the url and add it to the file
      while ((inputLine = in.readLine()) != null) {
        file.appendContent(inputLine);
      }
      in.close();
      // Add the file to the current directory
      session.getCurrentDir().addReplace(file);

      // System.out.println(file.getContent());
    } catch (MalformedURLException e) {
      session.addError("Malformed URL.");
    } catch (IOException e) {
      session.addError("Error reading data from URL");
    } catch (InvalidNameException invalidName) {
      session.addError(invalidName.getMessage());
    } catch (InvalidAdditionException invalidAddition) {
      session.addError(invalidAddition.getMessage());
    }

  }
}
