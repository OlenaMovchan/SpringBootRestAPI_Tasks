package com.example.tasknewspring.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    @Test
    void testValidTransitions() {

        assertTrue(TaskStatus.PLANNED.isValidTransition(TaskStatus.WORK_IN_PROGRESS));
        assertTrue(TaskStatus.PLANNED.isValidTransition(TaskStatus.POSTPONED));
        assertTrue(TaskStatus.PLANNED.isValidTransition(TaskStatus.CANCELLED));

        assertTrue(TaskStatus.WORK_IN_PROGRESS.isValidTransition(TaskStatus.NOTIFIED));
        assertTrue(TaskStatus.WORK_IN_PROGRESS.isValidTransition(TaskStatus.SIGNED));
        assertTrue(TaskStatus.WORK_IN_PROGRESS.isValidTransition(TaskStatus.POSTPONED));
        assertTrue(TaskStatus.WORK_IN_PROGRESS.isValidTransition(TaskStatus.CANCELLED));

        assertTrue(TaskStatus.POSTPONED.isValidTransition(TaskStatus.WORK_IN_PROGRESS));
        assertTrue(TaskStatus.POSTPONED.isValidTransition(TaskStatus.NOTIFIED));
        assertTrue(TaskStatus.POSTPONED.isValidTransition(TaskStatus.SIGNED));
        assertTrue(TaskStatus.POSTPONED.isValidTransition(TaskStatus.CANCELLED));

        assertTrue(TaskStatus.NOTIFIED.isValidTransition(TaskStatus.DONE));
        assertTrue(TaskStatus.NOTIFIED.isValidTransition(TaskStatus.SIGNED));
        assertTrue(TaskStatus.NOTIFIED.isValidTransition(TaskStatus.CANCELLED));

        assertTrue(TaskStatus.SIGNED.isValidTransition(TaskStatus.DONE));
        assertTrue(TaskStatus.SIGNED.isValidTransition(TaskStatus.NOTIFIED));
        assertTrue(TaskStatus.SIGNED.isValidTransition(TaskStatus.CANCELLED));

    }

    @Test
    void testInvalidTransitions() {

        assertFalse(TaskStatus.PLANNED.isValidTransition(TaskStatus.NOTIFIED));
        assertFalse(TaskStatus.WORK_IN_PROGRESS.isValidTransition(TaskStatus.DONE));
        assertFalse(TaskStatus.POSTPONED.isValidTransition(TaskStatus.DONE));
        assertFalse(TaskStatus.NOTIFIED.isValidTransition(TaskStatus.WORK_IN_PROGRESS));
        assertFalse(TaskStatus.SIGNED.isValidTransition(TaskStatus.PLANNED));
        assertFalse(TaskStatus.DONE.isValidTransition(TaskStatus.WORK_IN_PROGRESS));
        assertFalse(TaskStatus.CANCELLED.isValidTransition(TaskStatus.NOTIFIED));

    }

}