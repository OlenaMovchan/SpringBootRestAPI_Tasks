package com.example.tasknewspring.entity;

import java.util.HashMap;
import java.util.Map;

public enum TaskStatus {
    PLANNED,
    WORK_IN_PROGRESS,
    POSTPONED,
    NOTIFIED,
    SIGNED,
    DONE,
    CANCELLED;
    private static final Map<TaskStatus, TaskStatus[]> validTransitions = new HashMap<>();
    static {
        validTransitions.put(PLANNED, new TaskStatus[] {WORK_IN_PROGRESS, POSTPONED, CANCELLED});
        validTransitions.put(WORK_IN_PROGRESS, new TaskStatus[] {NOTIFIED, SIGNED, POSTPONED, CANCELLED});
        validTransitions.put(POSTPONED, new TaskStatus[] {WORK_IN_PROGRESS, NOTIFIED, SIGNED, CANCELLED});
        validTransitions.put(NOTIFIED, new TaskStatus[] {DONE, SIGNED, CANCELLED});
        validTransitions.put(SIGNED, new TaskStatus[] {DONE, NOTIFIED, CANCELLED});
        validTransitions.put(DONE, new TaskStatus[] {});
        validTransitions.put(CANCELLED, new TaskStatus[] {});
    }


    public boolean isValidTransition(TaskStatus nextState) {
        TaskStatus[] possibleNextStates = validTransitions.get(this);
        if (possibleNextStates != null) {
            for (TaskStatus state : possibleNextStates) {
                if (state == nextState) {
                    return true;
                }
            }
        }
        return false;
    }
}
