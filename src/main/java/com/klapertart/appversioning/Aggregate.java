package com.klapertart.appversioning;

import lombok.extern.slf4j.Slf4j;

/**
 * @author tritr
 * @since 5/19/2024
 */

@Slf4j
public class Aggregate {

    public int sum(int val1, int val2){
        int result = val1 + val2;
        log.info("result: {}", result);
        return result;
    }

    public int multiple(int val1, int val2){
        log.info("result: {}", (val1*val2));
        return (val1*val2);
    }

    public int subtraction(int val1, int val2){
        log.info("result: {}", (val1-val2));
        return (val1-val2);
    }

    // add fitur 1

}
