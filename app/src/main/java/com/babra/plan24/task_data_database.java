package com.babra.plan24;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {task_data.class}, version = 1)
public abstract class task_data_database extends RoomDatabase {
        public abstract task_data_dao task_data_dao();

}
