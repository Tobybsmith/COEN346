import java.util.Comparator;

class TimeSorter implements Comparator<Task>
{
    @Override
    public int compare(Task t1, Task t2)
    {
        return t1.getBurst() - t2.getBurst();
    }
}
