package com.example.autoboard.helpers;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionTypeTest {

    @Test
    void testEnumValues() {
        // Verify the enum contains all expected constants
        ActionType[] expectedValues = {
                ActionType.CREATE_TASK,
                ActionType.UPDATE_TASK,
                ActionType.DELETE_TASK,
                ActionType.ASSIGN_TASK,
                ActionType.COMPLETE_TASK
        };
        assertArrayEquals(expectedValues, ActionType.values());
    }

    @Test
    void testEnumNames() {
        // Verify the name of each enum constant
        assertEquals("CREATE_TASK", ActionType.CREATE_TASK.name());
        assertEquals("UPDATE_TASK", ActionType.UPDATE_TASK.name());
        assertEquals("DELETE_TASK", ActionType.DELETE_TASK.name());
        assertEquals("ASSIGN_TASK", ActionType.ASSIGN_TASK.name());
        assertEquals("COMPLETE_TASK", ActionType.COMPLETE_TASK.name());
    }

    @Test
    void testEnumOrdinals() {
        // Verify the ordinal values of each enum constant
        assertEquals(0, ActionType.CREATE_TASK.ordinal());
        assertEquals(1, ActionType.UPDATE_TASK.ordinal());
        assertEquals(2, ActionType.DELETE_TASK.ordinal());
        assertEquals(3, ActionType.ASSIGN_TASK.ordinal());
        assertEquals(4, ActionType.COMPLETE_TASK.ordinal());
    }
}