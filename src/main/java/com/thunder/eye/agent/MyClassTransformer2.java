package com.thunder.eye.agent;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

/**
 * MyClassTransformer2 类的简要描述
 *
 * @author liyang
 * @since 2025/1/25
 */
public class MyClassTransformer2 {
    public byte[] transform(byte[] classfileBuffer) {
        ClassReader cr = new ClassReader(classfileBuffer);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        for (MethodNode mn : cn.methods) {
            if (mn.name.equals("myMethod")) {
                // 修改方法体
                mn.instructions.insert(mn.instructions.getFirst(), new FieldInsnNode(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;"));
                mn.instructions.insert(mn.instructions.getLast(), new LdcInsnNode("Entering myMethod"));
                mn.instructions.insert(mn.instructions.getLast(), new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false));
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cn.accept(cw);
        return cw.toByteArray();
    }
}
