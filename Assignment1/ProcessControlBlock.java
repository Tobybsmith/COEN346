public class ProcessControlBlock
{
    private int mPID;
    private PROCESS_STATE mProcessState;
    
    enum PROCESS_STATE
    {
        READY,
        EXECUTE,
        TERMINATE,
        WAIT,
        NEW,
        LAST_ENUM_STATE,
    }
    
    public ProcessControlBlock(int aPID)
    {
        mPID = aPID;
        mProcessState = PROCESS_STATE.READY;
    }
    
    public int GetPID()
    {
        return mPID;
    }
    
    @Override
    protected void finalize()
    {
        //When the object
        mProcessState = PROCESS_STATE.TERMINATE;
    }
}