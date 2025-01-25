package com.thunder.eye.agent;

import com.alibaba.excel.support.asm.Opcodes;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/**
 * TimerMethodVisitor 类的简要描述
 *
 * @author liyang
 * @since 2025/1/25
 */
public class TimerMethodVisitor extends MethodVisitor {
    private final String methodName;

    public TimerMethodVisitor(MethodVisitor mv, int access, String name, String descriptor) {
        super(Opcodes.ASM9, mv);
        this.methodName = name;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn("Entering method: " + methodName);
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("Exiting method: " + methodName);
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitInsn(opcode);
    }
}
