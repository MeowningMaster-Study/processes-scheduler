// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

package org.meowningmaster.processes_scheduler;

import java.util.LinkedList;
import java.util.Random;
import java.io.*;

public class SchedulingAlgorithm {
  PrintStream out;
  LinkedList<sProcess> processes;
  sProcess process = null;
  LinkedList<sProcess> tickets = new LinkedList<>();
  int quantumSize = 20; // ms

  public SchedulingAlgorithm(LinkedList<sProcess> processes) {
    String resultsFile = "Summary-Processes";
    try {
      out = new PrintStream(new FileOutputStream(resultsFile));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    this.processes = processes;
    for (sProcess p : processes) {
      for (int i = 0; i < p.weight; i += 1) {
        tickets.add(p);
      }
    }
  }

  /**
   * Print process status
   */
  void print(String state) {
    String line = String.format("Process %d: %s (%s)", process.index, state, process);
    out.println(line);
  }

  /**
   * Select next process
   * @return size of the next quantum
   */
  public void elect() {
    int i = new Random().nextInt(tickets.size());
    process = processes.get(i);
    print("registered");
  }

  public Results run(int runtime, Results result) {
    result.schedulingType = "Preemptive";
    result.schedulingName = "Lottery";

    int completed = 0;
    int comptime = 0;
    for (; comptime <= runtime; comptime += quantumSize) {
      elect();

      process.cpudone += quantumSize;
      process.ionext += quantumSize;

      if (process.isCompleted()) {
        completed++;
        print("completed");
        if (completed == processes.size()) {
          result.compuTime = comptime;
          return result;
        }
        tickets.remove(process);
      }

      if (process.isIOBlocked()) {
        print("I/O blocked");
        process.numblocked += 1;
        process.ionext = 0;
      }
    }

    result.compuTime = comptime;
    return result;
  }
}
