package org.ggp.base.util.statemachine;

import java.io.Serializable;

import org.ggp.base.util.gdl.grammar.GdlTerm;

/**
 * A Move represents a possible move that can be made by a role. Each
 * player makes exactly one move on every turn. This includes moves
 * that represent passing, or taking no action.
 * <p>
 * Note that Move objects are not intrinsically tied to a role. They
 * only express the action itself.
 */
@SuppressWarnings("serial")
public class Move implements Serializable
{
    protected final GdlTerm contents;

    public Move(GdlTerm contents)
    {
        this.contents = contents;
    }

    @Override
    public boolean equals(Object o)
    {
        if ((o != null) && (o instanceof Move)) {
            Move move = (Move) o;
            return move.contents.equals(contents);
        }

        return false;
    }

    public GdlTerm getContents()
    {
        return contents;
    }

    @Override
    public int hashCode()
    {
        return contents.hashCode();
    }

    @Override
    public String toString()
    {
        return contents.toString();
    }
}