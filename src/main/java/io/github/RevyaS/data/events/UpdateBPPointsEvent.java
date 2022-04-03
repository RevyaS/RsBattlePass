package io.github.RevyaS.data.events;

import io.github.RevyaS.data.containers.QuestData;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.impl.AbstractEvent;

public class UpdateBPPointsEvent extends AbstractEvent {

    private int points;
    private Cause cause;

    public UpdateBPPointsEvent(Cause cause, int points)
    {
        this.cause = cause;
        this.points = points;
    }

    public int getPoints() {return points;}

    @Override
    public Cause getCause() {
        return null;
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }

    @Override
    public EventContext getContext() {
        return super.getContext();
    }
}
