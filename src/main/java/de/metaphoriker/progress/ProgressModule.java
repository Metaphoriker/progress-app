package de.metaphoriker.progress;

import com.google.inject.AbstractModule;
import de.metaphoriker.progress.model.TaskDao;
import de.metaphoriker.progress.model.TaskRepository;
import de.metaphoriker.progress.model.TaskService;

public class ProgressModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskRepository.class).asEagerSingleton();
        bind(TaskService.class).asEagerSingleton();
        bind(TaskDao.class).asEagerSingleton();
    }
}
