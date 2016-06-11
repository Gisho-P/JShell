package driver;

public class CommandInterpreter {
	
	public CommandInterpreter(){
	}
	
	public String interpretCmd(String cmd){
		System.out.println("DEBUG: Your cmd is " + cmd);
		
		// Store the output here
		String output = "";
		
		// Splitting the cmd
		String[] cmdArgs = cmd.split(" ");
		switch(cmdArgs[0]){
			case "cat":
				break;
			case "mkdir":
				break;
			case "cp":
				break;
			// Put whatever commands are needed	
			
		};
		return "Output of the cmd";
	}
}


