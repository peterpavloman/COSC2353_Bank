package beans;

import javax.ejb.Remote;

@Remote
public interface DebugResetDBSchemaRemote
{
	public void resetTables();
}
