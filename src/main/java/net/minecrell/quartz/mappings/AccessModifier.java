/*
 * QuartzMappings
 * Copyright (c) 2015, Minecrell <https://github.com/Minecrell>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.minecrell.quartz.mappings;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_PROTECTED;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public enum AccessModifier {

    PRIVATE (ACC_PRIVATE),
    PACKAGE_LOCAL (0),
    PROTECTED (ACC_PROTECTED),
    PUBLIC (ACC_PUBLIC);

    private static final AccessModifier[] modifiers = values();
    private final int modifier;

    private AccessModifier(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }

    public boolean is(int access) {
        return (access & modifier) != 0;
    }

    public int transform(int access) {
        AccessModifier current = AccessModifier.of(access);
        if (this != current) {
            // Don't lower access
            if (current.compareTo(this) < 0) {
                access &= ~current.modifier;
                access |= modifier;
            }
        }

        return access;
    }

    public static AccessModifier of(int access) {
        for (AccessModifier modifier : modifiers) {
            if (modifier.is(access)) {
                return modifier;
            }
        }

        throw new AssertionError();
    }

    public static boolean isFinal(int access) {
        return (access & ACC_FINAL) != 0;
    }

    public static int removeFinal(int access) {
        if (isFinal(access)) {
            access &= ~ACC_FINAL;
        }

        return access;
    }

}
