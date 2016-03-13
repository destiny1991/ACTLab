package cn.edu.buaa.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Try {
    public static List<Integer> findSubstring(String s, String[] words) {
		List<Integer> res = new ArrayList<>();
		if (words == null || words.length == 0)
			return res;

		Map<String, Integer> wordsMap = new HashMap<>();
		for (int i = 0; i < words.length; i++) {
			String key = words[i];
			Integer value = wordsMap.get(key);
			if(value != null) {
				wordsMap.put(key, value + 1);
			} else {
				wordsMap.put(key, 1);
			}
		}
		
		int wLen = words[0].length();
		for(int i = 0; i < wLen; i++) {
			int cnt = 0;
			int left = i;
			Map<String, Integer> winMap = new HashMap<>();
			for(int j = i; j <= s.length() - wLen; j += wLen) {
				String str = s.substring(j, j + wLen);
				if(wordsMap.containsKey(str)) {
					if(winMap.containsKey(str) ) {
						winMap.put(str, winMap.get(str) + 1);
					} else {
						winMap.put(str, 1);
					}
					
					cnt++;
					while(winMap.get(str) > wordsMap.get(str)) {
						String t = s.substring(left, left + wLen);
						winMap.put(t, winMap.get(t) - 1);
						cnt--;
						left = left + wLen;
					}
					
					if(cnt == words.length) {
						res.add(left);
					}
				} else {
					winMap.clear();
					cnt = 0;
					left = j + wLen;
				}
			}
		}
		
		return res;
	}
	
    
    public boolean comp(List<Integer> a, List<Integer> b) {
    	if(a.size() != b.size()) return false;
    	for(int i = 0; i < a.size(); i++) {
    		if(a.get(i) != b.get(i)) return false;
    	}  	
    	return true;
    }
    
    public List<List<Integer>> deduplication(List<List<Integer>> res) {
        for(int i = 0; i < res.size(); i++) {
            for(int j = i + 1; j < res.size(); j++) {
            	if(comp(res.get(i), res.get(j))) {
            		res.remove(j);
            		j--;
            	}
            }
        }
        return res;
    }
    
    public boolean isMatch(String s, String p) {
        if(p.length() == 0) return (s.length() == 0);
        if(s.length() == 0 && p.length() == 1 && p.charAt(0) == '*') return true;

        if(s.charAt(0) == p.charAt(0) || p.charAt(0) == '?') {
            return isMatch(s.substring(1), p.substring(1));
        } else if(p.charAt(0) == '*') {
            while(s.length() > 0 && p.charAt(0) == '*') {
                if(isMatch(s, p.substring(1))) return true;
                s = s.substring(1);
            }
            return isMatch(s, p.substring(1));
        } else return false;
    }
    
	public static void main(String[] args) {
		String string = "wordgoodgoodgoodbestword";
		String[] words = {"word","good","best","good"};
		
		List<Integer> res = findSubstring(string, words);
		System.out.println(res);
	}
}
