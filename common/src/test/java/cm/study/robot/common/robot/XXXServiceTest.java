package cm.study.robot.common.robot;

import java.util.Map;
import mockit.Tested;
import cm.study.robot.common.XXXService;
import mockit.Injectable;
import org.testng.annotations.Test;

public class XXXServiceTest {

	@Tested
	private XXXService XXXService;

	@Injectable
	private Map<String, Object> xxxDao;

	/** 
	* int age=(Integer)xxxDao.get("age");
	* xxxDao.put("name","boy");
	* xxxDao.put("flag",3);
	* return false;
	*/
	@Test
	public void doSomething_$EE() throws Exception {}

	/** 
	* int age=(Integer)xxxDao.get("age");
	* xxxDao.put("name","boy");
	* xxxDao.put("flag",1);
	* boolean sex=(Boolean)xxxDao.get("sex");
	* xxxDao.put("name","boy");
	* return true;
	*/
	@Test
	public void doSomething_$II() throws Exception {}

	/** 
	* int age=(Integer)xxxDao.get("age");
	* xxxDao.put("name","boy");
	* xxxDao.put("flag",2);
	* return true;
	*/
	@Test
	public void doSomething_$EI() throws Exception {}

	/** 
	* int age=(Integer)xxxDao.get("age");
	* xxxDao.put("name","boy");
	* xxxDao.put("flag",1);
	* boolean sex=(Boolean)xxxDao.get("sex");
	* xxxDao.put("name","girl");
	* return true;
	*/
	@Test
	public void doSomething_$IE() throws Exception {}
}
