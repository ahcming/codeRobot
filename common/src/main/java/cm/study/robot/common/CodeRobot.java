package cm.study.robot.common;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public interface CodeRobot {

	void scanMethod( CompilationUnit testNode, MethodDeclaration methodDeclara );
}
