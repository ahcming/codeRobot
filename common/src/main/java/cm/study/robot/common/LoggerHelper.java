package cm.study.robot.common;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;

public class LoggerHelper {

	public static void debug( Object object ) {
		if( object == null ) {
			System.out.println( "   debug:#null#\n" );
			return;
		}

		System.out.println( "   debug:" + object.getClass() + "#" + object + "#\n" );
	}

	public static void debug( ASTNode parent, StructuralPropertyDescriptor prop ) {
		System.out.println( "   debug:#" + prop + "#" + parent.getStructuralProperty( prop ) + "#\n" );
	}

}
