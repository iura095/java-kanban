package com.bistricaIurie.TaskTracker.model;

public enum Endpoint {
    GET_TASKS,
    GET_TASKBYID,
    GET_SUBTASKS,
    GET_SUBTASKBYID,
    GET_EPICS,
    GET_EPICBYID,
    GET_EPICSUBTASKS,
    GET_HISTORY,
    GET_PRIORITIZED_TASKS,
    ADD_TASK,
    ADD_SUBTASK,
    ADD_EPIC,
    DELETE_TASK,
    DELETE_SUBTASK,
    DELETE_EPIC,
    UNKNOWN;
}
