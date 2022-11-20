// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

package org.meowningmaster.processes_scheduler;

import java.util.LinkedList;
import java.io.*;

public class SchedulingAlgorithm {
  PrintStream out;
  LinkedList<sProcess> processes;

  public SchedulingAlgorithm(LinkedList<sProcess> processes) {
    this.processes = processes;
    
    String resultsFile = "Summary-Processes";
    try {
      out = new PrintStream(new FileOutputStream(resultsFile));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * Print process status
   */
  void print(int i, String state) {
    sProcess process = processes.get(i);
    String line = String.format("Process %d: %s (%s)", i, state, process.toString());
    out.println(line);
  }

  public Results run(int runtime, Results result) {
    int i = 0;
    int comptime = 0;
    int currentProcess = 0;
    int previousProcess = 0;
    int size = processes.size();
    int completed = 0;

    result.schedulingType = "Batch (Nonpreemptive)";
    result.schedulingName = "First-Come First-Served"; 
    
    sProcess process = processes.get(currentProcess);
    print(currentProcess, "registered");
    while (comptime < runtime) {
      if (process.cpudone == process.cputime) {
        completed++;
        print(currentProcess, "completed");
        if (completed == size) {
          result.compuTime = comptime;
          out.close();
          return result;
        }
        for (i = size - 1; i >= 0; i--) {
          process = processes.get(i);
          if (process.cpudone < process.cputime) { 
            currentProcess = i;
          }
        }
        process = processes.get(currentProcess);
        print(currentProcess, "registered");
      }      
      if (process.ioblocking == process.ionext) {
        print(currentProcess, "I/O blocked");
        process.numblocked++;
        process.ionext = 0; 
        previousProcess = currentProcess;
        for (i = size - 1; i >= 0; i--) {
          process = processes.get(i);
          if (process.cpudone < process.cputime && previousProcess != i) { 
            currentProcess = i;
          }
        }
        process = processes.get(currentProcess);
        print(currentProcess, "registered");
      }        
      process.cpudone++;       
      if (process.ioblocking > 0) {
        process.ionext++;
      }
      comptime++;
    }

    result.compuTime = comptime;
    return result;
  }
}
