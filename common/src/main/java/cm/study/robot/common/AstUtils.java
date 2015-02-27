package cm.study.robot.common;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import ext.test4j.apache.commons.io.FileUtils;

public class AstUtils {

	private CompilationUnit _root;

	private AstUtils( String javaFile ) throws Exception {
		String source = FileUtils.readFileToString( new File( javaFile ), "UTF-8" );
		ASTParser parser = ASTParser.newParser( AST.JLS3 );
		parser.setSource( source.toCharArray() );

		_root = ( CompilationUnit )parser.createAST( null );
		_root.recordModifications();
	}

	public static AstUtils build( String javaFile ) throws Exception {
		AstUtils self = new AstUtils( javaFile );
		return self;
	}

	public void scan() {
		List<StructuralPropertyDescriptor> clazzPropeties = _root.structuralPropertiesForType();
		for( StructuralPropertyDescriptor clazzPropety : clazzPropeties ) {
			if( "types".equals( clazzPropety.getId() ) ) {
				// System.out.println( clazzPropety );
				List<TypeDeclaration> types = ( List<TypeDeclaration> )_root.getStructuralProperty( clazzPropety );
				scan_types( types );
			}
		}
	}

	public void scan_types( List<TypeDeclaration> types ) {
		for( TypeDeclaration type : types ) {
			MethodDeclaration[] methodDeclaras = type.getMethods();
			for( MethodDeclaration methodDeclara : methodDeclaras ) {
				scan_method( methodDeclara );
			}
		}
	}

	public void scan_method( MethodDeclaration methodDeclara ) {
		List<StructuralPropertyDescriptor> methodPropeties = methodDeclara.structuralPropertiesForType();
		for( StructuralPropertyDescriptor propertyDesc : methodPropeties ) {
			System.out.println( propertyDesc.getClass() + " => " + propertyDesc );
			Object method = methodDeclara.getStructuralProperty( propertyDesc );
			debug( method );
		}
	}

	public void debug( Object object ) {
		if( object == null ) {
			System.out.println( "=====null" );
			return;
		}

		System.out.println( object.getClass() + "@" + object + "@" );
	}

	@Override
	public String toString() {
		return _root.toString();
	}
}
