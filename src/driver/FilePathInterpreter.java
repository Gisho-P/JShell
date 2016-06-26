package driver;

import java.util.ArrayList;
import java.util.Iterator;

public class FilePathInterpreter {

	/**
	 * Finds the location and returns the existing dir with the input path.
	 * @param init The init dir to move from.
	 * @param path The path relative to the init dir.
	 * @return The dir at that valid path.
	 * @throws InvalidDirectoryPathException
	 */
	public static FileTypes interpretPath(Directory init, String path)
			throws InvalidDirectoryPathException {
		if(!path.equals("")){
			
			// Splits our path up
			String[] splitPath = path.split("/");
	
			// Checks to see if our first is "/" i.e. is it back to root?
			if (path.charAt(0) == '/') {
				while (init.getParent() != null) {
					init = init.getParent();
				}
				return interpretPathRecursive(init,
						path.substring(1, path.length()));
			}
			return interpretPathRecursive(init, path);
		}
		return init;
	};

	/**
	 * Recursively goes through a file path and finds the dir of file at that path.
	 * @param init The current dir the path is relative to.
	 * @param currPath The current path of the dir/file.
	 * @return The dir or file at that path.
	 * @throws InvalidDirectoryPathException
	 */
	private static FileTypes interpretPathRecursive(Directory init,
			String currPath) throws InvalidDirectoryPathException {
		// Checks to see whether the current path is nothing if it is we found
		// our init	
		currPath = removeLeadingSlashes(currPath);
		if (!currPath.equals("")) {
			
			String[] splitPath = currPath.split("/");
			// Same case as before except with ..
			if (splitPath[0].equals("..")) {
				// Checking to see if we're at the current root of not
				if (init.getParent() != null) {
					// Checking if we have '../moredirs/evenmore' or just '..'
					if (splitPath.length > 1) {
						return interpretPathRecursive(init.getParent(),
								currPath.substring(3, currPath.length()));
					} else {
						return interpretPathRecursive(init.getParent(),
								currPath.substring(2, currPath.length()));
					}
				} else {
					// Checking if we have '../moredirs/evenmore' or just '..'
					if (splitPath.length > 1) {
						return interpretPathRecursive(init,
								currPath.substring(3, currPath.length()));
					} else {
						return interpretPathRecursive(init,
								currPath.substring(2, currPath.length()));
					}
				}
			}

			// Just return itself
			if (splitPath[0].equals(".")) {
				// Checking if we have './moredirs/evenmore' or just '.'
				if (splitPath.length > 1) {
					return interpretPathRecursive(init,
							currPath.substring(2, currPath.length()));
				} else {
					return interpretPathRecursive(init,
							currPath.substring(1, currPath.length()));
				}
			}

			// Checking sub dirs
			ArrayList<Directory> subDirs = init.getChildDirs();
			Iterator<Directory> dirIterator = subDirs.iterator();

			// Looping over to see if what we're looking for is a dir
			while (dirIterator.hasNext()) {
				Directory next = dirIterator.next();
				if (next.getName().equals(splitPath[0])) {

					int length = splitPath[0].length();
					// Go into the new dir
					// Taking care of the '/' in case there are other sub dirs

					if (splitPath.length == 1)
						return interpretPathRecursive(next,
								currPath.substring(length, currPath.length()));
					else
						return interpretPathRecursive(
								next,
								currPath.substring(length + 1,
										currPath.length()));
				}
			}

			// Looping over to see if we're looking for a file instead
			if (splitPath.length == 1) {
				ArrayList<File> files = init.getChildFiles();
				Iterator<File> filesIterator = files.iterator();

				// Else it's a file we're looking for
				while (filesIterator.hasNext()) {
					File next = filesIterator.next();

					if (next.getName().equals(splitPath[0])) {
						return next;
					}
				}
			}

			// Else it doesn't exist and throw an exception
			throw new InvalidDirectoryPathException(
					"There are no files or directories with name " + currPath);
		}
		return init;
	}
	
	/**
	 * Removes the ending slashes in a str i.e.
	 * @param path
	 * @return
	 */
	private static String removeLeadingSlashes(String path){
		int slashes = 0;
		
		for(int i = 0; i < path.length(); i++){
			if(path.charAt(i) != '/'){
				break;
			}
			slashes++;
		}
		return path.substring(0, path.length() - slashes);
	}

	/**
	 * 
	 * @param init
	 *            The initial dir in which the intrepret path was called in
	 * @param path
	 *            The path of the File/Dir TO BE MADE, the last position of the
	 *            path is to be made and does not exist
	 * @return The DIR of the path before the file
	 * @throws InvalidDirectoryPathException
	 */
	public static FileTypes interpretMakePath(Directory init, String path)
			throws InvalidDirectoryPathException {
		if (path != "") {
			String[] splitPath = path.split("/");
			int last = splitPath[splitPath.length - 1].length();

			if (splitPath[0].equals(".")) {

				// Either /t1/t2.. or /t1/t2/ or /
			} else if (splitPath[0].equals("..") || path.equals("/")) {
				return interpretPath(init, "/");
			}

			// if our path has more than 1 subpath e.g. dir/dir1/dir3
			if (splitPath.length > 2) {
				return interpretPath(init,
						path.substring(0, path.length() - last - 1));
			}
			// 1 dir like /test
			return interpretPath(init, path.substring(0, path.length() - last));
			// if our path has exactly than 1 subpath e.g. dir/dir1/dir3
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
