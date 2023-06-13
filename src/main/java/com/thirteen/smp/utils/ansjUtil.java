package com.thirteen.smp.utils;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.ArrayList;
import java.util.List;

public class ansjUtil {

    public static List<String> startParticiple(String s){
        Result result = ToAnalysis.parse(s);//进行分词，得到Result
        List<Term> terms = result.getTerms();//处理获得分词结果
        List<String> datas = new ArrayList<>();
        for (Term term :terms){
            datas.add(term.getName());//装入String列表中
        }
        return datas;
   }
}
