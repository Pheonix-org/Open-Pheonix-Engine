package com.shinkson47.opex.backend.runtime.console.instruction;

import com.shinkson47.opex.backend.resources.pools.KeySupplier;

import java.io.Serializable;

/**
 * <h1>A named instruction attribute.</h1>
 * <br>
 * <p>
 * Responsible for the name of instructions and switches.
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 27/11/2020</a>
 * @version 1
 * @since v1
 */
public interface NamedInstruction extends Serializable {

    /**
     * <h2>The name of this attribute</h2>
     */
    String getName();

    /**
     * <h2>Supplies keys for named instruction attributes</h2>
     * by using thier names as keys.
     */
    KeySupplier<NamedInstruction> NamedInstructionKeySupplier = new KeySupplier<NamedInstruction>(){
        /**
         * {@inheritDoc}
         * Uses an instruction's name as it's key.
         *
         * @param item The instruction to extract a name from.
         * @return A value that may be used as a Pool Key to represent that item.
         */
        @Override
        public String SupplyKey(NamedInstruction item) {
            return item.getName();
        }
    };
}