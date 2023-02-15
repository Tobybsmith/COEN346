import java.util.Comparator;

class PrioritySorter implements Comparator<Task>
{
    @Override
    public int compare(Task t1, Task t2)
    {
        //High priority = good
        return t1.getPriority() - t2.getPriority();
    }
}
