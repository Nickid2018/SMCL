package com.github.nickid2018.smcl.parser;

import java.util.*;
import com.github.nickid2018.smcl.*;

public abstract class AbstractParser <T extends Statement> {

    public abstract T parseStatement(CharArrayString expr, CharsInfo chars, LevelInfo level, int priority,Queue<StructEntry> structs);
    
    public static Statement nextStatement(CharArrayString str,int start,int end,CharsInfo info,Queue<StructEntry> structs,SMCL smcl){
        if(str.string[start]==smcl.settings.overrideCharacter){
            return structs.poll().formatted;
        }
        return null;
    }
}
