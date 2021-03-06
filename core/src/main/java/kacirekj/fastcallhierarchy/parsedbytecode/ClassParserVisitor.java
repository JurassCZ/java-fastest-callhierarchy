package kacirekj.fastcallhierarchy.parsedbytecode;

import kacirekj.fastcallhierarchy.parsedbytecode.clazz.Clazz;
import kacirekj.fastcallhierarchy.parsedbytecode.clazz.Instruction;
import kacirekj.fastcallhierarchy.parsedbytecode.clazz.Method;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * Visitor-pattern class for Java Agent.
 */
public class ClassParserVisitor extends ClassVisitor {
	private boolean resultReady = false;

	private Clazz parsingClass;
	private Method parsingMethod;

	private final CompleteMethodVisitor completeMethodVisitor = new CompleteMethodVisitor(Opcodes.ASM5);

	public ClassParserVisitor() {
		super(Opcodes.ASM5);
	}

	// Step 1 - visit Class A
	@Override
	public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
		this.resultReady = false;
		parsingClass = new Clazz(name, superName, interfaces);
	}

	// Step 2 - for each method M of Class A
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		parsingMethod = new Method(name, desc, parsingClass);
		parsingClass.getMethods().add(parsingMethod);
		return completeMethodVisitor;
	}

	class CompleteMethodVisitor extends MethodVisitor {
		Instruction parsingInstruction;

		public CompleteMethodVisitor(int api) {	super(api);	}

		// Step 3 - for each instruction of method M in Class A
		@Override
		public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
			parsingInstruction = new Instruction(name, desc, owner, parsingMethod);
			parsingMethod.getInstructions().add(parsingInstruction);
		}
	}

	// Step 4 - class A visit finished
	@Override
	public void visitEnd() {
		resultReady = true;
	}

	public Clazz getParsedClassIfReady() {
		return resultReady ? this.parsingClass : null;
	}
}
