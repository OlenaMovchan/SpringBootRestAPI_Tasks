package com.example.tasknewspring.service.impl;

import com.example.tasknewspring.dto.TaskDto;
import com.example.tasknewspring.dto.TaskResponse;
import com.example.tasknewspring.entity.TaskEntity;
import com.example.tasknewspring.entity.TaskStatus;
import com.example.tasknewspring.entity.UserEntity;
import com.example.tasknewspring.exception.TaskNotFoundException;
import com.example.tasknewspring.exception.UserNotFoundException;
import com.example.tasknewspring.repository.TaskRepository;
import com.example.tasknewspring.repository.UserEntityRepository;
import com.example.tasknewspring.util.LocaleMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private LocaleMessage localeMessage;

    @Mock
    private UserEntityRepository userEntityRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testCreateTask_Success() {

        TaskDto taskDto = new TaskDto();
        taskDto.setDescription("Test task");
        taskDto.setDueDate(LocalDate.now().plusDays(1));
        taskDto.setStatus(TaskStatus.PLANNED);
        taskDto.setUserId(1);

        TaskEntity mockTaskEntity = new TaskEntity();
        mockTaskEntity.setId(1L);
        mockTaskEntity.setDescription("Test task");
        mockTaskEntity.setDueDate(LocalDate.now().plusDays(1));
        mockTaskEntity.setStatus(TaskStatus.PLANNED);

        when(userEntityRepository.findById(1)).thenReturn(Optional.of(new UserEntity()));
        when(taskRepository.save(any(TaskEntity.class))).thenReturn(mockTaskEntity);
        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(mockTaskEntity);
        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);
        TaskDto createdTask = taskService.createTask(taskDto, mock(WebRequest.class));

        assertEquals("Test task", createdTask.getDescription());
        assertEquals(TaskStatus.PLANNED, createdTask.getStatus());

    }

    @Test
    void testCreateTask_Negative_UserNotFound() {
        TaskDto taskDto = new TaskDto();
        taskDto.setUserId(1);
        taskDto.setStatus(TaskStatus.PLANNED);

        when(userEntityRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () -> taskService.createTask(taskDto, mock(WebRequest.class)));

    }

    @Test
    void testUpdateTask_Positive() {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(1L);
        taskEntity.setDescription("todo task1");
        taskEntity.setDueDate(LocalDate.now());
        taskEntity.setStatus(TaskStatus.PLANNED);
        taskEntity.setUserId(1);

        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setDescription(taskEntity.getDescription());
        taskDto.setDueDate(taskEntity.getDueDate());
        taskDto.setStatus(TaskStatus.WORK_IN_PROGRESS);
        taskDto.setUserId(1);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setName("testUser");

        when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
        when(taskRepository.save(Mockito.any())).thenReturn(taskEntity);
        when(userEntityRepository.findByName(Mockito.anyString())).thenReturn(Optional.of(userEntity));
        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(taskEntity);
        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        TaskDto updatedTask = taskService.updateTask(taskDto, 1L);
        System.out.println(updatedTask.getStatus());
        assertEquals(TaskStatus.WORK_IN_PROGRESS, updatedTask.getStatus());

    }

    @Test
    void testUpdateTask_Negative_TaskNotFound() {
        Authentication authentication = mock(Authentication.class);
//        when(authentication.getName()).thenReturn("testUser");
//
//        UserEntity userEntity = new UserEntity();
//        userEntity.setId(1);
//        userEntity.setName("testUser");

        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        TaskDto taskDto = new TaskDto();
        taskDto.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskDto, 1L));
    }

    @Test
    void testGetAllTasks_Positive() {
        int pageNo = 0;
        int pageSize = 10;

        List<TaskEntity> taskEntities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId((long) i);
            taskEntities.add(taskEntity);
        }

        Page<TaskEntity> page = new PageImpl<>(taskEntities);
        when(taskRepository.findAll(PageRequest.of(pageNo, pageSize))).thenReturn(page);

        TaskResponse taskResponse = taskService.getAllTasks(pageNo, pageSize);

        assertNotNull(taskResponse);
        assertEquals(10, taskResponse.getTotalElements());
        assertEquals(1, taskResponse.getTotalPages());

    }

    @Test
    void testGetAllTasks_Negative_NoTasks() {

        int pageNo = 0;
        int pageSize = 10;

        Page<TaskEntity> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(taskRepository.findAll(PageRequest.of(pageNo, pageSize))).thenReturn(page);

        TaskResponse taskResponse = taskService.getAllTasks(pageNo, pageSize);

        assertNotNull(taskResponse);
        assertEquals(0, taskResponse.getTotalElements());
        assertEquals(1, taskResponse.getTotalPages());

    }

    @Test
    void testGetTaskById_Negative_TaskNotFound() {

        Long taskId = 1L;

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    void testGetTasksByUserId_Positive() {
        int userId = 1;
        int pageNo = 0;
        int pageSize = 10;

        List<TaskEntity> taskEntities = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setId((long) i);
            taskEntities.add(taskEntity);
        }

        Page<TaskEntity> page = new PageImpl<>(taskEntities);
        Mockito.when(taskRepository.findTaskEntitiesByUserId(userId, PageRequest.of(pageNo, pageSize))).thenReturn(page);

        TaskResponse taskResponse = taskService.getTasksByUserId(userId, pageNo, pageSize);

        assertNotNull(taskResponse);
        assertEquals(10, taskResponse.getTotalElements());
        assertEquals(1, taskResponse.getTotalPages());
    }

    @Test
    void testGetTasksByUserId_Negative_NoTasks() {
        int userId = 1;
        int pageNo = 0;
        int pageSize = 10;

        Page<TaskEntity> page = new PageImpl<>(Collections.emptyList());
        Mockito.when(taskRepository.findTaskEntitiesByUserId(userId, PageRequest.of(pageNo, pageSize))).thenReturn(page);

        TaskResponse taskResponse = taskService.getTasksByUserId(userId, pageNo, pageSize);

        assertNotNull(taskResponse);
        assertEquals(0, taskResponse.getTotalElements());
        assertEquals(1, taskResponse.getTotalPages());
    }

    @Test
    void testDeleteTask_Positive() {
        Long taskId = 1L;
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).delete(taskEntity);
    }

    @Test
    void testDeleteTask_Negative_TaskNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(taskId));
    }





