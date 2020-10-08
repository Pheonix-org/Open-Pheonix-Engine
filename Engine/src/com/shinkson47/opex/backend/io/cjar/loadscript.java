package com.shinkson47.opex.backend.io.cjar;

import java.io.Serializable;

public interface loadscript extends Serializable {
    public void preload();
    public void load();
    public void postload();
}
