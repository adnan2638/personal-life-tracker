package com.adnan.personallifetracker.feature.tasks.domain

import com.adnan.personallifetracker.feature.tasks.domain.model.CompletionTiming
import com.adnan.personallifetracker.feature.tasks.domain.model.CreateTaskCommand
import com.adnan.personallifetracker.feature.tasks.domain.model.Task
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskCategory
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskStatus
import com.adnan.personallifetracker.feature.tasks.domain.model.TaskPriority
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskCategoryRepository
import com.adnan.personallifetracker.feature.tasks.domain.repository.TaskRepository
import com.adnan.personallifetracker.feature.tasks.domain.usecase.ArchiveTaskCategoryUseCase
import com.adnan.personallifetracker.feature.tasks.domain.usecase.ArchiveTaskUseCase
import com.adnan.personallifetracker.feature.tasks.domain.usecase.CompleteTaskUseCase
import com.adnan.personallifetracker.feature.tasks.domain.usecase.CreateTaskCategoryUseCase
import com.adnan.personallifetracker.feature.tasks.domain.usecase.CreateTaskUseCase
import com.adnan.personallifetracker.feature.tasks.domain.usecase.MarkTaskPartialUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Test

class TaskUseCasesTest {
    private val repository = InMemoryTaskRepository()
    private val categories = InMemoryCategoryRepository()

    @Test fun creates_valid_task() = runTest {
        val task = CreateTaskUseCase(repository, now = { 100 })(CreateTaskCommand(title = "Study", date = "2026-09-02"))
        assertEquals("Study", task.title); assertEquals(TaskStatus.PLANNED, task.status); assertEquals(TaskPriority.MEDIUM, task.priority); assertNull(task.completedAt)
    }

    @Test fun rejects_blank_title() = runTest {
        assertIllegalArgument { CreateTaskUseCase(repository)(CreateTaskCommand(title = "  ", date = "2026-09-02")) }
    }

    @Test fun task_without_deadline_is_valid() = runTest {
        val task = CreateTaskUseCase(repository)(CreateTaskCommand(title = "Read", date = "2026-09-02"))
        assertNull(task.deadlineAt)
    }

    @Test fun valid_start_and_deadline_are_accepted() = runTest {
        val task = CreateTaskUseCase(repository)(CreateTaskCommand(title = "Read", date = "2026-09-02", startAt = 10, deadlineAt = 20))
        assertEquals(10L, task.startAt); assertEquals(20L, task.deadlineAt)
    }

    @Test fun invalid_start_and_deadline_are_rejected() {
        org.junit.Assert.assertThrows(IllegalArgumentException::class.java) { TaskValidation.validateDetails("Read", 21, 20, null) }
    }

    @Test fun invalid_completion_state_and_timing_combination_is_rejected() {
        org.junit.Assert.assertThrows(IllegalArgumentException::class.java) { TaskValidation.validateCompletion(TaskStatus.PLANNED, CompletionTiming.ON_TIME, 10) }
    }

    @Test fun inactive_category_reference_is_rejected() = runTest {
        repository.categoriesAreActive = false
        assertIllegalArgument { CreateTaskUseCase(repository)(CreateTaskCommand(title = "Read", categoryId = "missing", date = "2026-09-02")) }
    }

    @Test fun completing_by_deadline_is_on_time() = runTest {
        val task = CreateTaskUseCase(repository, now = { 10 })(CreateTaskCommand(title = "Read", date = "2026-09-02", deadlineAt = 20))
        val completed = CompleteTaskUseCase(repository, now = { 20 })(task.id)
        assertEquals(TaskStatus.COMPLETED, completed.status); assertEquals(CompletionTiming.ON_TIME, completed.completionTiming)
    }

    @Test fun completing_after_deadline_is_late() = runTest {
        val task = CreateTaskUseCase(repository, now = { 10 })(CreateTaskCommand(title = "Read", date = "2026-09-02", deadlineAt = 20))
        val completed = CompleteTaskUseCase(repository, now = { 21 })(task.id)
        assertEquals(CompletionTiming.LATE, completed.completionTiming)
    }

    @Test fun partial_task_has_no_completion_timing() = runTest {
        val task = CreateTaskUseCase(repository)(CreateTaskCommand(title = "Read", date = "2026-09-02"))
        val partial = MarkTaskPartialUseCase(repository, now = { 30 })(task.id)
        assertEquals(TaskStatus.PARTIAL, partial.status); assertNull(partial.completionTiming); assertNull(partial.completedAt)
    }

    @Test fun category_creation_and_active_duplicate_rejection() = runTest {
        val create = CreateTaskCategoryUseCase(categories, now = { 1 })
        create("Academic")
        assertIllegalArgument { create(" academic ") }
    }

    @Test fun archiving_preserves_task_and_category_history() = runTest {
        val task = CreateTaskUseCase(repository)(CreateTaskCommand(title = "Read", date = "2026-09-02"))
        ArchiveTaskUseCase(repository, now = { 40 })(task.id)
        assertEquals(40L, repository.findTask(task.id)?.deletedAt)
        val category = CreateTaskCategoryUseCase(categories)("Health")
        ArchiveTaskCategoryUseCase(categories, now = { 50 })(category.id)
        assertEquals(50L, categories.findCategory(category.id)?.archivedAt)
    }
}

private suspend fun assertIllegalArgument(block: suspend () -> Unit) {
    try {
        block()
        fail("Expected IllegalArgumentException")
    } catch (_: IllegalArgumentException) {
        // Expected validation result.
    }
}

private class InMemoryTaskRepository : TaskRepository {
    private val tasks = linkedMapOf<String, Task>()
    private val state = MutableStateFlow<List<Task>>(emptyList())
    var categoriesAreActive = true
    override fun observeActiveTasks(): Flow<List<Task>> = state
    override suspend fun findTask(id: String): Task? = tasks[id]
    override suspend fun create(task: Task) { tasks[task.id] = task; publish() }
    override suspend fun update(task: Task) { tasks[task.id] = task; publish() }
    override suspend fun archive(id: String, archivedAt: Long) { tasks[id]?.let { tasks[id] = it.copy(deletedAt = archivedAt, updatedAt = archivedAt) }; publish() }
    override suspend fun isActiveCategory(id: String): Boolean = categoriesAreActive
    private fun publish() { state.value = tasks.values.filter { it.deletedAt == null } }
}

private class InMemoryCategoryRepository : TaskCategoryRepository {
    private val categories = linkedMapOf<String, TaskCategory>()
    private val state = MutableStateFlow<List<TaskCategory>>(emptyList())
    override fun observeActiveCategories(): Flow<List<TaskCategory>> = state
    override suspend fun findCategory(id: String): TaskCategory? = categories[id]
    override suspend fun create(category: TaskCategory) {
        require(categories.values.none { it.archivedAt == null && it.name.equals(category.name, ignoreCase = true) }) { "An active category with this name already exists." }
        categories[category.id] = category; publish()
    }
    override suspend fun update(category: TaskCategory) { categories[category.id] = category; publish() }
    override suspend fun archive(id: String, archivedAt: Long) { categories[id]?.let { categories[id] = it.copy(archivedAt = archivedAt, updatedAt = archivedAt) }; publish() }
    private fun publish() { state.value = categories.values.filter { it.archivedAt == null } }
}
