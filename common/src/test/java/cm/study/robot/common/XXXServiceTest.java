package cm.study.robot.common;

import java.util.Map;

import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;

import org.testng.Assert;
import org.testng.annotations.Test;

public class XXXServiceTest {
	
	@Tested
	private XXXService xXXService;
	
	@Injectable
	private Map<String, Object> xxxDao;

	@Test
	public void doSomethingTest() throws Exception {
		new NonStrictExpectations() {
			{
				xxxDao.get( "name" );
				result = "bbb";
			}
		};
		
		Assert.assertTrue( xXXService.doSomething() );
	}
}
