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
	 * Your application starts here, and so does the implementation of JGE.
	 * @param args
	 */
	public static void main(String[] args) {		
		
		/**
		 * 
		 * JGEL is designed to be ran in an environment where only one game is present, as such it is static;
		 * The hypervisor is static and can only be assigned one application script, which cannot be changed once assigned.
		 */
		
		Hypervisor.Start(new TestStartup());
	}
}