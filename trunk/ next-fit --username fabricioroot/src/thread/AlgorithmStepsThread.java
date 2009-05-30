package thread;

import gui.MainScreen;
import bean.Process;
import bean.MemoryCell;
import manager.NextFitAlgorithm;
import manager.MemoryGenerator;
import java.util.Vector;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 *
 * @author Fabrício Reis
 */
public class AlgorithmStepsThread implements Runnable {

    JButton jButtonAlgorithmSteps;
    Vector<MemoryCell> finalMainMemory;
    MemoryGenerator memoryGenerator = new MemoryGenerator();
    Vector<Process> processesQueue;
    JPanel jPanelAnimation;
    JLabel jLabelNextStep;
    MainScreen mainScreen;
    boolean isJButtonOkClicked = false;
    JButton jButtonOkNextStep;
    JLabel jLabelAtDialogNextStep;
    int initialPosition = 0;
    int initialPositionBlocks = 0;
    
    public AlgorithmStepsThread(MainScreen mainScreen, JButton jButtonAlgorithmSteps, Vector<MemoryCell> finalMainMemory, Vector<Process> processesQueue, JPanel jPanelAnimation, int initialPositionBlocks, JButton jButtonOkNextStep) {
        this.mainScreen = mainScreen ;
        this.jButtonAlgorithmSteps = jButtonAlgorithmSteps;
        this.finalMainMemory = finalMainMemory;
        this.processesQueue = processesQueue;
        this.jPanelAnimation = jPanelAnimation;
        this.initialPositionBlocks = initialPositionBlocks;
        this.jButtonOkNextStep = jButtonOkNextStep;
    }
    
    public Vector<MemoryCell> getFinalMainMemory() {
        return this.finalMainMemory;
    }
    
    public int getInitialPositionBlocks() {
        return this.initialPositionBlocks;
    }

    public JButton getJButtonOkNextStep() {
        return this.jButtonOkNextStep;
    }

    public void setJButtonOkNextStep(JButton jButtonOkNextStep) {
        this.jButtonOkNextStep = jButtonOkNextStep;
    }

