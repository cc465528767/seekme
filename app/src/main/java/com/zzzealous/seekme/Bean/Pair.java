package com.zzzealous.seekme.Bean;

/**
 * Created by zzzealous on 2018/3/4.
 */


/**
 *
 * 对应存储实体
 * @param <E>
 * @param <F>
 */
public class Pair <E extends Object, F extends Object> {
    private E first;
    private F second;

    public Pair(){
    }

    public E getFirst() {
        return first;
    }
    public void setFirst(E first) {
        this.first = first;
    }
    public F getSecond() {
        return second;
    }
    public void setSecond(F second) {
        this.second = second;
    }


}