//
//    @Test
//    void testUpdateTask_Negative_InvalidStateTransition() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.PLANNED);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.SIGNED);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//
//        Assertions.assertThrows(IllegalStateException.class, () -> taskService.updateTask(taskDto, 1L));
//    }
//
//    @Test
//    void testUpdateTask_Negative_InvalidStateTransition_fromWorkInProgressState() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.WORK_IN_PROGRESS);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.DONE);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//
//        Assertions.assertThrows(IllegalStateException.class, () -> taskService.updateTask(taskDto, 1L));
//    }
//
//    @Test
//    void testUpdateTask_Positive_PostponedState() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.PLANNED);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.POSTPONED);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity);
//        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(taskEntity);
//        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);
//
//        TaskDto updatedTask = taskService.updateTask(taskDto, 1L);
//        System.out.println(updatedTask.getStatus());
//        assertEquals(TaskStatus.POSTPONED, updatedTask.getStatus());
//    }
//
//    @Test
//    void testUpdateTask_Positive_NotifiedState() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.WORK_IN_PROGRESS);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.NOTIFIED);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity);
//        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(taskEntity);
//        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);
//
//        TaskDto updatedTask = taskService.updateTask(taskDto, 1L);
//        System.out.println(updatedTask.getStatus());
//        assertEquals(TaskStatus.NOTIFIED, updatedTask.getStatus());
//    }
//
//    @Test
//    void testUpdateTask_Positive_SignedState() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.WORK_IN_PROGRESS);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.SIGNED);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity);
//        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(taskEntity);
//        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);
//
//        TaskDto updatedTask = taskService.updateTask(taskDto, 1L);
//        System.out.println(updatedTask.getStatus());
//        assertEquals(TaskStatus.SIGNED, updatedTask.getStatus());
//    }
//
//    @Test
//    void testUpdateTask_Positive_DoneState() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.NOTIFIED);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.DONE);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity);
//        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(taskEntity);
//        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);
//
//        TaskDto updatedTask = taskService.updateTask(taskDto, 1L);
//        System.out.println(updatedTask.getStatus());
//        assertEquals(TaskStatus.DONE, updatedTask.getStatus());
//    }
//
//    @Test
//    void testUpdateTask_Positive_CanceledState() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.NOTIFIED);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.CANCELLED);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity);
//        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(taskEntity);
//        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);
//
//        TaskDto updatedTask = taskService.updateTask(taskDto, 1L);
//        System.out.println(updatedTask.getStatus());
//        assertEquals(TaskStatus.CANCELLED, updatedTask.getStatus());
//    }
//
//    @Test
//    void testUpdateTask_Positive_CanceledState_fromPostponedState() {
//        TaskEntity taskEntity = new TaskEntity();
//        taskEntity.setId(1L);
//        taskEntity.setStatus(TaskStatus.POSTPONED);
//
//        TaskDto taskDto = new TaskDto();
//        taskDto.setId(1L);
//        taskDto.setStatus(TaskStatus.CANCELLED);
//
//        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(taskEntity));
//        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(taskEntity);
//        when(mapper.map(any(TaskDto.class), eq(TaskEntity.class))).thenReturn(taskEntity);
//        when(mapper.map(any(TaskEntity.class), eq(TaskDto.class))).thenReturn(taskDto);
//
//        TaskDto updatedTask = taskService.updateTask(taskDto, 1L);
//        System.out.println(updatedTask.getStatus());
//        assertEquals(TaskStatus.CANCELLED, updatedTask.getStatus());
//    }


    //PLANNED,
    //    WORK_IN_PROGRESS,
    //    POSTPONED,
    //    NOTIFIED,
    //    SIGNED,
    //    DONE,
    //    CANCELLED;

}