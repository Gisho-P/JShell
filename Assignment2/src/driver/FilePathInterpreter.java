package driver;

import java.util.ArrayList;
import java.util.Iterator;

public class FilePathInterpreter {

	public static FileTypes interpretPath(Directory init, String path) throws InvalidDirectoryPathException{
		//System.out.println(path + "AFTER");
		if(path != ""){
			if(path.charAt(path.length() - 1) == '/' && !path.equals("/"))
				return interpretPathRecursive(init, path.substring(0, path.length() - 1));
			char first = path.charAt(0);
			if(first == '/'){
				//Directory parent = init.getParent();
				while(init.getParent() != null){
					init = init.getParent();
				}
				return interpretPathRecursive(init, path.substring(1, path.length()));
			}
			return interpretPathRecursive(init, path);	
			}
		else
			return init;
	};
	
	private static FileTypes interpretPathRecursive(Directory init, String currPath) throws InvalidDirectoryPathException{
		if(!currPath.equals("")){
			String[] splitPath = currPath.split("/");
			
			if(splitPath[0].equals("..")){
				if(init.getParent() != null){
					if(splitPath.length > 1){
						return interpretPathRecursive(init.getParent(), currPath.substring(3, currPath.length()));
					}
					else{
						return interpretPathRecursive(init.getParent(), currPath.substring(2, currPath.length()));
					}
				}
				else{
					if(splitPath.length > 1){
						return interpretPathRecursive(init, currPath.substring(3, currPath.length()));
					}
					else{
						return interpretPathRecursive(init, currPath.substring(2, currPath.length()));
					}
				}
			}
			
			if(splitPath[0].equals(".")){
				if(splitPath.length > 1){
					return interpretPathRecursive(init, currPath.substring(2, currPath.length()));
				}
				else{
					return interpretPathRecursive(init, currPath.substring(1, currPath.length()));
				}
			}
			
			
			// Checking sub dirs
			ArrayList<Directory> subDirs = init.getChildDirs();
			Iterator<Directory> dirIterator= subDirs.iterator();
			
			while(dirIterator.hasNext()){
				Directory next = dirIterator.next();
				if(next.getName().equals(splitPath[0])){
					
					int length = splitPath[0].length();
					// Go into the new dir
					// Taking care of the '/' in case there are other sub dirs
					
					//System.out.println(splitPath.length);
					if(splitPath.length == 1)
						return interpretPathRecursive(next, currPath.substring(length, currPath.length()));
					else
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
	
	public static FileTypes interpretMakePath(Directory init, String path) throws InvalidDirectoryPathException{
		if(path != ""){
			if(!path.equals("/")){
				String[] splitPath = path.split("/");
				int last = splitPath[splitPath.length - 1].length();
				
				//System.out.println(path + "BEFORE");
				if(splitPath.length > 1){
					if((path.length() - last - 1) == 0){
						return interpretPath(init, "/");	
					}
					return interpretPath(init, path.substring(0, path.length() - last - 1));	
				}
				
				return interpretPath(init, path.substring(0, path.length() - last));	
				}
			
			return interpretPath(init, path);
		}
		return init.getParent();
	};
	
    public static class InvalidDirectoryPathException extends Exception {
    	private static final long serialVersionUID = 59L;
        public InvalidDirectoryPathException(String message) {
            super(message);
        }
    }
}
