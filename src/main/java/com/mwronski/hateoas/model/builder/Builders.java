package com.mwronski.hateoas.model.builder;

/**
 * Class holds all model builders
 *
 * @author Michal Wronski
 * @date 27-05-2014
 */
public final class Builders {

    public static MessageBuilder message() {
        return new MessageBuilder();
    }

    private Builders() {
        //no instances
    }
}
