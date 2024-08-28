package com.onesource.demo.util;


import java.util.List;
import java.util.Objects;

public class Delegation {
    private final String le1;
    private final String le2;
    private final String delegateTo;

    private final List<String> authorizations;

    public Delegation(String left, String right, String delegateTo, List<String> authorizations) {
        this.le1 = left;
        this.le2 = right;
        this.delegateTo = delegateTo;
        this.authorizations = authorizations;
    }
    public String toString() {
        return "(" + le1 + ", " + le2 + "): " + authorizations.toString();
    }

    public String getLe1() {
        return le1;
    }

    public String getLe2() {
        return le2;
    }

    public String getDelegateTo() {
        return delegateTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delegation)) return false;
        Delegation otherDelegation = (Delegation) o;
        return (Objects.equals(le1, otherDelegation.le1) || Objects.equals(le1, otherDelegation.le2))
                && (Objects.equals(le2, otherDelegation.le1) || Objects.equals(le2, otherDelegation.le2))
                && (Objects.equals(delegateTo, otherDelegation.delegateTo));
    }

    @Override
    public int hashCode() {
        return Objects.hash(le1, le2, delegateTo);
    }
}
