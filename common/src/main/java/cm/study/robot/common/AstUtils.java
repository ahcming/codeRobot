package cm.study.robot.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import mockit.Injectable;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.beust.jcommander.internal.Console;

import ext.test4j.apache.commons.io.FileUtils;

public class AstUtils {

	private CompilationUnit _rootNode;

	private String testClassPath;

	private String className;

	private String pkgName;

	private CompilationUnit _testNode;
	
	private CodeRobot codeRobot;
	
	public static CodeRobot SERVICE = new ServiceRobot();
	public static CodeRobot DAO = new DaoRobot();

	private AstUtils( String javaFile ) throws Exception {
		setTestClassPath( javaFile );

		String source = FileUtils.readFileToString( new File( javaFile ), "UTF-8" );
		ASTParser parser = ASTParser.newParser( AST.JLS3 );
		parser.setSource( source.toCharArray() );
		parser.setKind( ASTParser.K_COMPILATION_UNIT );

		_rootNode = ( CompilationUnit )parser.createAST( null );
		_rootNode.recordModifications();

		ASTParser testParser = ASTParser.newParser( AST.JLS3 );
		testParser.setSource( "".toCharArray() );
		_testNode = ( CompilationUnit )testParser.createAST( null );
		_testNode.recordModifications();
	}

	public void setTestClassPath( String javaFile ) {
		String path = javaFile.substring( 0, javaFile.lastIndexOf( "/" ) );
		testClassPath = path.replaceAll( "src/main/java", "src/test/java" ) + "/robot";

		className = javaFile.substring( javaFile.lastIndexOf( "/" ) + 1 ).split( "\\." )[ 0 ];
	}

	public static AstUtils init( String javaFile ) throws Exception {
		AstUtils self = new AstUtils( javaFile );
		return self;
	}
	
	public AstUtils codeRobot( CodeRobot robot ) {
		this.codeRobot = robot;
		return this;
	}

	public AstUtils scan() {
		List<StructuralPropertyDescriptor> clazzPropeties = _rootNode.structuralPropertiesForType();
		for( StructuralPropertyDescriptor clazzPropety : clazzPropeties ) {
			String nodeId = clazzPropety.getId();
			Object nodeValue = _rootNode.getStructuralProperty( clazzPropety );

			if( "package".equals( nodeId ) ) {
				_scan_package( nodeValue );
			}

			if( "imports".equals( nodeId ) ) {
				_scan_imports( nodeValue );
			}

			if( "types".equals( nodeId ) ) {
				_scan_types( nodeValue );
			}
		}

		return this;
	}

	private void _scan_package( Object pkg ) {
		PackageDeclaration pkgDecla = ( PackageDeclaration )pkg;
		AST ast = _testNode.getAST();
		PackageDeclaration testPkgDecla = ast.newPackageDeclaration();
		pkgName = pkgDecla.getName().getFullyQualifiedName();
		String[] pkgEles = ( pkgName + ".robot" ).split( "\\." );
		testPkgDecla.setName( RobotUtil.string2Name( ast, pkgEles ) );
		_testNode.setPackage( testPkgDecla );
	}

	private void _scan_imports( Object imports ) {
		List<ImportDeclaration> _imports = ( List<ImportDeclaration> )imports;
		for( ImportDeclaration _import : _imports ) {

			AST ast = _testNode.getAST();
			ImportDeclaration id = ast.newImportDeclaration();
			id.setName( RobotUtil.string2Name( ast, _import.getName().getFullyQualifiedName().split( "\\." ) ) );
			RobotUtil.addImport( _testNode, id );
		}

	}

	private void _scan_types( Object types ) {
		List<TypeDeclaration> typeDeclas = ( List<TypeDeclaration> )types;
		AST ast = _testNode.getAST();
		for( TypeDeclaration typeDecla : typeDeclas ) {
			LoggerHelper.debug( typeDecla );
			if( Modifier.isPublic( typeDecla.getModifiers() ) ) {
				TypeDeclaration classDec = ast.newTypeDeclaration();
				classDec.setInterface( false );// 设置为非接口

				SimpleName testClassName = ast.newSimpleName( typeDecla.getName() + "Test" );

				// 设置类节点
				classDec.setName( testClassName );// 类名
				classDec.modifiers().add( ast.newModifier( Modifier.ModifierKeyword.PUBLIC_KEYWORD ) );// 类可见性

				// 将类节点连接为编译单元的子节点
				_testNode.types().add( classDec );

				VariableDeclarationFragment fragment = ast.newVariableDeclarationFragment();
				fragment.setName( ast.newSimpleName( className ) );
				FieldDeclaration testTargetField = ast.newFieldDeclaration( fragment );
				SimpleType type = ast.newSimpleType( ast.newSimpleName( className ) );
				testTargetField.setType( type );

				RobotUtil.addAnnotation( _testNode, testTargetField, "Tested", "mockit.Tested" );
				testTargetField.modifiers().add( ast.newModifier( Modifier.ModifierKeyword.PRIVATE_KEYWORD ) );

				ImportDeclaration import_tested = ast.newImportDeclaration();
				import_tested.setName( RobotUtil.string2Name( ast, ( pkgName + "." + className ).split( "\\." ) ) );
				RobotUtil.addImport( _testNode, import_tested );// annotation

				getBody().add( testTargetField );
			}

			FieldDeclaration[] fieldDeclarations = typeDecla.getFields();
			for( FieldDeclaration fieldDeclaration : fieldDeclarations ) {
//				LoggerHelper.debug( fieldDeclaration.getType() );

//				String fieldName = "";
//				List<StructuralPropertyDescriptor> props = fieldDeclaration.structuralPropertiesForType();
//				for( StructuralPropertyDescriptor prop : props ) {
//					if( "fragments".equals( prop.getId() ) ) {
//						List<VariableDeclarationFragment> frags = ( List<VariableDeclarationFragment> )fieldDeclaration.getStructuralProperty( prop );
//						fieldName = frags.get( 0 ).toString();
//					}
//				}

				BodyDeclaration node = (BodyDeclaration)ASTNode.copySubtree( ast, fieldDeclaration );
				RobotUtil.addAnnotation( _testNode, node, "Injectable", "mockit.Injectable" );
				getBody().add( node );
			}

			MethodDeclaration[] methodDeclaras = typeDecla.getMethods();
			for( MethodDeclaration methodDeclara : methodDeclaras ) {
				codeRobot.scanMethod( _testNode, methodDeclara );
			}
		}
	}

	public List<BodyDeclaration> getBody() {
		TypeDeclaration typeDecla = ( TypeDeclaration )_testNode.types().get( 0 ); // 每个测试类只有一个TypeDeclaration
		return typeDecla.bodyDeclarations();
	}

	public void generate() {
		File dir = new File( testClassPath );
		if( !dir.exists() ) {
			dir.mkdirs();
		}

		File testFile = new File( testClassPath + "/" + className + "Test.java" );
		if( testFile.exists() ) {
			testFile.delete();
		}

		try {
			FileUtils.writeStringToFile( testFile, _testNode.toString(), "UTF-8" );
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return _rootNode.toString();
	}
}
