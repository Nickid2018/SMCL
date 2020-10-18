package com.github.nickid2018.smcl.parser;

import java.util.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.optimize.*;

public class CharsInfo {

    private SMCL smcl;

    public PriorityQueue<CharInfo> charQueue = new PriorityQueue<CharInfo>(new Comparator<CharInfo>(){
            @Override
            public int compare(CharInfo p1, CharInfo p2) {                    
                return p1.ch == p2.ch ?p1.position - p2.position: smcl.register.getCharPriority(p1.ch) - smcl.register.getCharPriority(p2.ch);
            }
        });

    public CharsInfo(SMCL smcl) {
        this.smcl = smcl;
    }

    public void putChar(char ch, int position) {
        charQueue.add(new CharInfo(ch, position));
    }
}
