package com.fudianchn;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author fudian
 */
public class Test01 {
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> lineDs = env.readTextFile("input/word.txt");
        FlatMapOperator<String, Tuple2<String, Integer>> wordAndOne = lineDs.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] words = s.split(" ");
                for (String word : words) {
                    Tuple2<String, Integer> wordTuple2 = Tuple2.of(word, 1);
                    collector.collect(wordTuple2);
                }
            }
        });
        UnsortedGrouping<Tuple2<String, Integer>> wordAndOneGroupBy = wordAndOne.groupBy(0);
        AggregateOperator<Tuple2<String, Integer>> sum = wordAndOneGroupBy.sum(1);
        sum.print();
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime + "毫秒");
    }
}