package manager;

import java.util.Vector;
import bean.MemoryCell;
import bean.Process;


/**
 *
 * @author Fabricio Reis
 */
public class NextFitAlgorithm {

    public NextFitAlgorithm() {
    }

    /* This method implements the algorithm NEXT-FIT of memory management, which
     * looks for the first big enough free space to put the process from a initial position.
     * It receives three parameters: memory (Vector<MemoryCell>), process (Process) and initialPosition (int).
     * Its out is a vector object that contains a return a code which works like this:
     * first position = success ('1' for true or '0' for false); second position = stopedPosition (where the algorithm stops); third position = leftover (Cell's size - Process's size);
     * If the out (returnCode) is null, means none big enough free space has been found in the memory to put the process.
     */
    
     public Vector<Integer> toExecute_A(Vector<MemoryCell> memory, Process process, int initialPosition) {
        int leftover = -1; //This variable stores the leftover (Cell's size - Process's size).
        int success = 0; //This variable defines if the algorithm found a free space in the memory to the process (success = 1) or not (success = 0)
        int stopedPosition = 0; //This variable defines the memory position where the algorithm stops. If 'success' is equals to '1', that means the position is where the process is put in the memory
        Vector<Integer> returnCode = null; //This object is used to return a code which works like this:
                                          //first position = success ('1' for true or '0' for false); second position = stopedPosition (see more information above); third position = leftover (see more information above);
        MemoryCell cell = new MemoryCell();
        boolean found = false;

        // Try to find a big enough free space in the memory to put the process from the initial position received as parameter
        int i = initialPosition;
        while(!found) {
            if(i <= memory.size() - 1) {
                cell = memory.elementAt(i);
                if((process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                    leftover = cell.getSize() - process.getSize();
                    stopedPosition = i;
                    found = true;
                    success = 1;
                }
                i++;
            }
            else {
                if((i == memory.size()) && (!found)){  //Brazilian joke = "Gambiarra"
                    if(initialPosition == 0) {
                        found = true;
                        stopedPosition = -1;
                    }
                    else {
                        // If the initial position is not the first one (zero) and the algorithm hasn't found a solution yet, the algorithm try
                        // to find a big enough free space to put the process in the other memory's bit didn't search
                        i = 0;
                        while (!found) {
                            cell = memory.elementAt(i);
                            if((process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                                leftover = cell.getSize() - process.getSize();
                                stopedPosition = i;
                                found = true;
                                success = 1;
                            }
                            i++;                            
                            if((i == initialPosition) && (!found)) {
                                found = true;
                                stopedPosition = -1;
                            }
                        }
                    }
                }
            }
        }
        returnCode = new Vector<Integer>();
        returnCode.add(success);
        returnCode.add(stopedPosition);
        returnCode.add(leftover);
        return returnCode;
    }
     
    
    /* This method implements the algorithm NEXT-FIT of memory management, which
     * looks for the first big enough free space to put the process from a initial position.
     * It receives three parameters: memory (Vector<MemoryCell>), process (Process) and initialPosition (int).
     * Its out is a vector object that contains a return a code which works like this:
     * first position = success ('1' for true or '0' for false); second position = stopedPosition (where the algorithm stops); third position = leftover (Cell's size - Process's size);
     * Its out is the modified memory (includes the process in the memory).
     */
     
     public Vector<MemoryCell> toExecute_B(Vector<MemoryCell> memory, Process process, int initialPosition) {
        int leftover = -1; //This variable stores the leftover (Cell's size - Process's size).
        int success = 0; //This variable defines if the algorithm found a free space in the memory to the process (success = 1) or not (success = 0)
        int stopedPosition = 0; //This variable defines the memory position where the algorithm stops. If 'success' is equals to '1', that means the position is where the process is put in the memory
        Vector<MemoryCell> out = memory; //This is the method's return. The new memory!
        MemoryCell cell = new MemoryCell();
        boolean found = false;

        // Try to find a big enough free space in the memory to put the process from the initial position received as parameter
        int i = initialPosition;
        while(!found) {
            if(i <= memory.size() - 1) {
                cell = memory.elementAt(i);
                if((process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                    leftover = cell.getSize() - process.getSize();
                    stopedPosition = i;
                    found = true;
                    success = 1;
                }
                i++;
            }
            else {
                if((i == memory.size()) && (!found)){  //Brazilian joke = "Gambiarra"
                    if(initialPosition == 0) {
                        found = true;
                        stopedPosition = -1;
                    }
                    else {
                        // If the initial position is not the first one and the algorithm hasn't found a solution yet, the algorithm try
                        // to find a big enough free space to put the process in the other memory's bit didn't search
                        i = 0;
                        while (!found) {
                            cell = memory.elementAt(i);
                            if((process.getSize() <= cell.getSize()) && (cell.isIsFree())) {
                                leftover = cell.getSize() - process.getSize();
                                stopedPosition = i;
                                found = true;
                                success = 1;
                            }
                            i++;
                            if((i == initialPosition) && (!found)) {
                                found = true;
                                stopedPosition = -1;
                            }
                        }
                    }
                }
            }
        }
                
        if(success == 1) {
            MemoryCell auxCell = new MemoryCell(false, process, process.getSize());
            out.set(stopedPosition, auxCell);
            if(leftover > 0){
                auxCell = new MemoryCell(true, null ,leftover);
                out.add(stopedPosition + 1, auxCell);
            }
        }
        return out;
    }
}