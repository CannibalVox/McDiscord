package net.technicpack.mcdiscord.coremod.asm;

import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.tree.*;

public class ClickableHudEditor implements IAsmEditor {

    private boolean obfuscated;

    public ClickableHudEditor(boolean obfuscated) {
        this.obfuscated = obfuscated;
    }

    @Override
    public void edit(MethodNode method) {
        AbstractInsnNode firstInstruction = method.instructions.getFirst();
        method.instructions.insertBefore(firstInstruction, new VarInsnNode(ILOAD, 1));
        method.instructions.insertBefore(firstInstruction, new VarInsnNode(ILOAD, 2));
        method.instructions.insertBefore(firstInstruction, new VarInsnNode(ILOAD, 3));
        method.instructions.insertBefore(firstInstruction, new MethodInsnNode(INVOKESTATIC, "net/technicpack/mcdiscord/event/HudHandler", "handleMouseClick", "(III)Z", false));

        LabelNode returnToMethod = new LabelNode();
        method.instructions.insertBefore(firstInstruction, new JumpInsnNode(IFEQ, returnToMethod));
        method.instructions.insertBefore(firstInstruction, new InsnNode(RETURN));
        method.instructions.insertBefore(firstInstruction, returnToMethod);
    }

    @Override
    public String getClassName() {
        return "net.minecraft.client.gui.GuiChat";
    }

    @Override
    public String getMethodName() {
        return getCorrectSymbol("mouseClicked", "func_73864_a");
    }

    @Override
    public String getMethodDesc() {
        return "(III)V";
    }

    private String getCorrectSymbol(String deobfuscated, String obfuscated) {
        return this.obfuscated?obfuscated:deobfuscated;
    }
}
