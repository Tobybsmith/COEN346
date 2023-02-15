public interface ProcessManager
{
    int CreateProcess() throws Exception;
    void TerminateProcess(int aPID) throws Exception;
}