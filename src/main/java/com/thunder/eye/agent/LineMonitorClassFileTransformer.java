package com.thunder.eye.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * LineMonitorClassFileTransformer 类的简要描述
 *
 * @author liyang
 * @since 2025/1/25
 */
public class LineMonitorClassFileTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
        if (className == null) {
            return null;
        }

        // 过滤掉不需要监控的类
        if (!className.startsWith("com/example")) {
            return null;
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor classVisitor = new LineMonitorClassVisitor(classWriter);
        org.objectweb.asm.ClassReader classReader = new org.objectweb.asm.ClassReader(classfileBuffer);
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
}
