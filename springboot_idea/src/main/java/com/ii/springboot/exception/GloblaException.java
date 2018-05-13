package com.ii.springboot.exception;

import com.ii.springboot.result.CodeMsg;

public class GloblaException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    private CodeMsg cm;
    public GloblaException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
