package driver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RetrieveUrlFile implements Command {
    MySession session;
    Output out;
    public RetrieveUrlFile(MySession session) {
        this.session = session;
        this.out = new Output();
    }

    @Override
    public String man() {
        return null;
    }

    @Override
    public Output interpret(List<String> args) {
        if (args.size() != 2) {
            out.withStdError("usage: curl url");
        }
        return exec(args.subList(1, args.size()));
    }

    public Output exec(List<String> args) {
        //http://www.cs.cmu.edu/~spok/grimmtmp/073.txt
        try {;
            URL url = new URL(args.get(0));
            BufferedReader in = new BufferedReader(new InputStreamReader
                    (url.openStream()));

            //what about urls that end with slashes? Do we have to consider
            // those? What about urls with no files at the end? Or urls with
            // no slashes at all
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
            out.addStdError("Malformed URL.\n");
        } catch (IOException e) {
            out.addStdError("Error reading data from URL\n");
        } catch (FileTypes.InvalidName invalidName) {
            out.addStdError(invalidName.getMessage() + "\n");
        } catch (Directory.InvalidAddition invalidAddition) {
            out.addStdError(invalidAddition.getMessage() + "\n");
        } catch (Directory.NameExistsException e) {
            out.addStdError(e.getMessage() + "\n");
        }

        return out;

    }


}
