package driver;

import java.util.ArrayList;
import java.util.Iterator;

public class FilePathInterpreter {

	public static Directory interpretPath(Directory init, String path) throws InvalidDirectoryPathException{
		return interpretPathRecursive(init, path);
	};
	
	public static Directory interpretPathRecursive(Directory init, String currPath) throws InvalidDirectoryPathException{
		
		if(currPath != ""){
			String[] splitPath = currPath.split("/");
			
			ArrayList<Directory> subDirs = init.getChildDirs();
			Iterator<Directory> dirIterator= subDirs.iterator();
			
			while(dirIterator.hasNext()){
				if(dirIterator.next().getName().equals(splitPath[0])){
					
					int length = splitPath[0].length();
					// Go into the new dir
					return interpretPathRecursive(init, currPath.substring(length + 1, currPath.length()));
				}
			}
			
			// Else it doesn't exist and throw an expcetion
	        throw new InvalidDirectoryPathException("There are no files or directories with name " + currPath);	
		}
		return init;
	}
	
    public static class InvalidDirectoryPathException extends Exception {
        public InvalidDirectoryPathException(String message) {
            super(message);
        }
    }
}
