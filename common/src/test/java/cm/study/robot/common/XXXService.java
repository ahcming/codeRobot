package cm.study.robot.common;

import java.util.HashMap;
import java.util.Map;

public class XXXService {

	private Map<String, Object> xxxDao = new HashMap<String, Object>();

	public boolean doSomething() {
		String name = ( String )xxxDao.get( "name" );
		if( name.equals( "bbb" ) ) {
			return true;
		} else {
			return false;
		}
	}
}
