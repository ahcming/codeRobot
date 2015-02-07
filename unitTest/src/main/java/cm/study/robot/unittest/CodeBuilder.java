package cm.study.robot.unittest;

/**
 * 代码生成父类
 */
public class CodeBuilder {

	private Class<?> target;
	
	private String testJavaFile;
	
	public CodeBuilder target(Class<?> target) {
		this.target = target;
		return this;
	}
	
	public CodeBuilder testFile( String testFile ) {
		testJavaFile = testFile;
		return this;
	}
	
}
