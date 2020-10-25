package com.github.nickid2018.smcl.parser;

import java.util.*;
import java.util.function.*;
import com.github.nickid2018.smcl.*;
import com.github.nickid2018.smcl.util.*;

public class CharsInfo {

	public SMCL smcl;

	public PriorityQueue<CharInfo> charQueue = new PriorityQueue<CharInfo>(new Comparator<CharInfo>() {
            @Override
            public int compare(CharInfo p1, CharInfo p2) {
                return p1.ch == p2.ch ? p1.position - p2.position
					: smcl.register.getCharPriority(p1.ch) - smcl.register.getCharPriority(p2.ch);
            }
        });

	public List<CharInfo> infos = new ArrayList<>();

	public CharsInfo(SMCL smcl) {
		this.smcl = smcl;
	}

	public void putChar(char ch, int position) {
		CharInfo info = new CharInfo(ch, position);
		charQueue.add(info);
		infos.add(info);
	}

	// 2*log2(n)+n
	public CharsInfo subInfo(final int start, final int end) {
		int from =Math.max(CollectionUtils.binarySearch(infos, new ToIntFunction<CharInfo>() {
                                   @Override
                                   public int applyAsInt(CharInfo value) {
                                       return start - value.position;
                                   }
                               }), 0);
		int to = Math.min(Math.max(CollectionUtils.binarySearch(infos, new ToIntFunction<CharInfo>() {
                                           @Override
                                           public int applyAsInt(CharInfo value) {
                                               return end - value.position;
                                           }
                                       }), 0), infos.size());
		CharsInfo newInfo = new CharsInfo(smcl);
		newInfo.infos = infos.subList(from, to);
		newInfo.charQueue.addAll(newInfo.infos);
		return newInfo;
	}
}
