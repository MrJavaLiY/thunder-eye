package com.thunder.eye.agent;

import com.alibaba.excel.support.asm.Opcodes;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * LineMonitorClassVisitor 类的简要描述
 *
 * @author liyang
 * @since 2025/1/25
 */
public class LineMonitorClassVisitor  extends ClassVisitor {
    public LineMonitorClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM9, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        return new LineMonitorMethodVisitor(methodVisitor, access, name, descriptor);
    }
}
