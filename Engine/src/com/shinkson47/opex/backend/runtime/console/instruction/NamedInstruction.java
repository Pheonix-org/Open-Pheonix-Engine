package com.shinkson47.opex.backend.runtime.console.instruction;

import java.io.Serializable;

/**
 * <h1></h1>
 * <br>
 * <p>
 *
 * </p>
 *
 * @author <a href="https://www.shinkson47.in">Jordan T. Gray on 27/11/2020</a>
 * @version 1
 * @since v1
 */
public interface NamedInstruction extends Serializable {
    String getName();
}
