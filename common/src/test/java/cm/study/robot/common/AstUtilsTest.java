package cm.study.robot.common;

import static org.testng.Assert.*;

import org.testng.annotations.Test;


public class AstUtilsTest {

	private String javaFile = "/home/chenming/ws-plugin/codeRobot/common/src/test/java/cm/study/robot/common/XXXService.java";
	
	@Test
	public void AstUtils() throws Exception {
		AstUtils.build( javaFile ).scan();
	}


}
