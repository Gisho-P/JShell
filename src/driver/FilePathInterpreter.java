package driver;

import java.util.ArrayList;
import java.util.Iterator;

public class FilePathInterpreter {

	public static FileTypes interpretPath(Directory init, String path) throws InvalidDirectoryPathException{
		
		if(path != ""){
			char first = path.charAt(0);
			if(first == '/'){
				Directory parent = init.getParent();
				while(parent != null){
					init = parent;
				}
			}
			
			return interpretPathRecursive(init, path.substring(1, path.length()));
			}
		else
			return init;
	};
	
	public static FileTypes interpretPathRecursive(Directory init, String currPath) throws InvalidDirectoryPathException{
		
		if(currPath != ""){
			String[] splitPath = currPath.split("/");
			
			if(splitPath[0].equals("..")){
				return interpretPathRecursive(init.getParent(), currPath.substring(3, currPath.length()));
			}
			
			if(splitPath[0].equals(".")){
				return interpretPathRecursive(init, currPath.substring(2, currPath.length()));
			}
			
			ArrayList<Directory> subDirs = init.getChildDirs();
			Iterator<Directory> dirIterator= subDirs.iterator();
			
			while(dirIterator.hasNext()){
				Directory next = dirIterator.next();
				
				if(next.getName().equals(splitPath[0])){
					
					int length = splitPath[0].length();
					// Go into the new dir
					return interpretPathRecursive(next, currPath.substring(length + 1, currPath.length()));
				}
			}
			
			// If we're at the end looking for a file
			if(splitPath.length == 1){
				ArrayList<File> files = init.getChildFiles();
				Iterator<File> filesIterator = files.iterator();
				
				// Else it's a file we're looking for
				while(filesIterator.hasNext()){
					File next = filesIterator.next();
					
					if(next.getName().equals(splitPath[0])){
						return next;
					}
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
