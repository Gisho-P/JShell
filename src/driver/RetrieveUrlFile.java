package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dhrumil on 01/07/16.
 */
public class RetrieveUrlFile {
    MySession session;
    public RetrieveUrlFile(MySession session) {
        this.session = session;
    }

    public String exec() {
        String msg = "";
        try {
            URL url = new URL("http://www.cs.cmu.edu/~spok/grimmtmp/073.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (url.openStream()));

            //what about urls that end with slashes? Do we have to consider
            // those?
            String filename = url.getPath().substring(url.getPath()
                    .lastIndexOf('/') + 1);

            File file = new File(filename);

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                file.appendContent(inputLine);
            }
            in.close();

            session.getCurrentDir().add(file);

            System.out.println(file.getContent());
        } catch (MalformedURLException e) {
            msg += "Malformed URL.\n";
        } catch (IOException e) {
            msg += "Error reading data from URL\n";
        } catch (FileTypes.InvalidName invalidName) {
            msg += invalidName.getMessage() + "\n";
        } catch (Directory.InvalidAddition invalidAddition) {
            msg += invalidAddition.getMessage() + "\n";
        } catch (Directory.NameExistsException e) {
            msg += e.getMessage() + "\n";
        }
        return msg;

    }

//    public static void main(String[] args) {
//        try {
//            URL url = new URL("http://www.cs.cmu.edu/~spok/grimmtmp/073.txt");
//            BufferedReader in = new BufferedReader(new InputStreamReader
//                    (url.openStream()));
//
//            //what about urls that end with slashes?
//            String filename = url.getPath().substring(url.getPath()
//                    .lastIndexOf('/') + 1);
//
//            File file = new File(filename);
//
//            String inputLine;
//
//            while ((inputLine = in.readLine()) != null) {
//                file.appendContent(inputLine);
//            }
//            in.close();
//
//            session.getCurrentDir().add(file);
//
//            System.out.println(file.getContent());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (FileTypes.InvalidName invalidName) {
//            invalidName.printStackTrace();
//        }
//    }

}