    public void run() {
        this.jButtonOkNextStep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isJButtonOkClicked = true;
            }
        });

        this.jButtonAlgorithmSteps.setEnabled(false);        
        
        this.finalMainMemory = this.memoryGenerator.decreaseProcessLifeTime(this.finalMainMemory);
        this.mainScreen.paintMainMemory(this.finalMainMemory);

        // Refreshes the value of 'initialPosition' after running the method 'decreaseProcessLifeTime' of the 'MemoryGenerator' class
        int k = 0;
        int auxCounter = 0;
        while(auxCounter < this.initialPositionBlocks) {
            auxCounter = auxCounter + this.finalMainMemory.elementAt(k).getSize();
            k++;
        }
        this.initialPosition = k;
        
        Process process = new Process();
        process.setSize(this.processesQueue.firstElement().getSize());
        process.setLifeTime(this.processesQueue.firstElement().getLifeTime());
        process.setId(this.processesQueue.firstElement().getId());
        this.processesQueue.remove(0);
        this.mainScreen.paintProcessesQueue(this.processesQueue);

        NextFitAlgorithm algorithm = new NextFitAlgorithm();

        //Semantically this object 'algorithmResult' determines if the algorithm found a solution
        //If the solution is found this object has the position where the process goes in and the leftover (Cell's size - Process's size)
        //See the corresponding method at the 'NextFitAlgorithm' class for more information.
        Vector<Integer> algorithmResult = algorithm.toExecute_A(this.finalMainMemory, process, this.initialPosition);
        
        if(algorithmResult.get(0) != 0) {

            Vector<MemoryCell> newMemory = algorithm.toExecute_B(this.finalMainMemory, process, this.initialPosition);
            
            int steps = 0; //This variable stores how many steps (blocks from the position = 0)) the process (represented like one block) has "to jump" to reach the next position available
            int orientationAxisY = 25;

            //It finds the 'steps' and walks until it reaches the position to go in
            for(int i = 0; i <= algorithmResult.get(1); i++){
                steps = steps + this.finalMainMemory.elementAt(i).getSize();
            }
            
            JTextField block = new JTextField();
            block.setText(String.valueOf(process.getSize()));
            block.setBackground(new java.awt.Color(51, 255, 255));
            block.setForeground(new java.awt.Color(0, 0, 0));
            block.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            block.setEditable(false);
            block.setToolTipText("Tamanho de P" + String.valueOf(process.getId()) + " = " +  String.valueOf(process.getSize()));
            this.jPanelAnimation.add(block);
            
            int j = 0;
            // If the initial position to paint the blocks is 0 (zero) -> It's gonna be like the FIRST-FIT algorithm
            if(this.initialPositionBlocks == 0) {
                if(steps <= 15) {
                    this.jButtonOkNextStep.setVisible(true);
                    block.setBounds(20, orientationAxisY, 30, 30);
                    while (j <= (steps - 1)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                    }
                    this.jButtonOkNextStep.setVisible(false);
                    j--;
                    block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                }
                else {
                    if((steps > 15) && (steps <= 30)) {
                        this.jButtonOkNextStep.setVisible(true);

                        // First row
                        block.setBounds(20, orientationAxisY, 30, 30);    
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }

                        // Second row
                        block.setBounds(20, (orientationAxisY + 60), 30, 30);
                        j = 0;
                        while (j <= (steps - 16)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        }
                        this.jButtonOkNextStep.setVisible(false);
                        j--;
                        block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                    }
                    else {
                        if((steps > 30) && (steps <= 45)){
                            this.jButtonOkNextStep.setVisible(true);

                            // First row
                            block.setBounds(20, orientationAxisY, 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }

                            // Second row
                            block.setBounds(20, (orientationAxisY + 60), 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }

                            // third row
                            block.setBounds(20, orientationAxisY + 120, 30, 30);
                            j = 0;
                            while (j <= (steps - 31)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20 + (j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }
                            this.jButtonOkNextStep.setVisible(false);
                            j--;
                            block.setBounds(20 + (j*35), (orientationAxisY + 120), 30, 30);
                        }
                    }
                }
            }
            else {
                //The initial position to paint the blocks is on the first row...
                if((this.initialPositionBlocks <= 15) && (steps >= this.initialPositionBlocks)){
                    if(steps <= 15) {
                        this.jButtonOkNextStep.setVisible(true);
                        j = (this.initialPositionBlocks - 1);
                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        while (j <= (steps - 1)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }
                        this.jButtonOkNextStep.setVisible(false);
                        j--;
                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    }
                    else {
                        if((steps > 15) && (steps <= 30)) {
                            this.jButtonOkNextStep.setVisible(true);

                            // First row
                            j = (this.initialPositionBlocks - 1);
                            block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }

                            // Second row
                            block.setBounds(20, (orientationAxisY + 60), 30, 30);
                            j = 0;
                            while (j <= (steps - 16)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }
                            this.jButtonOkNextStep.setVisible(false);
                            j--;
                            block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        }
                        else {
                            if((steps > 30) && (steps <= 45)){
                                this.jButtonOkNextStep.setVisible(true);

                                // First row
                                j = (this.initialPositionBlocks - 1);
                                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                }

                                // Second row
                                block.setBounds(20, (orientationAxisY + 60), 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }

                                // third row
                                block.setBounds(20, orientationAxisY + 120, 30, 30);
                                j = 0;
                                while (j <= (steps - 31)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20 + (j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }
                                this.jButtonOkNextStep.setVisible(false);
                                j--;
                                block.setBounds(20 + (j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }
                    }
                }
                else {
                    if((this.initialPositionBlocks <= 15) && (steps < this.initialPositionBlocks)){
                        this.jButtonOkNextStep.setVisible(true);

                        // First row
                        j = (this.initialPositionBlocks - 1);
                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }

                        // Second row
                        block.setBounds(20, (orientationAxisY + 60), 30, 30);
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        }

                        // third row
                        block.setBounds(20, (orientationAxisY + 120), 30, 30);
                        j = 0;
                        while (j <= 14) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            }
                        }

                        //Going back... First Row
                        block.setBounds(20, orientationAxisY, 30, 30);
                        j = 0;
                        while (j <= (steps - 1)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                            }
                        }
                        this.jButtonOkNextStep.setVisible(false);
                        j--;
                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                    }
                }

                //The initial position to paint the blocks is on the second row
                j = 0;
                if((this.initialPositionBlocks > 15) && (this.initialPositionBlocks <= 30) && (steps >= this.initialPositionBlocks)){
                    if(steps <= 30) {
                        this.jButtonOkNextStep.setVisible(true);
                        // Second row
                        j = (this.initialPositionBlocks - 16);
                        block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                        while (j <= (steps - 16)) {
                            if (this.isJButtonOkClicked) {
                                this.isJButtonOkClicked = false;
                                j++;
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        }
                        this.jButtonOkNextStep.setVisible(false);
                        j--;
                        block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                    }
                    else {
                        if((steps > 30) && (steps <= 45)){
                            this.jButtonOkNextStep.setVisible(true);

                            // Second row
                            j = (this.initialPositionBlocks - 16);
                            block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }

                            // third row
                            block.setBounds(20, (orientationAxisY + 120), 30, 30);
                            j = 0;
                            while (j <= (steps - 31)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }
                            this.jButtonOkNextStep.setVisible(false);
                            j--;
                            block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                        }
                    }
                }
                else {
                    if((this.initialPositionBlocks > 15) && (this.initialPositionBlocks <= 30) && (steps < this.initialPositionBlocks)){
                        if(steps <= 15) {
                            this.jButtonOkNextStep.setVisible(true);

                            // Second row
                            j = (this.initialPositionBlocks - 16);
                            block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                }
                            }

                            // third row
                            block.setBounds(20, (orientationAxisY + 120), 30, 30);
                            j = 0;
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }

                            //Going back... First Row
                            block.setBounds(20, orientationAxisY, 30, 30);
                            j = 0;
                            while (j <= (steps - 1)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }
                            this.jButtonOkNextStep.setVisible(false);
                            j--;
                            block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                        else {
                            if(steps > 15) {
                                this.jButtonOkNextStep.setVisible(true);

                                // Second row
                                j = (this.initialPositionBlocks - 16);
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }

                                // third row
                                block.setBounds(20, (orientationAxisY + 120), 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }

                                //Going back... First Row
                                block.setBounds(20, orientationAxisY, 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                }

                                //Going back... Second Row
                                block.setBounds(20, (orientationAxisY + 60), 30, 30);
                                j = 0;
                                while (j <= (steps - 16)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }
                                this.jButtonOkNextStep.setVisible(false);
                                j--;
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                        }
                    }
                }

                //The initial position to paint the blocks is on the third row
                if((this.initialPositionBlocks > 30) && (this.initialPositionBlocks <= 45) && (steps >= this.initialPositionBlocks)){
                    this.jButtonOkNextStep.setVisible(true);
                    j = (this.initialPositionBlocks - 31);
                    block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                    while (j <= (steps - 31)) {
                        if (this.isJButtonOkClicked) {
                            this.isJButtonOkClicked = false;
                            j++;
                            block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                        }
                    }
                    this.jButtonOkNextStep.setVisible(false);
                    j--;
                    block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                }
                else {
                    if((this.initialPositionBlocks > 30) && (this.initialPositionBlocks <= 45) && (steps < this.initialPositionBlocks)){
                        if(steps <= 15) {
                            this.jButtonOkNextStep.setVisible(true);

                            // Third Row
                            j = (this.initialPositionBlocks - 31);
                            block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                            while (j <= 14) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }

                            // Going back ... First row
                            block.setBounds(20, orientationAxisY, 30, 30);
                            j = 0;
                            while (j <= (steps - 1)) {
                                if (this.isJButtonOkClicked) {
                                    this.isJButtonOkClicked = false;
                                    j++;
                                    block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                }
                            }
                            this.jButtonOkNextStep.setVisible(false);
                            j--;
                            block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                        }
                        else {
                            if((steps > 15) && (steps <= 30)){
                                this.jButtonOkNextStep.setVisible(true);

                                // Third row
                                j = (this.initialPositionBlocks - 31);
                                block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    }
                                }

                                // Going back... First row
                                block.setBounds(20, orientationAxisY, 30, 30);
                                j = 0;
                                while (j <= 14) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                    }
                                }

                                // Going back... Second row
                                block.setBounds(20, (orientationAxisY + 60), 30, 30);
                                j = 0;
                                while (j <= (steps - 16)) {
                                    if (this.isJButtonOkClicked) {
                                        this.isJButtonOkClicked = false;
                                        j++;
                                        block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                    }
                                }
                                this.jButtonOkNextStep.setVisible(false);
                                j--;
                                block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                            }
                            else {
                                if((steps > 30) && (steps <= 45)){
                                    this.jButtonOkNextStep.setVisible(true);

                                    // Third row
                                    j = (this.initialPositionBlocks - 31);
                                    block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    }

                                    // Going back... First row
                                    block.setBounds(20, orientationAxisY, 30, 30);
                                    j = 0;
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block.setBounds(20+(j*35), orientationAxisY, 30, 30);
                                        }                                        
                                    }

                                    // Going back... Second row
                                    block.setBounds(20, (orientationAxisY + 60), 30, 30);
                                    j = 0;
                                    while (j <= 14) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block.setBounds(20+(j*35), (orientationAxisY + 60), 30, 30);
                                        }                                        
                                    }

                                    // Going back... Third row
                                    block.setBounds(20, (orientationAxisY + 120), 30, 30);
                                    j = 0;
                                    while (j <= (steps - 31)) {
                                        if (this.isJButtonOkClicked) {
                                            this.isJButtonOkClicked = false;
                                            j++;
                                            block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                        }
                                    }
                                    this.jButtonOkNextStep.setVisible(false);
                                    j--;
                                    block.setBounds(20+(j*35), (orientationAxisY + 120), 30, 30);
                                }
                            }
                        }
                    }
                }
            }

            this.finalMainMemory = newMemory;
            this.mainScreen.paintMainMemory(this.finalMainMemory);
            this.jPanelAnimation.add(block);
            block.setText("j");
            block.setBackground(new java.awt.Color(255, 255, 102));
            block.setToolTipText("Posição de parada da última busca");

            //It finds the new 'initialPositionBlocks' from the value returned by the corresponding method of the 'NextFitAlgorithm' class
            this.initialPositionBlocks = 0;
            for(int i = 0; i <= algorithmResult.get(1); i++){
                this.initialPositionBlocks = this.initialPositionBlocks + this.finalMainMemory.elementAt(i).getSize();
            }

            if(this.processesQueue.size() > 0) {
                this.jButtonAlgorithmSteps.setEnabled(true);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Não há espaço contínuo na memória grande suficiente para armazenar o processo!\n" +
                            "Por isso, ele será inserido novamente na fila (última posição).", "ATENÇÃO", JOptionPane.WARNING_MESSAGE);
            this.processesQueue.add(process);
            this.mainScreen.paintProcessesQueue(this.processesQueue);
            this.jButtonAlgorithmSteps.setEnabled(true);
        }
    }
}