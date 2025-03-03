/**
 * Interface para elementos visitáveis
 */
package trabalhofinal.smarthome.visitor;
//package com.smarthome.visitor;

public interface Visitable {
    String accept(HomeVisitor visitor);
}
