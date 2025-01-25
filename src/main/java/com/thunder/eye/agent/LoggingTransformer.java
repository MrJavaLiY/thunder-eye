package com.thunder.eye.agent;

import com.alibaba.excel.support.asm.Opcodes;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;

/**
 * LoggingTransformer 类的简要描述
 *
 * @author liyang
 * @since 2025/1/25
 */
public class LoggingTransformer extends ClassVisitor {
    public LoggingTransformer(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        return new LoggingMethodVisitor(mv, access, name, descriptor);
    }
}
