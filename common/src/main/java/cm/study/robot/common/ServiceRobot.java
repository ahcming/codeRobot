package cm.study.robot.common;

import java.lang.Thread.State;
import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.internal.runtime.Log;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ServiceRobot implements CodeRobot {

	@Override
	public void scanMethod( CompilationUnit testNode, MethodDeclaration methodDeclara ) {
		if( !Modifier.isPublic( methodDeclara.getModifiers() ) ) { // 只对public方法生成测试方法
			LoggerHelper.debug( methodDeclara );
			return;
		} else if( methodDeclara.isConstructor() ) { // 构造方法不测试
			return;
		}

		// List<StructuralPropertyDescriptor> propDescs =
		// methodDeclara.structuralPropertiesForType();
		// for(StructuralPropertyDescriptor propDesc : propDescs) {
		// LoggerHelper.debug( methodDeclara, propDesc );
		// if( propDesc.getId().equals( "javadoc" )) {
		// Javadoc javadoc = ( Javadoc )methodDeclara.getStructuralProperty(
		// propDesc );
		// LoggerHelper.debug( javadoc );
		// List tags = javadoc.tags();
		// for(Object tag : tags) {
		// LoggerHelper.debug( tag );
		// }
		// }
		// }

		// 把所有分支语句化为顺序语句
		Map<String, List<Statement>> allBranches = _convertBlock( methodDeclara.getBody().statements(), "" );

		String methodName = methodDeclara.getName().getIdentifier();

		for( Entry<String, List<Statement>> sequenceStat : allBranches.entrySet() ) {
			List<Statement> statements = sequenceStat.getValue();
			if( null == statements ) {
				continue;
			}
			generate( testNode, methodName, sequenceStat.getKey(), statements );
		}

	}

	public void generate( CompilationUnit testNode, String methodName, String suffix, List<Statement> statements ) {
		AST ast = testNode.getAST();
		MethodDeclaration methodDecla = ast.newMethodDeclaration();
		RobotUtil.addAnnotation( testNode, methodDecla, "Test", "org.testng.annotations.Test" ); // 增加方法注解

		methodDecla.modifiers().add( ast.newModifier( Modifier.ModifierKeyword.PUBLIC_KEYWORD ) ); // public

		// test method name
		methodDecla.setName( ast.newSimpleName( methodName + "_$" + suffix ) );

		methodDecla.setReturnType2( ast.newPrimitiveType( PrimitiveType.VOID ) ); // return

		SimpleName exceptionName = ast.newSimpleName( "Exception" );
		methodDecla.thrownExceptions().add( exceptionName ); // 抛出异常

		methodDecla.setBody( _scan_method_block( testNode, statements ) );

		Javadoc docComment = ast.newJavadoc();
		for( Statement statement : statements ) {
			TagElement te = ast.newTagElement();
			te.setTagName( statement.toString().replace( "\n", "" ) );
			docComment.tags().add( te );
		}
		methodDecla.setJavadoc( docComment );

		RobotUtil.getBody( testNode ).add( methodDecla );
	}

	private Block _scan_method_block( CompilationUnit testNode, List<Statement> statements ) {
		AST ast = testNode.getAST();
		Block methodBody = ast.newBlock();

		for( Statement stat : statements ) {
			// LoggerHelper.debug( stat ); //

		}
		return methodBody;
	}

	/**
	 * 把所有分支按分支拆分
	 */
	private Map<String, List<Statement>> _convertBlock( List<Statement> statements, String prefix ) {
		Map<String, List<Statement>> allBranchesStatements = new HashMap<String, List<Statement>>();

		for( Statement statement : statements ) {
			if( statement instanceof IfStatement ) {
				IfStatement ifStat = ( IfStatement )statement;
				Map<String, Map<String, List<Statement>>> allIfBranches = traversalIf( ifStat, prefix );

				if( allBranchesStatements.isEmpty() ) {
					for( Entry<String, Map<String, List<Statement>>> entry : allIfBranches.entrySet() ) {
						allBranchesStatements.putAll( entry.getValue() );
					}
				} else {
					for( Entry<String, List<Statement>> branch : allBranchesStatements.entrySet() ) {
						String branchName = branch.getKey();
						List<Statement> branchStats = branch.getValue();
						if( null == branchStats ) {
							continue;
						}

						for( Entry<String, Map<String, List<Statement>>> entry : allIfBranches.entrySet() ) {
							allBranchesStatements.putAll( meger( "$".equals( branchName ) ? entry.getKey() : ( entry.getKey() + branchName ), branchStats,
									entry.getValue() ) );
						}

						allBranchesStatements.put( branchName, null );
					}
				}
			} else {
				if( allBranchesStatements.isEmpty() ) {
					List<Statement> stats = new ArrayList<Statement>();
					stats.add( statement );
					allBranchesStatements.put( "$", stats );
				} else {
					for( Entry<String, List<Statement>> branch : allBranchesStatements.entrySet() ) {
						List<Statement> statements2 = branch.getValue();
						if( null == statements2 ) {
							continue;
						}
						statements2.add( statement );
					}
				}
			}
		}

		return allBranchesStatements;
	}

	public Map<String, Map<String, List<Statement>>> traversalIf( IfStatement ifStat, String prefix ) {
		Map<String, Map<String, List<Statement>>> result = new HashMap<String, Map<String, List<Statement>>>();
		Block thenStat = ( Block )( ifStat.getThenStatement() );
		Map<String, List<Statement>> subThenBranches = _convertBlock( thenStat.statements(), "I" );
		result.put( prefix + "I", subThenBranches );

		Statement elseStat = ifStat.getElseStatement();
		if( elseStat instanceof IfStatement ) {
			Map<String, Map<String, List<Statement>>> elseStatTraversal = traversalIf( ( IfStatement )elseStat, "E" );
			result.putAll( elseStatTraversal );
		} else {
			Block elseStatBlock = ( Block )elseStat;
			Map<String, List<Statement>> subElseBranches = _convertBlock( elseStatBlock.statements(), "E" );
			result.put( prefix + "E", subElseBranches );
		}

		return result;
	}

	public Map<String, List<Statement>> meger( String prefix, List<Statement> statements, Map<String, List<Statement>> branchStatements ) {
		Map<String, List<Statement>> result = new HashMap<String, List<Statement>>();
		if( branchStatements.size() == 1 ) {
			List<Statement> newStats = new ArrayList<Statement>( statements );
			newStats.addAll( branchStatements.get( "$" ) );
			result.put( prefix, newStats );
			return result;
		}

		for( Entry<String, List<Statement>> entry : branchStatements.entrySet() ) {
			List<Statement> statements2 = entry.getValue();
			if( null == statements2 ) {
				continue;
			}
			List<Statement> stats = new ArrayList<Statement>( statements );
			stats.addAll( statements2 );
			result.put( entry.getKey(), stats );
		}

		return result;
	}

}
