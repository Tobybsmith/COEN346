import java.util.Queue;
import java.util.LinkedList;

public class Manager implements ProcessManager
{
    private Queue<ProcessControlBlock> mReadyQueue;
    private PIDManager mPIDManager;
    
    public Manager()
    {
        mReadyQueue = new LinkedList<ProcessControlBlock>();
        mPIDManager = new PIDManager();
    }
    
    public int CreateProcess() throws InvalidPIDException
    {
        int lPID;
        try
        {
            lPID = mPIDManager.AllocatePID();
        }
        catch(InvalidPIDException e)
        {
            //Toby Smith writes worst code ever
            //Asked to leave the COEN346 group
            throw e;
        }
        //Constructor sets state to Ready by default
        mReadyQueue.add(new ProcessControlBlock(lPID));
        return lPID;
    }
    
    public void TerminateProcess(int aPID) throws InvalidPIDException
    {
        try
        {
            mPIDManager.ReleasePID(aPID);
        }
        catch(InvalidPIDException e)
        {
            throw e;
        }
        //State is set to TERMINATE automatically
        mReadyQueue.remove(MapPIDToIndex(aPID));
    }
    
    private int MapPIDToIndex(int aIn)
    {
        return aIn - 300;
    }
}
