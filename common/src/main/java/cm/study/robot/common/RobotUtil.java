package cm.study.robot.common;

import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.TypeDeclaration;


public class RobotUtil {

	public static Name string2Name(AST ast, String[] identifiers ) {
		if( identifiers == null || identifiers.length == 0 ) {
			return ast.newSimpleName( "" );
		} else if( identifiers.length == 1 ) {
			return ast.newSimpleName( identifiers[0] );
		}
		
		QualifiedName base = ast.newQualifiedName(ast.newSimpleName( identifiers[0] ), ast.newSimpleName( identifiers[1] ) );
		for( int i = 2; i < identifiers.length; i++) {
			base = ast.newQualifiedName( base, ast.newSimpleName( identifiers[i] ) );
		}
		
		return base;
	}

	/**
	 * 给字段，方法增加注解
	 */
	public static void addAnnotation( CompilationUnit rootNode, BodyDeclaration target, String annoName, String annoTypeName ) {
		List modifers = target.modifiers();
		// for( Object obj : modifers ) { // 判断是否已经加了此注解
		// debug( obj );
		// }

		AST ast = target.getAST();
		MarkerAnnotation anno = ast.newMarkerAnnotation();
		anno.setTypeName( ast.newSimpleName( annoName ) );
		modifers.add( 0, anno ); // 增加注解

		ImportDeclaration import_testAnno = ast.newImportDeclaration();
		import_testAnno.setName( RobotUtil.string2Name( ast, annoTypeName.split( "\\." ) ) );
		addImport( rootNode, import_testAnno );
	}

	public static void addImport( CompilationUnit rootNode, ImportDeclaration importDeclaration ) {
		List<ImportDeclaration> importDeclarations = rootNode.imports();
		boolean contain = false;
		for( ImportDeclaration _imDeclaration : importDeclarations ) { // 不要重复import一个类
			if( _imDeclaration.getName().getFullyQualifiedName().equals( importDeclaration.getName().getFullyQualifiedName() ) ) {
				contain = true;
			}
		}

		if( !contain ) {
			rootNode.imports().add( importDeclaration ); // import 注解
		}
	}
	
	public static List<BodyDeclaration> getBody(CompilationUnit testNode) {
		TypeDeclaration typeDecla = ( TypeDeclaration )testNode.types().get( 0 ); // 每个测试类只有一个TypeDeclaration
		return typeDecla.bodyDeclarations();
	}

}
