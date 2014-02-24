package beans;

import javax.ejb.Remote;

/**
 * test class for resetting the DB
 * @author Peter
 */
@Remote
public interface DebugResetDBSchemaRemote
{
	public void doTest();
	public void resetTables();
}
