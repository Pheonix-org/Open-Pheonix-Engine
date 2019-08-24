package TestProject.Main;

/**
 * Import the Java Gaming Engine Library.
 */
import com.Shinkson47.JGEL.BackEnd.Operation.ErrorManagement.Hypervisor;

/**
 * Project example of the implementation of the Java Gaming Engine.
 * 
 * @author gordie
 *
 */
public class TestProject {
	/**
	 * It is recommended that your hypervisor always be static for easy access for the game within it.
	 */
	public static Hypervisor Hypervisor;

	/**
	 * Your application starts here, and so does the implementation.
	 * @param args
	 */
	public static void main(String[] args) {		
		/**
		 * Create a hypervisor to monitor, manage and execute your game.
		 * 
		 * The hypervisor can only be assigned one application script, which cannot be changed once assigned.
		 * 
		 * It supports assignments upon instance creation, which is designed for ease of use;
		 * creating the hypervisor this way automatically begins execution.
		 * 
		 * Using .assign() can be used to assign without starting the script yet.
		 */
		Hypervisor = new Hypervisor(new TestStartup());			
		
		
		
		
		
		
		
		// or to delay startup or to change configurations use
		
		Hypervisor hypervisor = new Hypervisor();
		hypervisor.Assign(new TestStartup());
		
		//When ready
		hypervisor.Start();
	}
}