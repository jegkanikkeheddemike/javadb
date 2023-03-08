package tables;

import data.TableData;
import data.TableID;

public class Task extends TableData<Task> {
    public TableID<User> assigned;
    public Task(TableID<User> assigned) {
        this.assigned = assigned;
    }
}
