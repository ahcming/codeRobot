package cm.study.robot.common;

import java.util.Map;

public class XXXService {

	private Map<String, Object> xxxDao;

	private XXXService() {
		System.out.println( "==" );
	}

	/**
	 * aaaaa
	 * @return
	 * @throws Exception
	 */
	public boolean doSomething() throws Exception {
		int age = ( Integer )xxxDao.get( "age" );
		xxxDao.put( "name", "boy" );

		if( age > 18 ) {
			xxxDao.put( "flag", 1 );
			boolean sex = (Boolean) xxxDao.get( "sex" );
			if(sex) {
				xxxDao.put( "name", "boy" );
			} else{
				xxxDao.put( "name", "girl" );
			}
//			return true;
		} else if( age == 18 ) {
			xxxDao.put( "flag", 2 );
//			return true;
		} else {
			xxxDao.put( "flag", 3 );
//			return false;
		}

		String address = (String) xxxDao.get( "address" );
		if( "china".equals( address ) ) {
			return true;
		} else {
			return false;
		}
	}

}
